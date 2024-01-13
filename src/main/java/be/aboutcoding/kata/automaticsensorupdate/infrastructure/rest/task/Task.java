package be.aboutcoding.kata.automaticsensorupdate.infrastructure.rest.task;

import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;

public record Task(Long id, TaskType type, String filename) {

    public static Task createConfigUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.CONFIGURATION_UPDATE, TS50X.TARGET_CONFIGURATION);
    }

    // todo: implement a way to specify the file for the firmware update
    public static Task createFirmwareUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.FIRMWARE_UPDATE, null);
    }
}
