package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.UpdateTask;

import java.util.Optional;

/**
 * A little overengineered for what the project really is. It serves as a demonstration of text book Dependency Inversion
 */
public interface TaskRepository {

    void scheduleFirmwareUpdateFor(Long id);

    void scheduleConfigurationUpdateFor(Long id);

    void deleteTask(Long id);

    Optional<UpdateTask> getTask(Long id);
}
