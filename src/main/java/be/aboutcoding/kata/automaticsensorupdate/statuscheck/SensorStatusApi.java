package be.aboutcoding.kata.automaticsensorupdate.statuscheck;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure.SensorStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.SensorStatusCheckProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                log.info("Id: {}", Long.parseLong(csvRecord.get("Id")));
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }

        return null;
    }

}
