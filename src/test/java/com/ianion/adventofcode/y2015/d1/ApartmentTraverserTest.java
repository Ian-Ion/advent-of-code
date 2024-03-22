package com.ianion.adventofcode.y2015.d1;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ApartmentTraverserTest {

    @ParameterizedTest
    @MethodSource("calculateFinalFloorTestArgs")
    void testCalculateFinalFloor(String input, int expectedOutput) {
        List<ApartmentTraverser.Direction> directions = parseAsDirectionsList(input);

        int result = ApartmentTraverser.calculateFinalFloor(directions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("findIndexWhenBasementEnteredForFirstTimeTestArgs")
    void testFindIndexWhenBasementEnteredForFirstTime(String input, int expectedOutput) {
        List<ApartmentTraverser.Direction> directions = parseAsDirectionsList(input);

        int result = ApartmentTraverser.findIndexWhenBasementEnteredForFirstTime(directions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> calculateFinalFloorTestArgs() {
        return Stream.of(
                Arguments.of("(())", 0),
                Arguments.of("()()", 0),
                Arguments.of("(((", 3),
                Arguments.of("(()(()(", 3),
                Arguments.of("))(((((", 3),
                Arguments.of("())", -1),
                Arguments.of("))(", -1),
                Arguments.of(")))", -3),
                Arguments.of(")())())", -3),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d1/input.txt"), 138)
        );
    }

    private static Stream<Arguments> findIndexWhenBasementEnteredForFirstTimeTestArgs() {
        return Stream.of(
                Arguments.of(")", 1),
                Arguments.of("()())", 5),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d1/input.txt"), 1771)
        );
    }

    private static List<ApartmentTraverser.Direction> parseAsDirectionsList(String input) {
        return input.chars()
                .mapToObj(i -> toDirection((char) i))
                .toList();
    }

    public static ApartmentTraverser.Direction toDirection(char character) {
        return Arrays.stream(ApartmentTraverser.Direction.values()).filter(d -> d.character == character).findFirst()
                .orElseThrow(() -> new RuntimeException("Could not decode Direction from character " + character));
    }
}
