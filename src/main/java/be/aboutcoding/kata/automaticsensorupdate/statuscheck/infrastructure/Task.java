package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;

public record Task(Long id, TaskType type, String filename) {

    public static Task createConfigUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.CONFIGURATION_UPDATE, TS50X.TARGET_CONFIGURATION);
    }

    // todo: implement a way to specify the file for the firmware update
    public static Task createFirmwareUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.FIRMWARE_UPDATE, null);
    }
}
