package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;
import be.aboutcoding.kata.automaticsensorupdate.infrastructure.IdParser;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
class SensorStatusCheckProcess implements StatusCheckProcess {

    private final SensorRepository sensorRepository;
    private final TaskRepository taskRepository;
    private final IdParser parser;

    public SensorStatusCheckProcess(SensorRepository sensorRepository,
                                    TaskRepository taskRepository,
                                    IdParser parser) {
        this.sensorRepository = sensorRepository;
        this.taskRepository = taskRepository;
        this.parser = parser;
    }

    public List<TS50X> start(MultipartFile file) {

        var ids = parser.apply(file);

        //Step 1: get actual sensor information for the following ids
        var targetSensors = sensorRepository.getSensorsWithIdIn(ids);


        for (var sensor : targetSensors) {
            //Step 2: update the sensor firmware if necessary
            if (!sensor.hasValidFirmware()) {
                taskRepository.scheduleFirmwareUpdateFor(sensor.getId());
                sensor.setStatus(ShippingStatus.UPDATING_FIRMWARE);
            }

            //Step 3: update the sensor configuration if necessary or possible
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
