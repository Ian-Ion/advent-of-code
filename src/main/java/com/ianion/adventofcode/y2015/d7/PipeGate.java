package com.ianion.adventofcode.y2015.d7;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
@Builder
public class PipeGate implements LogicGate {

    Wire input;
    Wire output;

    @Override
    public int fire(Map<Wire, Integer> currentSignals) {
        return currentSignals.get(input);
    }

    @Override
    public Set<Wire> getDependencies() {
        return Set.of(input);
    }
}
