package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.P100T;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Sensorinformation(@JsonProperty Long serial,
                                @JsonProperty String type,
                                @JsonProperty Integer statusId,
                                @JsonProperty String currentConfiguration,
                                @JsonProperty String  currentFirmware) {

    public static TS50X toTS50X(Sensorinformation info) {
        return new TS50X(info.serial, info.currentFirmware(), info.currentConfiguration());
    }

    public static P100T toP100T(Sensorinformation info) {
        return new P100T(info.serial, info.currentFirmware(), info.currentConfiguration());
    }
}
