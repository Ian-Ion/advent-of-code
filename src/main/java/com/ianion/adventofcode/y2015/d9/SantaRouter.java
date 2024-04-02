package com.ianion.adventofcode.y2015.d9;

import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class SantaRouter {

    public static int findShortestDistanceThatVisitsAll(Places places) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Builder
    public record Places(
            Set<LocationConnection> locationsConnections
    ) {

    }

    @Builder
    public record Location(
            String name
    ) {}

    @Builder
    public record LocationPair(
            Location first,
            Location second
    ) {}

    @Builder
    public record LocationConnection(
            LocationPair locations,
            Integer distance
    ) {}
}
