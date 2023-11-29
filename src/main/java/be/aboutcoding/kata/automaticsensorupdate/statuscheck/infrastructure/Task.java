package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

public record Task(Long id, TaskType type, String filename) {

    public static Task createConfigUpdateTaskFor(Long sensorId, String targetConfiguration) {
        return new Task(sensorId, TaskType.CONFIGURATION_UPDATE, targetConfiguration);
    }

    // todo: implement a way to specify the file for the firmware update
    public static Task createFirmwareUpdateTaskFor(Long sensorId) {
        return new Task(sensorId, TaskType.FIRMWARE_UPDATE, null);
    }
}
