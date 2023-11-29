package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Sensor {

    private final Long id;
    private final String currentFirmwareVersion;
    private final String currentConfiguration;
    private ShippingStatus status;

    public Sensor(Long id, String firmwareVersion, String configuration) {
        this.id = id;
        this.currentFirmwareVersion = firmwareVersion;
        this.currentConfiguration = configuration;
    }
}
