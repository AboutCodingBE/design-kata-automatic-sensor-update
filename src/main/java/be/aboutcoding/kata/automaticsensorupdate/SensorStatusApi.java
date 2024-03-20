package be.aboutcoding.kata.automaticsensorupdate;

import be.aboutcoding.kata.automaticsensorupdate.infrastructure.rest.sensor.SensorStatus;
import be.aboutcoding.kata.automaticsensorupdate.infrastructure.IdParser;
import be.aboutcoding.kata.automaticsensorupdate.logic.StatusCheckProcess;
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

    private StatusCheckProcess statusCheck;

    public SensorStatusApi(StatusCheckProcess statusCheck) {
        this.statusCheck = statusCheck;
    }

    @PostMapping
    public List<SensorStatus> getStatusFor(@RequestParam("file")MultipartFile file) {

        var sensors =  statusCheck.start(file);
        return sensors.stream()
                .map(SensorStatus::from)
                .toList();
    }

}
