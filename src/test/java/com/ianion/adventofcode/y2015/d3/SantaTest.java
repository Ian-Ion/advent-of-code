package com.ianion.adventofcode.y2015.d3;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SantaTest {

    @ParameterizedTest
    @MethodSource("countUniqueHousesVisitedTestArgs")
    void testCountUniqueHousesVisited(String input, int expectedOutput) {
        List<Direction> directions = parseAsDirectionsList(input);

        int result = Santa.countUniqueHousesVisited(directions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("countUniqueHousesVisitedByTwoSantasTestArgs")
    void testCountUniqueHousesVisitedByTwoSantas(String input, int expectedOutput) {
        List<Direction> directions = parseAsDirectionsList(input);

        int result = Santa.countUniqueHousesVisitedByTwoSantas(directions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> countUniqueHousesVisitedTestArgs() {
        return Stream.of(
                Arguments.of(">", 2),
                Arguments.of("^>v<", 4),
                Arguments.of("^v^v^v^v^v", 2),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d3/input.txt"), 2565)
        );
    }

    private static Stream<Arguments> countUniqueHousesVisitedByTwoSantasTestArgs() {
        return Stream.of(
                Arguments.of("^v", 3),
                Arguments.of("^>v<", 3),
                Arguments.of("^v^v^v^v^v", 11),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d3/input.txt"), 2639)
        );
    }

    private static List<Direction> parseAsDirectionsList(String input) {
        return input.chars()
                .mapToObj(i -> toDirection((char) i))
                .toList();
    }

    public static Direction toDirection(char character) {
        return Arrays.stream(Direction.values()).filter(d -> d.character == character).findFirst()
                .orElseThrow(() -> new RuntimeException("Could not decode Direction from character " + character));
    }
}
