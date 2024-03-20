package be.aboutcoding.kata.automaticsensorupdate;

import be.aboutcoding.kata.automaticsensorupdate.logic.StatusCheckProcess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
class SensorStatusApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatusCheckProcess statusCheckProcess;

    @InjectMocks
    private SensorStatusApi api;

//    @Test
//    void should_start_process_with_correct_parameters() throws Exception {
//        var idFile = aMultipartFileOf("file", "examples/sensors.csv");
//
//        mockMvc.perform(multipart("/sensor/status")
//                .file(idFile))
//                .andExpect(status().isOk());
//
//        verify(statusCheckProcess).start(List.of(123435L, 67890L, 34234455677L));
//    }
//
//    @Test
//    void can_deal_with_empty_csv_file() throws Exception {
//        var idFile = aMultipartFileOf("file", "examples/empty-sensors.csv");
//
//        mockMvc.perform(multipart("/sensor/status")
//                        .file(idFile))
//                .andExpect(status().isOk());
//
//        verify(statusCheckProcess).start(List.of());
//    }
//
//    @Test
//    void should_respond_with_bad_request_when_invalid_input_file() throws Exception {
//        var idFile = aMultipartFileOf("file", "examples/invalid-sensors.csv");
//
//        mockMvc.perform(multipart("/sensor/status")
//                        .file(idFile))
//                .andExpect(status().isBadRequest());
//    }
//
//    private MockMultipartFile aMultipartFileOf(String fileParameterName, String location) throws Exception {
//        var inputStream = new FileInputStream(ResourceUtils.getFile("classpath:" + location));
//        return new MockMultipartFile(fileParameterName,
//                "sensors.cvs",
//                MediaType.TEXT_PLAIN_VALUE,
//                inputStream);
//    }

}