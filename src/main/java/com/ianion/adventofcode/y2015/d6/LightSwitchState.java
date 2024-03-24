package com.ianion.adventofcode.y2015.d6;

import com.ianion.adventofcode.common.Coordinate;

import java.util.Set;

public interface LightSwitchState {

    default LightSwitchState apply(LightSwitcher.SwitchInstruction instruction) {
        return switch (instruction.behavior()) {
            case SWITCH_ON -> switchOn(instruction.getAllAffectedSwitches());
            case SWITCH_OFF -> switchOff(instruction.getAllAffectedSwitches());
            case TOGGLE -> toggle(instruction.getAllAffectedSwitches());
        };
    }

    LightSwitchState switchOn(Set<Coordinate> toSwitchOn);

    LightSwitchState switchOff(Set<Coordinate> toSwitchOff);

    LightSwitchState toggle(Set<Coordinate> toToggle);

    int getAnswer();
}
