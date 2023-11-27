package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TS50X {

    private final Long id;
    private final String currentFirmwareVersion;
    private final String currentConfiguration;
    private ShippingStatus status;

    public TS50X(Long id, String firmwareVersion, String configuration) {
        this.id = id;
        this.currentFirmwareVersion = firmwareVersion;
        this.currentConfiguration = configuration;
    }

    public Long getId() {
        return this.id;
    }

}
