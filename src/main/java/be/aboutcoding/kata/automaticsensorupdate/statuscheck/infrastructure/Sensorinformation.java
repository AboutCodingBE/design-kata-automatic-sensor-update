package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Sensorinformation(@JsonProperty Long serial,
                                @JsonProperty String type,
                                @JsonProperty Integer statusId,
                                @JsonProperty String currentConfiguration,
                                @JsonProperty String  currentFirmware,
                                @JsonProperty List<Long> taskQueue) {

    public static TS50X toTS50X(Sensorinformation info) {
        return new TS50X(info.serial, info.currentFirmware(), info.currentConfiguration(), info.taskQueue());
    }
}
