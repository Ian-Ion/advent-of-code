package com.ianion.adventofcode.y2015.d9;

import lombok.Builder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Route(
        List<Location> path,
        int totalDistance
) {

    public static Route empty() {
        return Route.builder()
                .path(List.of())
                .totalDistance(0)
                .build();
    }

    public Route append(Location location, int distance) {
        return this.toBuilder()
                .path(Stream.concat(path.stream(), Stream.of(location)).toList())
                .totalDistance(totalDistance + distance)
                .build();
    }

    public Optional<Location> getEndOfRoute() {
        return !path.isEmpty()
                ? Optional.of(path.get(path.size() - 1))
                : Optional.empty();
    }
}
