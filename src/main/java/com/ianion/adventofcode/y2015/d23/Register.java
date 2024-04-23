package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;

@Builder(toBuilder = true)
public record Register(
        String name,
        int value
) {

    public Register halve() {
        return this.toBuilder().value(value / 2).build();
    }

    public Register triple() {
        return this.toBuilder().value(value * 3).build();
    }

    public Register increment() {
        return this.toBuilder().value(value + 1).build();
    }
}
