package be.aboutcoding.kata.automaticsensorupdate.statuscheck;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure.SensorStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.SensorService;
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

    private SensorService statusCheck;

    public SensorStatusApi(SensorService statusCheck) {
        this.statusCheck = statusCheck;
    }

    @PostMapping
    public List<SensorStatus> getStatusFor(@RequestParam("file")MultipartFile file) {
        var sensors =  statusCheck.validateFirmwareOfSensors(file);
        return sensors.stream()
                .map(SensorStatus::from)
                .toList();
    }

}
