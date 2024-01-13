package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;
import be.aboutcoding.kata.automaticsensorupdate.infrastructure.IdParser;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class SensorStatusCheckProcess implements StatusCheckProcess {

    private final SensorRepository sensorRepository;
    private final TaskRepository taskRepository;

    public SensorStatusCheckProcess(SensorRepository sensorRepository, TaskRepository taskRepository) {
        this.sensorRepository = sensorRepository;
        this.taskRepository = taskRepository;
    }

    public List<TS50X> start(List<Long> ids) {

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
