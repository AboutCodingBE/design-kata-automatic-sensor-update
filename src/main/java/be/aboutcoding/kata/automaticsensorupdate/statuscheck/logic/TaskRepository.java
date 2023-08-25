package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

public interface TaskRepository {

    void scheduleFirmwareUpdateFor(Long id);

    void scheduleConfigurationUpdateFor(Long id);
}
