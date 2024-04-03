package com.ianion.adventofcode.y2015.d9;

import lombok.Builder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Route(
        List<Location> locations,
        int totalDistance
) {

    public static Route empty() {
        return Route.builder()
                .locations(List.of())
                .totalDistance(0)
                .build();
    }

    public Route visit(Location location, int distance) {
        return this.toBuilder()
                .locations(Stream.concat(locations.stream(), Stream.of(location)).toList())
                .totalDistance(totalDistance + distance)
                .build();
    }

    public Optional<Location> getCurrentLocation() {
        return !locations.isEmpty()
                ? Optional.of(locations.get(locations.size() - 1))
                : Optional.empty();
    }
}
