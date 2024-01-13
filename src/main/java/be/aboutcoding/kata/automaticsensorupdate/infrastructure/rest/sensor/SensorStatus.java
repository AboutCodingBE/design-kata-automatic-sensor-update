package be.aboutcoding.kata.automaticsensorupdate.infrastructure.rest.sensor;

import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;

public record SensorStatus(Long id, String status) {

    public static SensorStatus from(TS50X sensor) {
        return new SensorStatus(sensor.getId(), sensor.getStatus().toString());
    }
}
