package com.ianion.adventofcode.y2015.d6;

import com.ianion.adventofcode.common.Coordinate;
import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LightSwitcherTest {

    private static final Pattern COORDINATES = Pattern.compile("(\\d)*,(\\d)*");

    @ParameterizedTest
    @MethodSource("countSwitchedOnAfterFollowingTestArgs")
    void testCountSwitchedOnAfterFollowing(List<String> input, int expectedOutput) {
        List<LightSwitcher.SwitchInstruction> instructions = parseAsInstructionsList(input);

        int result = LightSwitcher.countSwitchedOnAfterFollowing(instructions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> countSwitchedOnAfterFollowingTestArgs() {
        return Stream.of(
                Arguments.of(List.of("turn on 0,0 through 999,999"), 1000000),
                Arguments.of(List.of("toggle 0,0 through 999,0"), 1000),
                Arguments.of(List.of("turn off 499,499 through 500,500"), 0),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d6/input.txt"), 543903)
        );
    }

    private List<LightSwitcher.SwitchInstruction> parseAsInstructionsList(List<String> input) {
        return input.stream()
                .map(s -> LightSwitcher.SwitchInstruction.builder()
                        .behavior(parseBehaviour(s))
                        .coordinates(parseCoordinates(s))
                        .build())
                .toList();
    }

    private LightSwitcher.Behavior parseBehaviour(String s) {
        return s.startsWith("turn on")
                ? LightSwitcher.Behavior.SWITCH_ON
                : s.startsWith("turn off")
                ? LightSwitcher.Behavior.SWITCH_OFF
                : LightSwitcher.Behavior.TOGGLE;
    }

    private static Coordinate.CoordinatePair parseCoordinates(String s) {
        List<Coordinate> coordinates = COORDINATES.matcher(s).results()
                .map(match -> s.substring(match.start(), match.end()))
                .map(LightSwitcherTest::parseCoordinate)
                .toList();

        return Coordinate.CoordinatePair.builder()
                .first(coordinates.get(0))
                .second(coordinates.get(1))
                .build();
    }

    private static Coordinate parseCoordinate(String cString) {
        String[] coordinateStrings = cString.split(",");
        return Coordinate.builder()
                .x(Integer.parseInt(coordinateStrings[0]))
                .y(Integer.parseInt(coordinateStrings[1]))
                .build();
    }
}
