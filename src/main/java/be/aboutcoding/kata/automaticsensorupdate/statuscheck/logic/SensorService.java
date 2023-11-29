package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.SemanticVersion;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.Sensor;
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

    public List<Sensor> validateFirmwareOfSensors(MultipartFile file) {
        var ids = new ArrayList<Long>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            var csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ids.add(Long.parseLong(csvRecord.get("Id")));
            }

            var sensors = new ArrayList<Sensor>(ids.size());
            for (var sensorId : ids) {

                var response = restTemplate.getForEntity("/sensor/{id}", Sensorinformation.class, sensorId);
                if (response.getStatusCode().value() == 404) {
                    log.error("The sensor with id {} could not be found", sensorId);
                    continue;
                }
                else if (response.getStatusCode().is5xxServerError()) {
                    log.error("The id {} return no sensor information because it triggered an error", sensorId);
                    continue;
                }

                if (response.getBody() == null) {
                    continue;
                }

                var sensorInformation = response.getBody();
                Sensor sensor = null;
                if (!hasValidFirmware(sensorInformation)) {
                    sensor = taskService.scheduleTask(sensorInformation, true, null);
                }

                else if (!sensorInformation.currentConfiguration().equals(TARGET_CONFIGURATION)) {
                    sensor = taskService.scheduleTask(sensorInformation, false, TARGET_CONFIGURATION);
                }
                else {
                    sensor = Sensorinformation.toTS50X(sensorInformation);
                    sensor.setStatus(ShippingStatus.READY);
                }
                sensors.add(sensor);
            }
            return sensors;

        } catch (IOException e) {
            throw new RuntimeException("fail to parse id file: " + e.getMessage());
        }
    }

    private boolean hasValidFirmware(Sensorinformation sensorinformation) {
        if (!VALID_FIRMWARE_VERSION.equals(sensorinformation.currentFirmware())) {
            var currentVersion = new SemanticVersion(sensorinformation.currentFirmware());
            var validVersion = new SemanticVersion(VALID_FIRMWARE_VERSION);
            return currentVersion.isEqualOrLargerThan(validVersion);
        }
        return true;
    }
}
