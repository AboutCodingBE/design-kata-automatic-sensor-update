package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.UpdateTask;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskResponse(
        @JsonProperty Long id,
        @JsonProperty Long sensorSerial,
        @JsonProperty String type,
        @JsonProperty String fileId) {

    public static UpdateTask toUpdateTask(TaskResponse taskResponse) {
        var task = new UpdateTask();
        task.setId(taskResponse.id());
        task.setSensorSerial(taskResponse.sensorSerial());
        task.setType(taskResponse.type());
        task.setFileId(taskResponse.fileId());

        return task;
    }
}
