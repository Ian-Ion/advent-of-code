package com.ianion.adventofcode.y2015.d9;

import lombok.Builder;

import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record Santa(
        Map<Location.Pair, Integer> distanceBetweenLocations,
        Set<Location> unvisited,
        Route route
) {

    private static final BinaryOperator<Santa> SANTA_WITH_SMALLEST_DISTANCE_TRAVELLED =
            (a, b) -> a.route.totalDistance() < b.route.totalDistance() ? a : b;

    private static final BinaryOperator<Santa> SANTA_WITH_LONGEST_DISTANCE_TRAVELLED =
            (a, b) -> a.route.totalDistance() > b.route.totalDistance() ? a : b;

    public static int findShortestDistanceThatVisitsAll(Location.Connections connectedLocations) {
        return Santa.initialize(connectedLocations)
                .visitUnvisitedInShortestPossibleDistance()
                .getTotalDistanceTravelled();
    }

    public static int findLongestDistanceThatVisitsAll(Location.Connections connectedLocations) {
        return Santa.initialize(connectedLocations)
                .visitUnvisitedInLongestPossibleDistance()
                .getTotalDistanceTravelled();
    }

    public static Santa initialize(Location.Connections connectedLocations) {
        return Santa.builder()
                .route(Route.empty())
                .distanceBetweenLocations(connectedLocations.distanceBetweenLocations())
                .unvisited(connectedLocations.getLocations())
                .build();
    }

    public Santa visitUnvisitedInShortestPossibleDistance() {
        return isFinished() ? this : unvisited.stream()
                .map(this::visit)
                .map(Santa::visitUnvisitedInShortestPossibleDistance)
                .filter(Santa::isFinished)
                .reduce(SANTA_WITH_SMALLEST_DISTANCE_TRAVELLED)
                .orElse(this);
    }

    public Santa visitUnvisitedInLongestPossibleDistance() {
        return isFinished() ? this : unvisited.stream()
                .map(this::visit)
                .map(Santa::visitUnvisitedInLongestPossibleDistance)
                .filter(Santa::isFinished)
                .reduce(SANTA_WITH_LONGEST_DISTANCE_TRAVELLED)
                .orElse(this);
    }

    private boolean isFinished() {
        return unvisited.isEmpty();
    }

    private Santa visit(Location nextLocation) {
        Integer distanceFromCurrent = route.getEndOfRoute()
                .map(currentLocation -> distanceBetweenLocations.get(
                        Location.Pair.from(currentLocation, nextLocation)))
                .orElse(0);

        return this.toBuilder()
                .unvisited(unvisited.stream().filter(u -> !u.equals(nextLocation)).collect(Collectors.toSet()))
                .route(route.append(nextLocation, distanceFromCurrent))
                .build();
    }

    public int getTotalDistanceTravelled() {
        return route.totalDistance();
    }
}
