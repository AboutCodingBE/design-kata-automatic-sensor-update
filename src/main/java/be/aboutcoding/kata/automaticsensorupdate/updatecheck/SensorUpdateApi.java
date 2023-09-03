package be.aboutcoding.kata.automaticsensorupdate.updatecheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sensor/update")
@Slf4j
public class SensorUpdateApi {

    private CheckForUpdateProcess statusCheck;

    public SensorUpdateApi(CheckForUpdateProcess statusCheck) {
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
