package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.FirmwareValidation;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Even though we have pulled out the behaviour from the sensors into a separate abstraction, we still need
 * to change the name of the sensor implementation, as one sensor class will represent all the different types
 * of sensors now.
 */
@Component
@Slf4j
public class SensorInformationClient implements SensorRepository {

    private final RestTemplate restTemplate;

    public SensorInformationClient(@Value("${api.base-url}") String baseUrl, RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.rootUri(baseUrl).build();
    }

    @Override
    public List<TS50X> getSensorsWithIdIn(List<Long> ids) {
        return ids.stream()
                .map(this::getInformationFor)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::getFromType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
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

    private Optional<FirmwareValidation> getFromType(Sensorinformation information) {
        switch (information.type().toLowerCase()) {
            case "ts50x":
                return Optional.of(Sensorinformation.toTS50X(information));
            case "p100t":
                return Optional.of(Sensorinformation.toP100T(information));
            default:
                log.error("Sensor with id {} is of a type tha this not supported", information.type());
                return Optional.empty();
        }
    }
}
