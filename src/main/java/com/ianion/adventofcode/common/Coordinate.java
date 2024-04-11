package com.ianion.adventofcode.common;

import lombok.Builder;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public Set<Coordinate> neighbors() {
        return IntStream.rangeClosed(x - 1, x + 1)
                .mapToObj(dX -> IntStream.rangeClosed(y - 1, y + 1)
                        .mapToObj(dY -> toNeighbor(dX, dY))
                        .flatMap(Optional::stream))
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    private Optional<Coordinate> toNeighbor(int dX, int dY) {
        return x == dX && y == dY
                ? Optional.empty()
                : Optional.of(Coordinate.builder().x(dX).y(dY).build());
    }
}
