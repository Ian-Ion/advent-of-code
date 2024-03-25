package com.ianion.adventofcode.y2015.d7;

import java.util.Map;
import java.util.Set;

public interface LogicGate {

    Wire getOutput();

    int fire(Map<Wire, Integer> currentSignals);

    Set<Wire> getDependencies();
}
