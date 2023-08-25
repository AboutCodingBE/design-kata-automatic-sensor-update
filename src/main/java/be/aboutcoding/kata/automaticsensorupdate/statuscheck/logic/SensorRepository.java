package be.aboutcoding.kata.automaticsensorupdate.statuscheck.logic;

import be.aboutcoding.kata.automaticsensorupdate.statuscheck.domain.TS50X;

import java.util.List;

public interface SensorRepository {

    List<TS50X> getSensorsWithIdIn(List<Long> ids);
}
