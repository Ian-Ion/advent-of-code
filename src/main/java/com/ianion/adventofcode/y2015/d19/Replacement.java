package com.ianion.adventofcode.y2015.d19;

import lombok.Builder;

@Builder
public record Replacement(
        String from,
        String to
) {
}
