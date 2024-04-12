package com.ianion.adventofcode.y2015.d3;

import com.ianion.adventofcode.common.Coordinate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Direction {
    NORTH('^', Coordinate.builder().x(1).y(0).build()),
    SOUTH('v', Coordinate.builder().x(-1).y(0).build()),
    EAST('>', Coordinate.builder().x(0).y(1).build()),
    WEST('<', Coordinate.builder().x(0).y(-1).build());

    final char character;
    final Coordinate positionDelta;
}
