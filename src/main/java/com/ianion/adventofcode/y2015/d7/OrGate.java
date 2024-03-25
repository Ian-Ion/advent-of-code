package com.ianion.adventofcode.y2015.d7;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
@Builder
public class OrGate implements LogicGate {

    Wire input1;
    Wire input2;
    Wire output;

    @Override
    public int fire(Map<Wire, Integer> currentSignals) {
        return currentSignals.get(input1) | currentSignals.get(input2);
    }

    @Override
    public Set<Wire> getDependencies() {
        return Set.of(input1, input2);
    }
}
