package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.FirmwareValidation;
import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;

import java.util.List;

/**
 * A little overengineered for what the project really is. It serves as a demonstration of text book Dependency Inversion
 */
public interface SensorRepository {

    List<FirmwareValidation> getSensorsWithIdIn(List<Long> ids);
}
