package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.FirmwareValidation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class P100T {

    public static final String TARGET_CONFIGURATION = "p100t-20231201T13341200.cfg";

    @Getter
    private final Long id;
    @Getter
    private final String currentFirmwareVersion;
    private final String currentConfiguration;
    @Getter
    @Setter
    private ShippingStatus status;

    public P100T(Long id, String currentFirmwareVersion, String currentConfiguration) {
        this.id = id;
        this.currentFirmwareVersion = currentFirmwareVersion;
        this.currentConfiguration = currentConfiguration;
    }

    public boolean hasLatestConfiguration() {
        return this.currentConfiguration.equals(TARGET_CONFIGURATION);
    }
}
