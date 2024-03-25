package com.ianion.adventofcode.y2015.d7;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
@Builder
public class RightShiftGate implements LogicGate {

    Wire input;
    int amount;
    Wire output;

    @Override
    public int fire(Map<Wire, Integer> currentSignals) {
        return currentSignals.get(input) >>> amount;
    }

    @Override
    public Set<Wire> getDependencies() {
        return Set.of(input);
    }
}
