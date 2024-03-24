package com.ianion.adventofcode.y2015.d6;

import com.google.common.collect.Sets;
import com.ianion.adventofcode.common.Coordinate;
import lombok.Builder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Slf4j
@UtilityClass
public class LightSwitcher {

    public static int countSwitchedOnAfterFollowing(List<SwitchInstruction> instructions) {
        return instructions.stream()
                .reduce(
                        LightSwitchState.create(),
                        LightSwitchState::apply,
                        (first, second) -> second)
                .switchedOn().size();
    }

    public enum Behavior {
        SWITCH_ON,
        SWITCH_OFF,
        TOGGLE
    }

    @Builder
    public record SwitchInstruction(
            Behavior behavior,
            Coordinate.CoordinatePair coordinates
    ) {

        public Set<Coordinate> getAllAffectedSwitches() {
            return IntStream
                    .rangeClosed(coordinates.first().x(), coordinates.second().x())
                    .mapToObj(x -> IntStream
                            .rangeClosed(coordinates.first().y(), coordinates.second().y())
                            .mapToObj(y -> Coordinate.builder().x(x).y(y).build()))
                    .flatMap(Function.identity())
                    .collect(toSet());
        }

    }

    @Builder(toBuilder = true)
    public record LightSwitchState(
            Set<Coordinate> switchedOn
    ) {

        public static LightSwitchState create() {
            return LightSwitchState.builder()
                    .switchedOn(Collections.emptySet())
                    .build();
        }

        public LightSwitchState apply(SwitchInstruction instruction) {
            log.info("Applying {}", instruction);
            return switch (instruction.behavior()) {
                case SWITCH_ON -> switchOn(instruction.getAllAffectedSwitches());
                case SWITCH_OFF -> switchOff(instruction.getAllAffectedSwitches());
                case TOGGLE -> toggle(instruction.getAllAffectedSwitches());
            };
        }

        private LightSwitchState switchOn(Set<Coordinate> toSwitchOn) {
            return this.toBuilder()
                    .switchedOn(Stream.concat(switchedOn.stream(), toSwitchOn.stream()).collect(toSet()))
                    .build();
        }

        private LightSwitchState switchOff(Set<Coordinate> toSwitchOff) {
            return this.toBuilder()
                    .switchedOn(Sets.filter(switchedOn, s -> !toSwitchOff.contains(s)))
                    .build();
        }

        private LightSwitchState toggle(Set<Coordinate> toToggle) {
            return this.toBuilder()
                    .switchedOn(Stream.concat(
                            Sets.filter(switchedOn, s -> !toToggle.contains(s)).stream(),
                            Sets.filter(toToggle, s -> !switchedOn.contains(s)).stream()).collect(toSet()))
                    .build();
        }
    }
}
