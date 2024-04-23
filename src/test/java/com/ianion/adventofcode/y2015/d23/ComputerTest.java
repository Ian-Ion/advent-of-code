package com.ianion.adventofcode.y2015.d23;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ComputerTest {

    private static final Register REGISTER_A = Register.builder().name("a").build();
    private static final Register REGISTER_B = Register.builder().name("b").build();

    private static final Pattern HALVE = Pattern.compile("hlf (\\w+)");
    private static final Pattern TRIPLE = Pattern.compile("tpl (\\w+)");
    private static final Pattern INCREMENT = Pattern.compile("inc (\\w+)");
    private static final Pattern JUMP = Pattern.compile("jmp ([\\\\+-]\\d+)");
    private static final Pattern JUMP_IF_EVEN = Pattern.compile("jie (\\w+), ([\\\\+-]\\d+)");
    private static final Pattern JUMP_IF_ODD = Pattern.compile("jio (\\w+), ([\\\\+-]\\d+)");

    @ParameterizedTest
    @MethodSource("runProgramTestArgs")
    void testRunProgram(List<String> instructions, int expectedRegisterAValue, int expectedRegisterBValue) {
        Program program = parseAsProgram(instructions);
        Computer result = Computer.runProgram(Set.of(REGISTER_A, REGISTER_B), program);

        assertThat(result.getRegisterValue(REGISTER_A.name())).isEqualTo(expectedRegisterAValue);
        assertThat(result.getRegisterValue(REGISTER_B.name())).isEqualTo(expectedRegisterBValue);
    }

    private static Program parseAsProgram(List<String> input) {
        List<Instruction> instructions = input.stream().map(i -> {
            Matcher halveMatcher = HALVE.matcher(i);
            Matcher tripleMatcher = TRIPLE.matcher(i);
            Matcher incrementMatcher = INCREMENT.matcher(i);
            Matcher jumpMatcher = JUMP.matcher(i);
            Matcher jumpIfEvenMatcher = JUMP_IF_EVEN.matcher(i);
            Matcher jumpIfOddMatcher = JUMP_IF_ODD.matcher(i);

            return halveMatcher.find()
                    ? createHalveInstruction(halveMatcher)
                    : tripleMatcher.find()
                    ? createTripleInstruction(tripleMatcher)
                    : incrementMatcher.find()
                    ? createIncrementInstruction(incrementMatcher)
                    : jumpMatcher.find()
                    ? createJumpInstruction(jumpMatcher)
                    : jumpIfEvenMatcher.find()
                    ? createJumpIfEvenInstruction(jumpIfEvenMatcher)
                    : jumpIfOddMatcher.find()
                    ? createJumpIfOneIstruction(jumpIfOddMatcher)
                    : null;

        }).toList();

        return Program.builder().instructions(instructions).build();
    }

    private static Halve createHalveInstruction(Matcher matcher) {
        return Halve.builder().registerName(matcher.group(1)).build();
    }

    private static Triple createTripleInstruction(Matcher matcher) {
        return Triple.builder().registerName(matcher.group(1)).build();
    }

    private static Increment createIncrementInstruction(Matcher matcher) {
        return Increment.builder().registerName(matcher.group(1)).build();
    }

    private static Jump createJumpInstruction(Matcher matcher) {
        return Jump.builder().offset(Integer.parseInt(matcher.group(1))).build();
    }

    private static JumpIfEven createJumpIfEvenInstruction(Matcher matcher) {
        return JumpIfEven.builder()
                .registerName(matcher.group(1))
                .offset(Integer.parseInt(matcher.group(2))).build();
    }

    private static JumpIfOne createJumpIfOneIstruction(Matcher matcher) {
        return JumpIfOne.builder()
                .registerName(matcher.group(1))
                .offset(Integer.parseInt(matcher.group(2))).build();
    }

    private static Stream<Arguments> runProgramTestArgs() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                "inc a",
                                "jio a, +2",
                                "tpl a",
                                "inc a"),
                        2, 0),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d23/input.txt"),
                        1, 184)
        );
    }
}
