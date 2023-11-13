package be.aboutcoding.kata.automaticsensorupdate.statuscheck.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FileResponse(
        @JsonProperty String id,
        @JsonProperty String name
) {
}
