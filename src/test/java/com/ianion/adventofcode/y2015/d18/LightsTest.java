package com.ianion.adventofcode.y2015.d18;

import com.ianion.adventofcode.common.Coordinate;
import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LightsTest {

    private static final List<String> TEST_INPUT = List.of(
            ".#.#.#",
            "...##.",
            "#....#",
            "..#...",
            "#.#..#",
            "####..");

    @ParameterizedTest
    @MethodSource("animateTestArgs")
    void testAnimate(
            List<String> input,
            int gridWidth,
            int gridHeight,
            int numberOfAnimationFrames,
            int expectedNumberOfLightsSwitchedOn
    ) {
        Set<Coordinate> initiallySwitchedOn = parseAsSwitchedOnCoordinates(input);

        int result = Lights.initialize(initiallySwitchedOn, gridWidth, gridHeight)
                .animate(numberOfAnimationFrames)
                .countSwitchedOn();

        assertThat(result).isEqualTo(expectedNumberOfLightsSwitchedOn);
    }

    @ParameterizedTest
    @MethodSource("animateWithCornerLightsStuckOnTestArgs")
    void testAnimateWithCornerLightsStuckOn(
            List<String> input,
            int gridWidth,
            int gridHeight,
            int numberOfAnimationFrames,
            int expectedNumberOfLightsSwitchedOn
    ) {
        Set<Coordinate> initiallySwitchedOn = parseAsSwitchedOnCoordinates(input);

        int result = Lights.initialize(initiallySwitchedOn, gridWidth, gridHeight)
                .animateWithCornerLightsStuckOn(numberOfAnimationFrames)
                .countSwitchedOn();

        assertThat(result).isEqualTo(expectedNumberOfLightsSwitchedOn);
    }

    private static Stream<Arguments> animateTestArgs() {
        return Stream.of(
                Arguments.of(TEST_INPUT, 6, 6, 1, 11),
                Arguments.of(TEST_INPUT, 6, 6, 2, 8),
                Arguments.of(TEST_INPUT, 6, 6, 3, 4),
                Arguments.of(TEST_INPUT, 6, 6, 4, 4),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d18/input.txt"),
                        100, 100, 100, 1061)
        );
    }

    private static Stream<Arguments> animateWithCornerLightsStuckOnTestArgs() {
        return Stream.of(
                Arguments.of(TEST_INPUT, 6, 6, 1, 18),
                Arguments.of(TEST_INPUT, 6, 6, 2, 18),
                Arguments.of(TEST_INPUT, 6, 6, 3, 18),
                Arguments.of(TEST_INPUT, 6, 6, 4, 14),
                Arguments.of(TEST_INPUT, 6, 6, 5, 17),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d18/input.txt"),
                        100, 100, 100, 1006)
        );
    }

    private static Set<Coordinate> parseAsSwitchedOnCoordinates(List<String> input) {
        return IntStream.range(0, input.size())
                .mapToObj(x -> IntStream.range(0, input.get(x).length())
                        .mapToObj(y -> toCoordinate(input.get(x).charAt(y), x, y))
                        .flatMap(Optional::stream))
                .flatMap(Function.identity())
                .collect(Collectors.toSet());
    }

    private static Optional<Coordinate> toCoordinate(char c, int x, int y) {
        return c == '#'
                ? Optional.of(Coordinate.builder().x(x).y(y).build())
                : Optional.empty();
    }
}
