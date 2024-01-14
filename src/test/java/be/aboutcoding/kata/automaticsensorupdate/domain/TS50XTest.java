package be.aboutcoding.kata.automaticsensorupdate.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TS50XTest {

    @ParameterizedTest
    @MethodSource("getTestValues")
    void should_verify_firmware_correctly(TS50X input, Boolean expected) {
        assertThat(input.hasValidFirmware()).isEqualTo(expected);
    }

    private static Stream<Arguments> getTestValues() {
        return Stream.of(
                Arguments.of(new TS50X(1L, "59.1.12Rev4", "config"), true),
                Arguments.of(new TS50X(2L, "60.1.12Rev4", "config"), true),
                Arguments.of(new TS50X(3L, "59.2.13Rev4", "config"), true),
                Arguments.of(new TS50X(4L, "59.1.12Rev10", "config"), true),
                Arguments.of(new TS50X(5L, "58.2.12Rev4", "config"), false),
                Arguments.of(new TS50X(6L, "59.0.12Rev4", "config"), false),
                Arguments.of(new TS50X(7L, "59.1.10Rev4", "config"), false),
                Arguments.of(new TS50X(8L, "59.1.12Rev2", "config"), false)
        );
    }

}