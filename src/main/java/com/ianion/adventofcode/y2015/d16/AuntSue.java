package com.ianion.adventofcode.y2015.d16;

import lombok.Builder;

import java.util.Set;

@Builder
public record AuntSue(
        String name,
        Set<Quality> qualities
) {

    @Builder
    public record Quality(
            Attribute attribute,
            int amount
    ) {}

    public enum Attribute {
        CHILDREN,
        CATS,
        SAMOYEDS,
        POMERANIANS,
        AKITAS,
        VIZSLAS,
        GOLDFISH,
        TREES,
        CARS,
        PERFUMES
    }
}
