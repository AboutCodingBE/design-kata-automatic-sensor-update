package be.aboutcoding.kata.automaticsensorupdate.statuscheck;

public record Task(Long id, TaskType type, String configurationFilename) {

    public static Task createConfigUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.CONFIGURATION_UPDATE, TS50X.TARGET_CONFIGURATION);
    }

    public static Task createFirmwareUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.FIRMWARE_UPDATE, null);
    }
}
