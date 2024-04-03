package com.ianion.adventofcode.y2015.d9;

import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public record Location(
        String name
) implements Comparable<Location> {

    private static final Comparator<Location> BY_NAME = Comparator.comparing(l -> l.name);

    @Override
    public int compareTo(Location other) {
        return BY_NAME.compare(this, other);
    }

    @Builder
    public record Pair(
            Location first,
            Location second
    ) {

        public static Pair from(Location first, Location second) {
            List<Location> sortedLocations = Stream.of(first, second).sorted().toList();
            return Pair.builder()
                    .first(sortedLocations.get(0))
                    .second(sortedLocations.get(1))
                    .build();
        }

        public Stream<Location> asStream() {
            return Stream.of(first, second);
        }
    }

    @Builder
    public record Connections(
            Map<Pair, Integer> distanceBetweenLocations
    ) {

        public Set<Location> getLocations() {
            return distanceBetweenLocations.keySet().stream()
                    .flatMap(Location.Pair::asStream)
                    .collect(Collectors.toSet());
        }
    }
}
