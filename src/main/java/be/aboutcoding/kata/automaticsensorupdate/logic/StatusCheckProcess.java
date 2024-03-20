package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StatusCheckProcess {

    List<TS50X> start(MultipartFile file);
}
