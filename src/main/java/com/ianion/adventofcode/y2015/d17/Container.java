package com.ianion.adventofcode.y2015.d17;

import lombok.Builder;

@Builder
public record Container(
        String id,
        int capacityInLitres
) {
}
