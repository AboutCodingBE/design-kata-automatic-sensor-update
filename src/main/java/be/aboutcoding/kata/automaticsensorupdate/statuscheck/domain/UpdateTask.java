package be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTask {
    private Long id;
    private Long sensorSerial;
    private String type;
    private String fileId;

    public boolean isFirmwareUpdate() {
        return this.type.equalsIgnoreCase("update_firmware");
    }

    public boolean isConfigurationUpdate() {
        return this.type.equalsIgnoreCase("update_configuration");
    }

}
