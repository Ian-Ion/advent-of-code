package com.ianion.adventofcode.y2015.d3;

import com.ianion.adventofcode.common.Coordinate;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@UtilityClass
public class HouseVisitor {

    private static final Coordinate STARTING_LOCATION = Coordinate.builder().x(0).y(0).build();

    public static int countUniqueHousesVisited(List<Direction> directions) {
        return visitHouses(directions).visited.size();
    }

    public static int countUniqueHousesVisitedByTwoSantas(List<Direction> directions) {
        List<Direction> santasDirections = filterDirections(getEvenNumberSequence(), directions);
        List<Direction> roboSantasDirections = filterDirections(getOddNumberSequence(), directions);

        Set<Coordinate> visitedBySanta = visitHouses(santasDirections).visited;
        Set<Coordinate> visitedByRoboSanta = visitHouses(roboSantasDirections).visited;

        return Stream.concat(visitedBySanta.stream(), visitedByRoboSanta.stream()).collect(toSet())
                .size();
    }

    private IntStream getEvenNumberSequence() {
        return IntStream.iterate(0, i -> i + 2);
    }

    private IntStream getOddNumberSequence() {
        return IntStream.iterate(1, i -> i + 2);
    }

    private static List<Direction> filterDirections(IntStream indexesToFilter, List<Direction> directions) {
        return indexesToFilter
                .takeWhile(i -> i < directions.size())
                .mapToObj(directions::get)
                .toList();
    }

    private static Santa visitHouses(List<Direction> directions) {
        return Stream
                .iterate(
                        Santa.initialize(directions),
                        Santa::hasMoreDirections,
                        Santa::followNextDirection)
                .reduce(Santa.initialize(directions), (first, second) -> second)
                .followNextDirection();
    }

    @Builder(toBuilder = true)
    private record Santa(
            Coordinate currentLocation,
            List<Direction> remainingDirections,
            Set<Coordinate> visited
    ) {

        public static Santa initialize(List<Direction> directions) {
            return Santa.builder()
                    .currentLocation(STARTING_LOCATION)
                    .remainingDirections(directions)
                    .visited(Set.of(STARTING_LOCATION))
                    .build();
        }

        public boolean hasMoreDirections() {
            return !remainingDirections.isEmpty();
        }

        public Santa followNextDirection() {
            HouseVisitor.Direction nextDirection = this.remainingDirections.get(0);
            Coordinate newLocation = this.currentLocation.add(nextDirection.positionDelta);

            return this.toBuilder()
                    .remainingDirections(this.remainingDirections.subList(1, this.remainingDirections.size()))
                    .currentLocation(newLocation)
                    .visited(Stream.concat(visited.stream(), Stream.of(newLocation)).collect(toSet()))
                    .build();
        }
    }

    @RequiredArgsConstructor
    public enum Direction {
        NORTH('^', Coordinate.builder().x(1).y(0).build()),
        SOUTH('v', Coordinate.builder().x(-1).y(0).build()),
        EAST('>', Coordinate.builder().x(0).y(1).build()),
        WEST('<', Coordinate.builder().x(0).y(-1).build());

        final char character;
        final Coordinate positionDelta;
    }
}
