package com.ianion.adventofcode.y2015.d15;

import lombok.Builder;

@Builder
public record Quantity(
        Ingredient ingredient,
        Integer teaspoons
) {
}
