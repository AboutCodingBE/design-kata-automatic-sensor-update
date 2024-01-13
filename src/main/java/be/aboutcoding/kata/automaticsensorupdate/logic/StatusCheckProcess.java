package be.aboutcoding.kata.automaticsensorupdate.logic;

import be.aboutcoding.kata.automaticsensorupdate.domain.TS50X;

import java.util.List;

public interface StatusCheckProcess {

    List<TS50X> start(List<Long> ids);
}
