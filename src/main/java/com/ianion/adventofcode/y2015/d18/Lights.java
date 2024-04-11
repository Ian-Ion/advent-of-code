package com.ianion.adventofcode.y2015.d18;

import com.ianion.adventofcode.common.Coordinate;
import lombok.Builder;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Lights(
        Set<Coordinate> switchedOn,
        Set<Coordinate> stuckOn,
        int gridWidth,
        int gridHeight,
        int framesToAnimate
) {

    public static Lights initialize(Set<Coordinate> switchedOn, int gridWidth, int gridHeight) {
        return Lights.builder()
                .switchedOn(switchedOn)
                .gridWidth(gridWidth)
                .gridHeight(gridHeight)
                .build();
    }

    public Lights animate(int frames) {
        Lights startOfAnimation = this.toBuilder()
                .stuckOn(Set.of())
                .framesToAnimate(frames)
                .build();

        return Stream
                .iterate(
                        startOfAnimation,
                        Lights::hasMoreToAnimate,
                        Lights::animate)
                .reduce(startOfAnimation, (first, second) -> second.animate());
    }

    public Lights animateWithCornerLightsStuckOn(int frames) {
        Set<Coordinate> cornerLights = calculateCornerLights();

        Lights startOfAnimation = this.toBuilder()
                .switchedOn(Stream.concat(switchedOn.stream(), cornerLights.stream()).collect(Collectors.toSet()))
                .stuckOn(cornerLights)
                .framesToAnimate(frames)
                .build();

        return Stream
                .iterate(
                        startOfAnimation,
                        Lights::hasMoreToAnimate,
                        Lights::animate)
                .reduce(startOfAnimation, (first, second) -> second.animate());
    }

    private Set<Coordinate> calculateCornerLights() {
        return Set.of(
                Coordinate.builder().x(0).y(0).build(),
                Coordinate.builder().x(gridWidth - 1).y(0).build(),
                Coordinate.builder().x(0).y(gridHeight - 1).build(),
                Coordinate.builder().x(gridWidth - 1).y(gridHeight - 1).build()
        );
    }

    private boolean hasMoreToAnimate() {
        return framesToAnimate() > 0;
    }

    private Lights animate() {
        return this.toBuilder()
                .switchedOn(workOutWhichLightsAreSwitchedOnAfterAnimating())
                .framesToAnimate(framesToAnimate - 1)
                .build();
    }

    private Set<Coordinate> workOutWhichLightsAreSwitchedOnAfterAnimating() {
        return generateAllPossibleCoordinates().stream()
                .filter(this::isSwitchedOnAfterAnimating)
                .collect(Collectors.toSet());
    }

    private Set<Coordinate> generateAllPossibleCoordinates() {
        return IntStream.range(0, gridWidth)
                .mapToObj(x -> IntStream.range(0, gridHeight)
                        .mapToObj(y -> Coordinate.builder().x(x).y(y).build()))
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    private boolean isSwitchedOnAfterAnimating(Coordinate coordinate) {
        boolean isStuckOn = stuckOn.contains(coordinate);

        boolean isAlreadySwitchedOn = switchedOn.contains(coordinate);

        long numberOfNeighboursOn = coordinate.neighbors().stream()
                .filter(switchedOn::contains)
                .count();

        return isStuckOn
                || numberOfNeighboursOn == 3
                || (isAlreadySwitchedOn && numberOfNeighboursOn == 2);
    }

    public int countSwitchedOn() {
        return switchedOn.size();
    }
}
