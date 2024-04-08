package com.ianion.adventofcode.y2015.d14;

import lombok.Builder;

@Builder(toBuilder = true)
public record Racer(
        Reindeer reindeer,
        Integer distanceFlown,
        Integer pointsAwarded
) {

    public static Racer from(Reindeer reindeer) {
        return Racer.builder()
                .reindeer(reindeer)
                .distanceFlown(0)
                .pointsAwarded(0)
                .build();
    }

    public Racer updateDistanceFlownIfFlyingAt(int second) {
        int currentCycleSecond = Math.floorMod(second, reindeer.getFlyRestCycleLength());

        int distanceTravelledThisSecond = currentCycleSecond < reindeer.secondsFlying()
                ? reindeer.speedInKmS() : 0;

        return this.toBuilder().distanceFlown(distanceFlown + distanceTravelledThisSecond).build();
    }

    public Racer awardPoint() {
        return this.toBuilder().pointsAwarded(pointsAwarded + 1).build();
    }
}
