package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

/**
 * A little bit over engineered for what the project really is. It serves as a demonstration of text book Dependency Inversion
 */
public interface TaskRepository {

    void scheduleFirmwareUpdateFor(Long id);

    void scheduleConfigurationUpdateFor(Long id);
}
