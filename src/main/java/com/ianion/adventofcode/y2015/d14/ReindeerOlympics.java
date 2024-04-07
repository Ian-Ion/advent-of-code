package com.ianion.adventofcode.y2015.d14;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Builder(toBuilder = true)
public record ReindeerOlympics(
        Set<Reindeer> competitors
) {

    public static ReindeerOlympics initialize(Set<Reindeer> competitors) {
        return ReindeerOlympics.builder().competitors(competitors).build();
    }

    public Race runRace(int raceTimeInSeconds) {
        Race startOfRace = Race.initialize(competitors, raceTimeInSeconds);

        return Stream
                .iterate(
                        startOfRace,
                        Race::isNotOver,
                        Race::raceForOneSecond)
                .reduce(startOfRace, (first, second) -> second);
    }

    @Builder
    public record Reindeer(
            String name,
            int speedInKmS,
            int secondsFlying,
            int secondsResting
    ) {

        private int getFlyRestCycleLength() {
            return secondsFlying + secondsResting;
        }

    }

    @Builder(toBuilder = true)
    public record Race(
            Set<Flyer> flyers,
            int secondsElapsed,
            int secondsRemaining
    ) {

        @Builder(toBuilder = true)
        public record Flyer(
                Reindeer reindeer,
                Integer distanceFlown,
                Integer pointsAwarded
        ) {

            private static final Comparator<Flyer> BY_DISTANCE_FLOWN = Comparator.comparing(Flyer::distanceFlown);

            public static Flyer from(Reindeer reindeer) {
                return Flyer.builder()
                        .reindeer(reindeer)
                        .distanceFlown(0)
                        .pointsAwarded(0)
                        .build();
            }

            public Flyer updateDistanceFlown(int second) {
                int currentCycleSecond = Math.floorMod(second, reindeer.getFlyRestCycleLength());

                int distanceTravelledThisSecond = currentCycleSecond < reindeer.secondsFlying()
                        ? reindeer.speedInKmS() : 0;

                return this.toBuilder().distanceFlown(distanceFlown + distanceTravelledThisSecond).build();
            }

            public Flyer awardPoint() {
                return this.toBuilder().pointsAwarded(pointsAwarded + 1).build();
            }
        }

        public int getWinningDistance() {
            return getLeader().map(Flyer::distanceFlown).orElse(0);
        }

        private static Race initialize(Set<Reindeer> competitors, int raceTimeInSeconds) {
            return Race.builder()
                    .flyers(competitors.stream().map(Flyer::from).collect(Collectors.toSet()))
                    .secondsElapsed(0)
                    .secondsRemaining(raceTimeInSeconds)
                    .build();
        }

        private Race raceForOneSecond() {
            return this.toBuilder()
                    .flyers(updateFlyers(secondsElapsed))
                    .secondsElapsed(secondsElapsed + 1)
                    .secondsRemaining(secondsRemaining - 1)
                    .build();
        }

        private Set<Flyer> updateFlyers(int secondsElapsed) {
            return flyers.stream()
                    .map(flyer -> flyer.updateDistanceFlown(secondsElapsed))
                    .map(this::awardPointIfLeader)
                    .collect(Collectors.toSet());
        }

        private Flyer awardPointIfLeader(Flyer flyer) {
            return getLeader().map(leader -> leader.equals(flyer) ? flyer.awardPoint() : flyer).orElse(flyer);
        }

        private Optional<Flyer> getLeader() {
            return flyers.stream().max(Flyer.BY_DISTANCE_FLOWN);
        }

        private boolean isNotOver() {
            return secondsRemaining >= 0;
        }
    }
}
