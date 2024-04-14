package com.ianion.adventofcode.y2015.d19;

import lombok.Builder;

import java.util.Comparator;

@Builder(toBuilder = true)
public record Replacement(
        String from,
        String to
) implements Comparable<Replacement> {

    private static final Comparator<Replacement> BY_FROM_LENGTH_DESC = Comparator.comparing(r -> r.from.length());

    @Override
    public int compareTo(Replacement other) {
        return BY_FROM_LENGTH_DESC.reversed().compare(this, other);
    }

    public Replacement reverse() {
        return this.toBuilder()
                .from(to)
                .to(from)
                .build();
    }
}
