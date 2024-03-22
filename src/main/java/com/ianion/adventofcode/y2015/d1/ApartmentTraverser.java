package com.ianion.adventofcode.y2015.d1;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class ApartmentTraverser {

    public static int calculateFinalFloor(List<Direction> directions) {
        long upDirections = directions.stream().filter(d -> d.equals(Direction.UP)).count();
        long downDirections = directions.stream().filter(d -> d.equals(Direction.DOWN)).count();

        return (int) (upDirections - downDirections);
    }

    public static int findIndexWhenBasementEnteredForFirstTime(List<Direction> directions) {
        return Stream
                .iterate(
                        TraverserState.begin(directions),
                        TraverserState::isNotFinishedOrEnteredBasement,
                        TraverserState::followNextDirection)
                .reduce(TraverserState.begin(directions), (first, second) -> second)
                .followNextDirection()
                .directionsFollowed;
    }

    @RequiredArgsConstructor
    public enum Direction {
        UP('(', 1),
        DOWN(')', -1);

        final char character;
        final int floorDelta;
    }

    @Builder(toBuilder = true)
    private record TraverserState(
            List<Direction> remainingDirections,
            int directionsFollowed,
            int currentFloor
    ) {

        public static TraverserState begin(List<Direction> directions) {
            return TraverserState.builder()
                    .remainingDirections(directions)
                    .directionsFollowed(0)
                    .currentFloor(0)
                    .build();
        }

        public boolean isNotFinishedOrEnteredBasement() {
            return currentFloor >= 0 && !remainingDirections.isEmpty();
        }

        public TraverserState followNextDirection() {
            Direction nextDirection = this.remainingDirections.get(0);

            return this.toBuilder()
                    .remainingDirections(this.remainingDirections.subList(1, this.remainingDirections.size()))
                    .directionsFollowed(this.directionsFollowed + 1)
                    .currentFloor(this.currentFloor + nextDirection.floorDelta)
                    .build();
        }
    }
}
