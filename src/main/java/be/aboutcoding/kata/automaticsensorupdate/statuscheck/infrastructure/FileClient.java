package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class FileClient implements FileRepository {

    private RestTemplate restTemplate;

    public FileClient(@Value("${api.base-url}") String baseUrl, RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.rootUri(baseUrl).build();
    }

    @Override
    public Optional<String> getFileNameOf(String fileId) {
        var fileResponse = restTemplate.getForEntity("task",FileResponse.class, fileId);
        if (fileResponse.getStatusCode().is4xxClientError() || fileResponse.getBody() == null) {
            log.error("Can't find task with id {}", fileId);
            return Optional.empty();
        }

        return Optional.of(fileResponse.getBody().id());
    }
}
