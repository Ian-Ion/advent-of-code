package com.ianion.adventofcode.y2015.d1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Direction {
    UP('(', 1),
    DOWN(')', -1);

    final char character;
    final int floorDelta;
}
