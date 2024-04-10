package com.ianion.adventofcode.y2015.d6;

import com.google.common.collect.Sets;
import com.ianion.adventofcode.common.Coordinate;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Builder(toBuilder = true)
public record LightsV1(
        Set<Coordinate> switchedOn
) implements Lights {

    public static int countSwitchedOnAfterFollowingInstructions(List<SwitchInstruction> instructions) {
        return instructions.stream()
                .reduce(
                        LightsV1.create(),
                        Lights::apply,
                        (first, second) -> second)
                .getAnswer();
    }

    private static Lights create() {
        return LightsV1.builder()
                .switchedOn(Set.of())
                .build();
    }

    @Override
    public Lights switchOn(Set<Coordinate> toSwitchOn) {
        return this.toBuilder()
                .switchedOn(Stream.concat(switchedOn.stream(), toSwitchOn.stream()).collect(toSet()))
                .build();
    }

    @Override
    public Lights switchOff(Set<Coordinate> toSwitchOff) {
        return this.toBuilder()
                .switchedOn(Sets.filter(switchedOn, s -> !toSwitchOff.contains(s)))
                .build();
    }

    @Override
    public Lights toggle(Set<Coordinate> toToggle) {
        return this.toBuilder()
                .switchedOn(Stream.concat(
                        Sets.filter(switchedOn, s -> !toToggle.contains(s)).stream(),
                        Sets.filter(toToggle, s -> !switchedOn.contains(s)).stream()).collect(toSet()))
                .build();
    }

    @Override
    public int getAnswer() {
        return switchedOn.size();
    }
}
