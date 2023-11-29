package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.Sensor;

public record SensorStatus(Long id, String status) {

    public static SensorStatus from(Sensor sensor) {
        return new SensorStatus(sensor.getId(), sensor.getStatus().toString());
    }
}
