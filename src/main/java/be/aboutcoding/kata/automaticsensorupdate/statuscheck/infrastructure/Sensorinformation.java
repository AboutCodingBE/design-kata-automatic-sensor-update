package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.P100T;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.SensorToValidate;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public record Sensorinformation(@JsonProperty Long serial,
                                @JsonProperty String type,
                                @JsonProperty Integer statusId,
                                @JsonProperty String currentConfiguration,
                                @JsonProperty String  currentFirmware) {

    public static TS50X toTS50X(Sensorinformation info) {
        return new TS50X(info.serial, info.currentFirmware(), info.currentConfiguration());
    }

    public static Optional<? extends  SensorToValidate> toSensor(Sensorinformation info) {
        switch (info.type.toLowerCase()) {
            case "ts50X":
                return Optional.of(new TS50X(info.serial, info.currentFirmware(), info.currentConfiguration()));
            case "p100t":
                return Optional.of(new P100T(info.serial, info.currentFirmware(), info.currentConfiguration()));
            default:
                log.warn("Type of sensor with id {} [{}] is not supported for this validation", info.serial(), info.type());
                return Optional.empty();
        }
    }
}
