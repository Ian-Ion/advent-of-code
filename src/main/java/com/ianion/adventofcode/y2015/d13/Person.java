package com.ianion.adventofcode.y2015.d13;

import lombok.Builder;

@Builder
public record Person(
        String name
) {

    @Builder
    public record Relationship(
            Person self,
            Person other
    ) {}
}
