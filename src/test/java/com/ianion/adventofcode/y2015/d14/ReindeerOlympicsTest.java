package com.ianion.adventofcode.y2015.d14;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReindeerOlympicsTest {

    private static final Pattern COMPETITOR = Pattern.compile(
            "(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");

    @ParameterizedTest
    @MethodSource("getWinningDistanceArgs")
    void testGetWinningDistance(List<String> input, int raceTimeInSeconds, int expectedWinningDistance) {
        Set<Reindeer> competitors = parseAsReindeers(input);

        int distanceTravelledByLeader = ReindeerOlympics.initialize(competitors)
                .runRace(raceTimeInSeconds)
                .getWinningDistance();

        assertThat(distanceTravelledByLeader).isEqualTo(expectedWinningDistance);
    }

    @ParameterizedTest
    @MethodSource("getWinningPointsTestArgs")
    void testGetWinningPoints(List<String> input, int raceTimeInSeconds, int expectedWinningPoints) {
        Set<Reindeer> competitors = parseAsReindeers(input);

        int winnerPoints = ReindeerOlympics.initialize(competitors)
                .runRace(raceTimeInSeconds)
                .getWinningPoints();

        assertThat(winnerPoints).isEqualTo(expectedWinningPoints);
    }

    private static Stream<Arguments> getWinningDistanceArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                        "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."), 1000, 1120),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d14/input.txt"), 2503, 2660)
        );
    }

    private static Stream<Arguments> getWinningPointsTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                        "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."), 1000, 689),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d14/input.txt"), 2503, 1256)
        );
    }

    private static Set<Reindeer> parseAsReindeers(List<String> input) {
        return input.stream()
                .map(ReindeerOlympicsTest::parseAsReindeer)
                .collect(Collectors.toSet());
    }

    private static Reindeer parseAsReindeer(String input) {
        Matcher matcher = COMPETITOR.matcher(input);
        matcher.find();
        return Reindeer.builder()
                .name(matcher.group(1))
                .speedInKmS(Integer.parseInt(matcher.group(2)))
                .secondsFlying(Integer.parseInt(matcher.group(3)))
                .secondsResting(Integer.parseInt(matcher.group(4)))
                .build();
    }
}
