package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.FirmwareValidation;
import lombok.Getter;
import lombok.Setter;

public class TS50X {

    public static final String TARGET_CONFIGURATION = "ts50x-20230811T10301211.cfg";

    private final Long id;
    @Getter
    private final String currentFirmwareVersion;
    private final String currentConfiguration;
    @Getter
    @Setter
    private ShippingStatus status;

    public TS50X(Long id, String firmwareVersion, String configuration) {
        this.id = id;
        this.currentFirmwareVersion = firmwareVersion;
        this.currentConfiguration = configuration;
    }

    public Long getId() {
        return this.id;
    }

    public boolean isUpdatingFirmware() {
        return this.status.equals(ShippingStatus.UPDATING_FIRMWARE);
    }

    public boolean hasLatestConfiguration() {
        return this.currentConfiguration.equals(TARGET_CONFIGURATION);
    }

}
