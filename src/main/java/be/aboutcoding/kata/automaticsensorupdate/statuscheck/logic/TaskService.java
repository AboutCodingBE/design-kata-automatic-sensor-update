package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.Sensor;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure.Sensorinformation;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class TaskService {

    private RestTemplate restTemplate;

    public TaskService(@Value("${api.base-url}") String baseUrl, RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.rootUri(baseUrl).build();
    }

    public Sensor scheduleTask(Sensorinformation sensorinformation, boolean firmwareTask, String targetConfiguration) {
        var sensor = Sensorinformation.toTS50X(sensorinformation);
        if (firmwareTask) {
            var response = restTemplate.postForEntity("/task", Task.createFirmwareUpdateTaskFor(sensor.getId()), Long.class);
            if (response.getStatusCode().is5xxServerError()) {
                log.error("Creating a firmware update task for sensor with id {} failed", sensor.getId());
            }
            sensor.setStatus(ShippingStatus.UPDATING_FIRMWARE);
        }
        else {
            var task = Task.createConfigUpdateTaskFor(sensor.getId(), targetConfiguration);
            var response = restTemplate.postForEntity("/task", task, Long.class);
            if (response.getStatusCode().is5xxServerError()) {
                log.error("Creating a firmware update task for sensor with id {} failed", sensor.getId());
            }
            sensor.setStatus(ShippingStatus.UPDATING_CONFIGURATION);
        }

        return sensor;
    }
}
