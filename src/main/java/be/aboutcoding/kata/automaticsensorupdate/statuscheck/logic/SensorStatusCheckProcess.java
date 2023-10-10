package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class SensorStatusCheckProcess {

    private final SensorRepository sensorRepository;
    private final TaskRepository taskRepository;

    public SensorStatusCheckProcess(SensorRepository sensorRepository, TaskRepository taskRepository) {
        this.sensorRepository = sensorRepository;
        this.taskRepository = taskRepository;
    }

    public List<TS50X> start(MultipartFile file) {
        //Step 1: parse the ids from the file
        var parser = new IdParser();
        var ids = parser.apply(file);

        //Step 2: get actual sensor information for the following ids
        var targetSensors = sensorRepository.getSensorsWithIdIn(ids);


        for (var sensor : targetSensors) {
            //Step 3: update the sensor firmware if necessary
            if (!sensor.hasValidFirmware()) {
                taskRepository.scheduleFirmwareUpdateFor(sensor.getId());
                sensor.setStatus(ShippingStatus.UPDATING_FIRMWARE);
            }

            //Step 4: update the sensor configuration if necessary or possible
            else if (!sensor.isUpdatingFirmware() && !sensor.hasLatestConfiguration()) {
                taskRepository.scheduleConfigurationUpdateFor(sensor.getId());
                sensor.setStatus(ShippingStatus.UPDATING_CONFIGURATION);
            }
            else {
                sensor.setStatus(ShippingStatus.READY);
            }
        }

        return targetSensors;
    }
}
