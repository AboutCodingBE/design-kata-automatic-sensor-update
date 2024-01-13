package be.aboutcoding.kata.automaticsensorupdate.logic;

/**
 * A little overengineered for what the project really is. It serves as a demonstration of text book Dependency Inversion
 */
public interface TaskRepository {

    void scheduleFirmwareUpdateFor(Long id);

    void scheduleConfigurationUpdateFor(Long id);
}
