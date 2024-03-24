package com.ianion.adventofcode.y2015.d6;

import com.google.common.collect.ImmutableMap;
import com.ianion.adventofcode.common.Coordinate;
import lombok.Builder;

import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record LightSwitchStateV2(
        Map<Coordinate, Integer> brightness
) implements LightSwitchState {

    public static LightSwitchState create() {
        return LightSwitchStateV2.builder()
                .brightness(Map.of())
                .build();
    }

    @Override
    public LightSwitchState switchOn(Set<Coordinate> toSwitchOn) {
        return increaseBrightnessBy(toSwitchOn, 1);
    }

    @Override
    public LightSwitchState switchOff(Set<Coordinate> toSwitchOff) {
        return increaseBrightnessBy(toSwitchOff, -1);
    }

    @Override
    public LightSwitchState toggle(Set<Coordinate> toToggle) {
        return increaseBrightnessBy(toToggle, 2);
    }

    @Override
    public int getAnswer() {
        return brightness.values().stream().reduce(0, Integer::sum);
    }

    private LightSwitchStateV2 increaseBrightnessBy(Set<Coordinate> toSwitchOn, int brightnessDelta) {
        var toChange = calculateChanges(toSwitchOn, c -> brightness.getOrDefault(c, 0) + brightnessDelta);
        var unaffected = getUnaffected(toSwitchOn);

        return this.toBuilder()
                .brightness(ImmutableMap.<Coordinate, Integer>builder().putAll(toChange).putAll(unaffected).build())
                .build();
    }

    private Map<Coordinate, Integer> calculateChanges(Set<Coordinate> toSwitchOn, ToIntFunction<Coordinate> mutateFunction) {
        return toSwitchOn.stream()
                .collect(Collectors.toMap(s -> s, s -> Math.max(0, mutateFunction.applyAsInt(s))));
    }

    private Map<Coordinate, Integer> getUnaffected(Set<Coordinate> toChange) {
        return brightness.entrySet().stream()
                .filter(s -> !toChange.contains(s.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
