package com.ianion.adventofcode.y2015.d6;

import com.ianion.adventofcode.common.Coordinate;
import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LightsTest {

    private static final Pattern COMMAND = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");

    @ParameterizedTest
    @MethodSource("countSwitchedOnAfterFollowingV1InstructionsTestArgs")
    void testCountSwitchedOnAfterFollowingV1Instructions(List<String> input, int expectedOutput) {
        List<SwitchInstruction> instructions = parseAsInstructionsList(input);

        int result = LightsV1.countSwitchedOnAfterFollowingInstructions(instructions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("countSwitchedOnAfterFollowingV2InstructionsTestArgs")
    void testCountSwitchedOnAfterFollowingV2Instructions(List<String> input, int expectedOutput) {
        List<SwitchInstruction> instructions = parseAsInstructionsList(input);

        int result = LightsV2.countSwitchedOnAfterFollowingInstructions(instructions);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> countSwitchedOnAfterFollowingV1InstructionsTestArgs() {
        return Stream.of(
                Arguments.of(List.of("turn on 0,0 through 999,999"), 1000000),
                Arguments.of(List.of("toggle 0,0 through 999,0"), 1000),
                Arguments.of(List.of("turn off 499,499 through 500,500"), 0),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d6/input.txt"), 543903)
        );
    }

    private static Stream<Arguments> countSwitchedOnAfterFollowingV2InstructionsTestArgs() {
        return Stream.of(
                Arguments.of(List.of("turn on 0,0 through 0,0"), 1),
                Arguments.of(List.of("toggle 0,0 through 999,999"), 2000000),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d6/input.txt"), 14687245)
        );
    }

    private static List<SwitchInstruction> parseAsInstructionsList(List<String> input) {
        return input.stream()
                .map(s -> {
                    Matcher m = COMMAND.matcher(s);
                    m.find();
                    return SwitchInstruction.builder()
                            .behavior(parseBehavior(m.group(1)))
                            .start(
                                    Coordinate.builder()
                                            .x(Integer.parseInt(m.group(2)))
                                            .y(Integer.parseInt(m.group(3)))
                                            .build())
                            .end(
                                    Coordinate.builder()
                                            .x(Integer.parseInt(m.group(4)))
                                            .y(Integer.parseInt(m.group(5)))
                                            .build())
                            .build();
                })
                .toList();
    }

    private static Behavior parseBehavior(String s) {
        return s.equals("turn on")
                ? Behavior.SWITCH_ON
                : s.equals("turn off")
                ? Behavior.SWITCH_OFF
                : Behavior.TOGGLE;
    }
}
