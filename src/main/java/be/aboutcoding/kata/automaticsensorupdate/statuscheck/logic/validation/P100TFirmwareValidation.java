package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.validation;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.FirmwareValidation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class P100TFirmwareValidation implements FirmwareValidation {

    private static final String VALID_FIRMWARE_VERSION = "2023-09-10R2";
    public static final String REVISION_DELIMITER = "R";

    @Override
    public boolean hasValidFirmware(String currentFirmwareVersion) {
        var currentFirmwareParsed = currentFirmwareVersion.split(REVISION_DELIMITER);
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
}
