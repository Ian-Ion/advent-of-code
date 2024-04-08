package com.ianion.adventofcode.y2015.d14;

import lombok.Builder;

@Builder
public record Reindeer(
        String name,
        int speedInKmS,
        int secondsFlying,
        int secondsResting
) {

    public int getFlyRestCycleLength() {
        return secondsFlying + secondsResting;
    }

}
