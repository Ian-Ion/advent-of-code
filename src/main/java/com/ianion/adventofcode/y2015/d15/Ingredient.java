package com.ianion.adventofcode.y2015.d15;

import lombok.Builder;

@Builder
public record Ingredient(
        String name,
        int capacity,
        int durability,
        int flavor,
        int texture,
        int calories
) {

}
