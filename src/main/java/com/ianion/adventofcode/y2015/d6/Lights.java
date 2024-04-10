package com.ianion.adventofcode.y2015.d6;

import com.ianion.adventofcode.common.Coordinate;

import java.util.Set;

public interface Lights {

    default Lights apply(SwitchInstruction instruction) {
        return switch (instruction.behavior()) {
            case SWITCH_ON -> switchOn(instruction.getAllAffectedSwitches());
            case SWITCH_OFF -> switchOff(instruction.getAllAffectedSwitches());
            case TOGGLE -> toggle(instruction.getAllAffectedSwitches());
        };
    }

    Lights switchOn(Set<Coordinate> toSwitchOn);

    Lights switchOff(Set<Coordinate> toSwitchOff);

    Lights toggle(Set<Coordinate> toToggle);

    int getAnswer();
}
