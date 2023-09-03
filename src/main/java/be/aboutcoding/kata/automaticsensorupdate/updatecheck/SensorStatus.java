package be.aboutcoding.kata.automaticsensorupdate.updatecheck;

public record SensorStatus(Long id, String status) {

    public static SensorStatus from(TS50X sensor) {
        return new SensorStatus(sensor.getId(), sensor.getStatus().toString());
    }
}
