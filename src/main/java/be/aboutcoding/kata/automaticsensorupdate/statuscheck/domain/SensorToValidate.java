package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class SensorToValidate {

    private final Long id;
    private final String currentFirmwareVersion;
    private final String currentConfiguration;
    @Getter
    @Setter
    private ShippingStatus status;

    public SensorToValidate(Long id, String currentFirmwareVersion, String currentConfiguration) {
        this.id = id;
        this.currentFirmwareVersion = currentFirmwareVersion;
        this.currentConfiguration = currentConfiguration;
    }

    public abstract boolean hasValidFirmware();

    public abstract boolean hasLatestConfiguration();

    public boolean isUpdatingFirmware() {
        return this.status.equals(ShippingStatus.UPDATING_FIRMWARE);
    }
}
