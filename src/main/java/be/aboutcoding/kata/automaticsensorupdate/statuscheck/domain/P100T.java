package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class P100T implements FirmwareValidation {

    private static final String VALID_FIRMWARE_VERSION = "2023-09-10R2";
    public static final String TARGET_CONFIGURATION = "p100t-20231201T13341200.cfg";
    public static final String REVISION_DELIMITER = "R";

    @Getter
    private final Long id;
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

    @Override
    public boolean hasValidFirmware() {
        var currentFirmwareParsed = this.currentFirmwareVersion.split(REVISION_DELIMITER);
        var validFirmwareParsed = VALID_FIRMWARE_VERSION.split(REVISION_DELIMITER);

        var currenFirmwareDate = parseDateFrom(currentFirmwareParsed[0]);
        var validFirmwareDate = parseDateFrom(validFirmwareParsed[0]);

        if (currenFirmwareDate.equals(validFirmwareDate)) {
            if (currentFirmwareParsed.length > 1) {
                return Integer.parseInt(currentFirmwareParsed[1]) >= Integer.parseInt(validFirmwareParsed[1]);
            }
        }
        return false;
    }

    private LocalDate parseDateFrom(String dateString) {
        var parts = dateString.split("-");
        return LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    public boolean hasLatestConfiguration() {
        return this.currentConfiguration.equals(TARGET_CONFIGURATION);
    }
}
