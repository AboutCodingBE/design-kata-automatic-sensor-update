package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.UpdateTask;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class TaskClient implements TaskRepository {

    private RestTemplate restTemplate;

    public TaskClient(@Value("${api.base-url}") String baseUrl, RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.rootUri(baseUrl).build();
    }

    @Override
    public void scheduleFirmwareUpdateFor(Long id) {
        var response = restTemplate.postForEntity("/task", Task.createFirmwareUpdateTaskFor(id), Long.class);
        if (response.getStatusCode().is5xxServerError()) {
            log.error("Creating a firmware update task for sensor with id {} failed", id);
        }
    }

    @Override
    public void scheduleConfigurationUpdateFor(Long id) {
        var response = restTemplate.postForEntity("/task", Task.createConfigUpdateTaskFor(id), Long.class);
        if (response.getStatusCode().is5xxServerError()) {
            log.error("Creating a firmware update task for sensor with id {} failed", id);
        }
    }

    @Override
    public void deleteTask(Long id) {
        restTemplate.delete("/task", id);
    }

    @Override
    public Optional<UpdateTask> getTask(Long id) {
        var taskResponse = restTemplate.getForEntity("task",TaskResponse.class, id);
        if (taskResponse.getStatusCode().is4xxClientError() || taskResponse.getBody() == null) {
            log.error("Can't find task with id {}", id);
            return Optional.empty();
        }

        return Optional.of(TaskResponse.toUpdateTask(taskResponse.getBody()));
    }
}
