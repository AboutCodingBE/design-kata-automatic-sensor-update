package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.ShippingStatus;
import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorStatusCheckProcessTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SensorStatusCheckProcess statusCheckProcess;

    @Test
    void should_schedule_a_firmware_update_if_firmware_is_outdated_have_correct_status() throws Exception {
        // given
        var idFile = aMultipartFileOf("file", "examples/invalid-sensors.csv");
        var sensorId = 1L;
        when(sensorRepository.getSensorsWithIdIn(List.of(sensorId))).thenReturn(List.of(anOutdatedSensorWithId(1L)));

        // when
        var result = statusCheckProcess.start(idFile);

        // then
        verify(taskRepository, times(1)).scheduleFirmwareUpdateFor(1L);
        assertThat(result)
                .hasSize(1)
                .extracting(TS50X::getStatus)
                .containsExactly(ShippingStatus.UPDATING_FIRMWARE);
    }

    @Test
    void should_schedule_a_configuration_update_when_not_latest_configuration() throws Exception {
        // given
        var idFile = aMultipartFileOf("file", "examples/invalid-sensors.csv");
        var sensorId = 1L;
        when(sensorRepository.getSensorsWithIdIn(List.of(sensorId))).thenReturn(List.of(aSensorWithoutLatestConfig(1L)));

        // when
        var result = statusCheckProcess.start(idFile);

        // then
        verify(taskRepository, times(1)).scheduleConfigurationUpdateFor(1L);
        assertThat(result)
                .hasSize(1)
                .extracting(TS50X::getStatus)
                .containsExactly(ShippingStatus.UPDATING_CONFIGURATION);
    }

    @Test
    void should_correctly_handle_a_valid_sensor() throws Exception {
        // given
        var idFile = aMultipartFileOf("file", "examples/invalid-sensors.csv");
        var sensorId = 1L;
        when(sensorRepository.getSensorsWithIdIn(List.of(sensorId))).thenReturn(List.of(aValidSensor(1L)));

        // when
        var result = statusCheckProcess.start(idFile);

        // then
        verifyNoInteractions(taskRepository);
        assertThat(result)
                .hasSize(1)
                .extracting(TS50X::getStatus)
                .containsExactly(ShippingStatus.READY);
    }

    private MockMultipartFile aMultipartFileOf(String fileParameterName, String location) throws Exception {
        var inputStream = new FileInputStream(ResourceUtils.getFile("classpath:" + location));
        return new MockMultipartFile(fileParameterName,
                "sensors.cvs",
                MediaType.TEXT_PLAIN_VALUE,
                inputStream);
    }

    private TS50X anOutdatedSensorWithId(Long id) {
        return new TS50X(id, "58.1.1Rev2", "config.cfg");
    }

    private TS50X aSensorWithoutLatestConfig(Long id) {
        return new TS50X(id, "59.12.1Rev2", "config.cfg");
    }

    private TS50X aValidSensor(Long id) {
        return new TS50X(id, "59.12.1Rev2", "ts50x-20230811T10301211.cfg");
    }
}