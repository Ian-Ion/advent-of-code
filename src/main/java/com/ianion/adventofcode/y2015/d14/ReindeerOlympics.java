package com.ianion.adventofcode.y2015.d14;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

        private int calculateDistanceFlownDuringSecond(int second) {
            int flyRestCycleLength = secondsFlying + secondsResting;
            int currentCycleSecond = Math.floorMod(second, flyRestCycleLength);

            return currentCycleSecond < secondsFlying ? speedInKmS : 0;
        }
    }

    @Builder(toBuilder = true)
    public record Race(
            Map<Reindeer, Integer> distanceTravelled,
            int secondsElapsed,
            int secondsRemaining
    ) {

        public int getWinningDistance() {
            return distanceTravelled.values().stream().reduce(0, Integer::max);
        }

        private static Race initialize(Set<Reindeer> competitors, int raceTimeInSeconds) {
            return Race.builder()
                    .distanceTravelled(competitors.stream().collect(Collectors.toMap(Function.identity(), i -> 0)))
                    .secondsElapsed(0)
                    .secondsRemaining(raceTimeInSeconds)
                    .build();
        }

        private Race raceForOneSecond() {
            return this.toBuilder()
                    .distanceTravelled(updateDistanceTravelled(secondsElapsed))
                    .secondsElapsed(secondsElapsed + 1)
                    .secondsRemaining(secondsRemaining - 1)
                    .build();
        }

        private Map<Reindeer, Integer> updateDistanceTravelled(int secondsElapsed) {
            return distanceTravelled.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            i -> i.getValue() + i.getKey().calculateDistanceFlownDuringSecond(secondsElapsed)));
        }

        private boolean isNotOver() {
            return secondsRemaining >= 0;
        }
    }
}
