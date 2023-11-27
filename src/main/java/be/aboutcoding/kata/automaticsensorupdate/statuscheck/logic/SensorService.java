package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.SemanticVersion;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure.Sensorinformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SensorService {

    private static final String VALID_FIRMWARE_VERSION = "59.1.12Rev4";
    public static final String TARGET_CONFIGURATION = "ts50x-20230811T10301211.cfg";

    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final TaskService taskService;

    public SensorService(@Value("${api.base-url}") String baseUrl, RestTemplate template, TaskService taskService) {
        this.baseUrl = baseUrl;
        this.restTemplate = template;
        this.taskService = taskService;
    }

    public List<TS50X> validateFirmwareOfSensors(MultipartFile file) {
        var ids = new ArrayList<Long>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            var csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ids.add(Long.parseLong(csvRecord.get("Id")));
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to parse id file: " + e.getMessage());
        }

        var targetSensors = ids.stream()
                .map(this::getInformationFor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Sensorinformation::toTS50X)
                .collect(Collectors.toList());


        for (var sensor : targetSensors) {
            if (!hasValidFirmware(sensor)) {
                taskService.scheduleFirmwareUpdateFor(sensor.getId());
                sensor.setStatus(ShippingStatus.UPDATING_FIRMWARE);
            }

            //Step 4: update the sensor configuration if necessary or possible
            else if (!sensor.getStatus().equals(ShippingStatus.UPDATING_FIRMWARE) ||
                    !sensor.getCurrentConfiguration().equals(TARGET_CONFIGURATION)) {
                taskService.scheduleConfigurationUpdateFor(sensor.getId(), TARGET_CONFIGURATION);
                sensor.setStatus(ShippingStatus.UPDATING_CONFIGURATION);
            }
            else {
                sensor.setStatus(ShippingStatus.READY);
            }
        }

        return targetSensors;
    }

    private Optional<Sensorinformation> getInformationFor(Long id) {
        var response = restTemplate.getForEntity("/sensor/{id}", Sensorinformation.class, id);
        if (response.getStatusCode().value() == 404) {
            log.error("The sensor with id {} could not be found", id);
            return Optional.empty();
        }
        else if (response.getStatusCode().is5xxServerError()) {
            log.error("The id {} return no sensor information because it triggered an error", id);
            return Optional.empty();
        }

        return Optional.ofNullable(response.getBody());
    }

    private boolean hasValidFirmware(TS50X sensor) {
        if (!VALID_FIRMWARE_VERSION.equals(sensor.getCurrentFirmwareVersion())) {
            var currentVersion = new SemanticVersion(sensor.getCurrentFirmwareVersion());
            var validVersion = new SemanticVersion(VALID_FIRMWARE_VERSION);
            return currentVersion.isEqualOrLargerThan(validVersion);
        }
        return true;
    }
}
