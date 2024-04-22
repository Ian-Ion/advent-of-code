package com.ianion.adventofcode.y2015.d22;

import lombok.Builder;

@Builder(toBuilder = true)
public record Shield(
        int armor,
        int duration
) {
}
