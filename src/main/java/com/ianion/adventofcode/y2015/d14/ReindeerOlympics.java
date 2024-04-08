package com.ianion.adventofcode.y2015.d14;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
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
    
}
