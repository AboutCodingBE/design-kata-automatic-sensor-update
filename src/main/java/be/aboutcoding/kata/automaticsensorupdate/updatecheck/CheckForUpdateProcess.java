package be.aboutcoding.kata.automaticsensorupdate.updatecheck;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class CheckForUpdateProcess {

    private final SensorInformationClient sensorClient;
    private final TaskClient taskClient;

    public CheckForUpdateProcess(SensorInformationClient sensorClient, TaskClient taskClient) {
        this.sensorClient = sensorClient;
        this.taskClient = taskClient;
    }

    public List<TS50X> start(MultipartFile file) {
        //Step 1: parse the ids from the file
        var parser = new IdParser();
        var ids = parser.apply(file);

        //Step 2: get actual sensor information for the following ids
        var targetSensors = sensorClient.getSensorsWithIdIn(ids);

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
