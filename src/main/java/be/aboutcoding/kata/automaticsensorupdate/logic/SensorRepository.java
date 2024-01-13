package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;

import java.util.List;

/**
 * A little overengineered for what the project really is. It serves as a demonstration of text book Dependency Inversion
 */
public interface SensorRepository {

    List<TS50X> getSensorsWithIdIn(List<Long> ids);
}
