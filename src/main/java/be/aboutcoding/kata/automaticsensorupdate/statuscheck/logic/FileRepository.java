package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import java.util.Optional;

public interface FileRepository {

    Optional<String> getFileNameOf(String fileId);
}
