package be.aboutcoding.kata.automaticsensorupdate;

import be.aboutcoding.kata.automaticsensorupdate.infrastructure.rest.SensorStatus;
import be.aboutcoding.kata.automaticsensorupdate.infrastructure.IdParser;
import be.aboutcoding.kata.automaticsensorupdate.logic.SensorStatusCheckProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sensor/status")
@Slf4j
public class SensorStatusApi {

    private SensorStatusCheckProcess statusCheck;

    public SensorStatusApi(SensorStatusCheckProcess statusCheck) {
        this.statusCheck = statusCheck;
    }

    @PostMapping
    public List<SensorStatus> getStatusFor(@RequestParam("file")MultipartFile file) {

        // Step 1: Get the actual input for our process to verify sensors
        var parser = new IdParser();
        var ids = parser.apply(file);

        var sensors =  statusCheck.start(ids);
        return sensors.stream()
                .map(SensorStatus::from)
                .toList();
    }

}
