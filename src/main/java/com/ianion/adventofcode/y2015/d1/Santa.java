package com.ianion.adventofcode.y2015.d1;

import lombok.Builder;

import java.util.List;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Santa(
        List<Direction> remainingDirections,
        int directionsFollowed,
        int currentFloor
) {

    public static int calculateFinalFloor(List<Direction> directions) {
        long upDirections = directions.stream().filter(d -> d.equals(Direction.UP)).count();
        long downDirections = directions.stream().filter(d -> d.equals(Direction.DOWN)).count();

        return (int) (upDirections - downDirections);
    }

    public static int findIndexWhenBasementEnteredForFirstTime(List<Direction> directions) {
        return Stream
                .iterate(
                        Santa.initialize(directions),
                        Santa::isNotFinishedOrEnteredBasement,
                        Santa::followNextDirection)
                .reduce(Santa.initialize(directions), (first, second) -> second)
                .followNextDirection()
                .directionsFollowed;
    }

    private static Santa initialize(List<Direction> directions) {
        return Santa.builder()
                .remainingDirections(directions)
                .directionsFollowed(0)
                .currentFloor(0)
                .build();
    }

    private boolean isNotFinishedOrEnteredBasement() {
        return currentFloor >= 0 && !remainingDirections.isEmpty();
    }

    private Santa followNextDirection() {
        Direction nextDirection = this.remainingDirections.get(0);

        return this.toBuilder()
                .remainingDirections(this.remainingDirections.subList(1, this.remainingDirections.size()))
                .directionsFollowed(this.directionsFollowed + 1)
                .currentFloor(this.currentFloor + nextDirection.floorDelta)
                .build();
    }
}
