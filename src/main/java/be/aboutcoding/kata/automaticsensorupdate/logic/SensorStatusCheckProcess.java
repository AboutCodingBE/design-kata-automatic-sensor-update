package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class SensorStatusCheckProcess {

    private final SensorInformationClient sensor;
    private final TaskClient taskClient;

    public SensorStatusCheckProcess(SensorInformationClient sensor, TaskClient taskClient) {
        this.sensor = sensor;
        this.taskClient = taskClient;
    }

    public List<TS50X> start(MultipartFile file) {
        //Step 1: parse the ids from the file
        var parser = new IdParser();
        var ids = parser.apply(file);

        //Step 2: get actual sensor information for the following ids
        var targetSensors = sensor.getSensorsWithIdIn(ids);

        //Step 2: update the sensor firmware if necessary
        for (var sensor : targetSensors) {
            if (!sensor.hasValidFirmware()) {
                taskClient.scheduleFirmwareUpdateFor(sensor.getId());
                sensor.setStatus(ShippingStatus.UPDATING_FIRMWARE);
            }
        }

        //Step 4: update the sensor configuration if necessary or possible
        for (var sensor : targetSensors) {
            if (!sensor.isUpdatingFirmware() && !sensor.hasLatestConfiguration()) {
                taskClient.scheduleConfigurationUpdateFor(sensor.getId());
                sensor.setStatus(ShippingStatus.UPDATING_CONFIGURATION);
            }
        }

        return targetSensors;
    }
}
