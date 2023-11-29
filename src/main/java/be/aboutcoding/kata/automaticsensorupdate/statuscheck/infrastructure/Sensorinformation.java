package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Sensorinformation(@JsonProperty Long serial,
                                @JsonProperty String type,
                                @JsonProperty Integer statusId,
                                @JsonProperty String currentConfiguration,
                                @JsonProperty String  currentFirmware) {

    public static Sensor toTS50X(Sensorinformation info) {
        return new Sensor(info.serial, info.currentFirmware(), info.currentConfiguration());
    }
}
