package com.ianion.adventofcode.common;

import lombok.Builder;

@Builder(toBuilder = true)
public record Coordinate(
        int x,
        int y
) {

    public Coordinate add(Coordinate other) {
        return this.toBuilder()
                .x(x + other.x)
                .y(y + other.y)
                .build();
    }

    @Builder(toBuilder = true)
    public record CoordinatePair(
            Coordinate first,
            Coordinate second
    ) {}
}
