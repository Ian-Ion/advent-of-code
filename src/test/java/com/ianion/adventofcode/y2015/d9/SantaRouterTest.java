package com.ianion.adventofcode.y2015.d9;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SantaRouterTest {

    private static final Pattern DISTANCE = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");

    @ParameterizedTest
    @MethodSource("findShortestDistanceThatVisitsAllTestArgs")
    void testFindShortestDistanceThatVisitsAll(List<String> input, int expectedOutput) {
        Location.Connections locations = parseAsConnectedLocations(input);

        int result = Santa.findShortestDistanceThatVisitsAll(locations);

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("findLongestDistanceThatVisitsAllTestArgs")
    void testFindLongestDistanceThatVisitsAll(List<String> input, int expectedOutput) {
        Location.Connections locations = parseAsConnectedLocations(input);

        int result = Santa.findLongestDistanceThatVisitsAll(locations);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> findShortestDistanceThatVisitsAllTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "London to Dublin = 464",
                        "London to Belfast = 518",
                        "Dublin to Belfast = 141"
                ), 605),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d9/input.txt"), 141)
        );
    }

    private static Stream<Arguments> findLongestDistanceThatVisitsAllTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "London to Dublin = 464",
                        "London to Belfast = 518",
                        "Dublin to Belfast = 141"
                ), 982),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d9/input.txt"), 141)
        );
    }

    private static Location.Connections parseAsConnectedLocations(List<String> input) {
        Map<Location.Pair, Integer> distancesBetweenLocations = input.stream()
                .map(i -> {
                    Matcher matcher = DISTANCE.matcher(i);

                    return matcher.find()
                            ? Optional.of(parseLocationConnection(matcher))
                            : Optional.<Map.Entry<Location.Pair, Integer>>empty();
                })
                .flatMap(Optional::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Location.Connections.builder()
                .distanceBetweenLocations(distancesBetweenLocations)
                .build();
    }

    private static Map.Entry<Location.Pair, Integer> parseLocationConnection(Matcher m) {
        return new AbstractMap.SimpleEntry<>(
                Location.Pair.from(
                        Location.builder().name(m.group(1)).build(),
                        Location.builder().name(m.group(2)).build()),
                Integer.parseInt(m.group(3)));
    }
}
