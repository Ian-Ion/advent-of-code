package com.ianion.adventofcode.y2015.d6;

import com.ianion.adventofcode.common.Coordinate;
import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@UtilityClass
public class LightSwitcher {

    public static int countSwitchedOnAfterFollowingV1Instructions(List<SwitchInstruction> instructions) {
        return instructions.stream()
                .reduce(
                        LightSwitchStateV1.create(),
                        LightSwitchState::apply,
                        (first, second) -> second)
                .getAnswer();
    }

    public static int countSwitchedOnAfterFollowingV2Instructions(List<SwitchInstruction> instructions) {
        return instructions.stream()
                .reduce(
                        LightSwitchStateV2.create(),
                        LightSwitchState::apply,
                        (first, second) -> second)
                .getAnswer();
    }

    public enum Behavior {
        SWITCH_ON,
        SWITCH_OFF,
        TOGGLE
    }

    @Builder
    public record SwitchInstruction(
            Behavior behavior,
            Coordinate start,
            Coordinate end
    ) {

        public Set<Coordinate> getAllAffectedSwitches() {
            return IntStream
                    .rangeClosed(start.x(), end.x())
                    .mapToObj(x -> IntStream
                            .rangeClosed(start.y(), end.y())
                            .mapToObj(y -> Coordinate.builder().x(x).y(y).build()))
                    .flatMap(Function.identity())
                    .collect(toSet());
        }
    }
}
