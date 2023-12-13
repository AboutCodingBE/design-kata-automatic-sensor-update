package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.validation;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic.FirmwareValidation;
import org.springframework.stereotype.Component;

@Component
public class TS50XFirmwareValidation implements FirmwareValidation {

    private static final String VALID_FIRMWARE_VERSION = "59.1.12Rev4";

    @Override
    public boolean hasValidFirmware(String currentFirmwareVersion) {
        if (!VALID_FIRMWARE_VERSION.equals(currentFirmwareVersion)) {
            var currentVersion = new SemanticVersion(currentFirmwareVersion);
            var validVersion = new SemanticVersion(VALID_FIRMWARE_VERSION);
            return currentVersion.isEqualOrLargerThan(validVersion);
        }
        return true;
    }

    private static class SemanticVersion {
        int major = 0;

        int minor = 0;
        int patch = 0;
        int revision = 0;
        String rawVersion;

        SemanticVersion(String version) {
            this.rawVersion = version;
            parse();
        }

        void parse() {
            if (this.rawVersion != null && !this.rawVersion.isEmpty()) {
                String[] versionParts = rawVersion.split("\\.");
                try {
                    this.major = Integer.parseInt(versionParts[0]);
                    this.minor = Integer.parseInt(versionParts[1]);
                    String[] patchRevision = versionParts[2].split("Rev");
                    this.patch = Integer.parseInt(patchRevision[0]);

                    if (patchRevision.length == 2) {
                        this.revision = Integer.parseInt(patchRevision[1]);
                    }
                } catch (NumberFormatException formatException) {
                    this.major = 0;
                    this.minor = 0;
                    this.patch = 0;
                    this.revision = 0;
                }
            }
        }

        boolean isEqualOrLargerThan(SemanticVersion other) {
            if (other.major == this.major &&
                    other.minor == this.minor &&
                    other.patch == this.patch &&
                    other.revision == this.revision) {
                return true;
            }
            if (this.major > other.major) {
                return true;
            }
            if (this.major == other.major && this.minor > other.minor) {
                return true;
            }
            if (this.major == other.major && this.minor == other.minor && this.patch > other.patch) {
                return true;
            } else return this.major == other.major && this.minor == other.minor && this.patch == other.patch &&
                    this.revision > other.revision;
        }
    }
}
