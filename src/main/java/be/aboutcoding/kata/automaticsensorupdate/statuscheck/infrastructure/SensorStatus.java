package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;

public record SensorStatus(Long id, String status) {

    public static SensorStatus from(TS50X sensor) {
        return new SensorStatus(sensor.getId(), sensor.getStatus().toString());
    }
}
