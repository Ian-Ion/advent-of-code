package com.ianion.adventofcode.y2015.d7;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CircuitCalculatorTest {

    private static final Pattern INPUT_GATE = Pattern.compile("(\\d+) -> (\\w+)");
    private static final Pattern PIPE_GATE = Pattern.compile("(\\w+) -> (\\w+)");
    private static final Pattern AND_VALUE_GATE = Pattern.compile("(\\d+) AND (\\w+) -> (\\w+)");
    private static final Pattern AND_WIRE_GATE = Pattern.compile("(\\w+) AND (\\w+) -> (\\w+)");
    private static final Pattern OR_GATE = Pattern.compile("(\\w+) OR (\\w+) -> (\\w+)");
    private static final Pattern LSHIFT_GATE = Pattern.compile("(\\w+) LSHIFT (\\d+) -> (\\w+)");
    private static final Pattern RSHIFT_GATE = Pattern.compile("(\\w+) RSHIFT (\\d+) -> (\\w+)");
    private static final Pattern COMPLEMENT_GATE = Pattern.compile("NOT (\\w+) -> (\\w+)");

    public static final List<String> TEST_INPUT = List.of(
            "123 -> x",
            "456 -> y",
            "x AND y -> d",
            "x OR y -> e",
            "x LSHIFT 2 -> f",
            "y RSHIFT 2 -> g",
            "NOT x -> h",
            "NOT y -> i");

    private static final Map<String, Integer> TEST_EXPECTED_OUTPUT = Map.of(
            "d", 72,
            "e", 507,
            "f", 492,
            "g", 114,
            "h", 65412,
            "i", 65079,
            "x", 123,
            "y", 456
    );

    private static final Wire WIRE_A = Wire.from("a");
    private static final Wire WIRE_B = Wire.from("b");

    @Test
    void testAssembleAndRun() {
        List<LogicGate> gates = parseAsGatesList(TEST_INPUT);

        Circuit result = Circuit.assembleAndRun(gates);

        TEST_EXPECTED_OUTPUT.forEach(
                (key, value) -> assertThat(result.getSignal(Wire.from(key))).isEqualTo(value));
    }

    @Test
    void testAssembleAndRunChallengePart1() {
        List<LogicGate> gates = readChallengeInput();

        Circuit result = Circuit.assembleAndRun(gates);

        assertThat(result.getSignal(WIRE_A)).isEqualTo(956);
    }

    @Test
    void testAssembleAndRunChallengePart2() {
        List<LogicGate> gates = readChallengeInput();

        Circuit firstPass = Circuit
                .assembleAndRun(gates);
        Circuit secondPass = Circuit
                .assembleAndRun(overrideWireBInputWithWireAOutput(gates, firstPass));

        assertThat(secondPass.getSignal(WIRE_A)).isEqualTo(40149);
    }

    private static List<LogicGate> readChallengeInput() {
        List<String> input = FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d7/input.txt");
        return parseAsGatesList(input);
    }

    private static List<LogicGate> parseAsGatesList(List<String> input) {
        return input.stream().map(i -> {
            Matcher andValueMatcher = AND_VALUE_GATE.matcher(i);
            Matcher andMatcher = AND_WIRE_GATE.matcher(i);
            Matcher orMatcher = OR_GATE.matcher(i);
            Matcher lShiftMatcher = LSHIFT_GATE.matcher(i);
            Matcher rShiftMatcher = RSHIFT_GATE.matcher(i);
            Matcher complementMatcher = COMPLEMENT_GATE.matcher(i);
            Matcher inputMatcher = INPUT_GATE.matcher(i);
            Matcher pipeMatcher = PIPE_GATE.matcher(i);

            return andValueMatcher.find()
                    ? createAndValueGate(andValueMatcher)
                    : andMatcher.find()
                    ? createAndGate(andMatcher)
                    : orMatcher.find()
                    ? createOrGate(orMatcher)
                    : lShiftMatcher.find()
                    ? createLShiftGate(lShiftMatcher)
                    : rShiftMatcher.find()
                    ? createRShiftGate(rShiftMatcher)
                    : complementMatcher.find()
                    ? createComplementGate(complementMatcher)
                    : inputMatcher.find()
                    ? createInputSignal(inputMatcher)
                    : pipeMatcher.find()
                    ? createPipeGate(pipeMatcher)
                    : null;

        }).toList();
    }

    private static AndValueGate createAndValueGate(Matcher m) {
        return AndValueGate.builder()
                .amount(Integer.parseInt(m.group(1)))
                .input(Wire.from(m.group(2)))
                .output(Wire.from(m.group(3)))
                .build();
    }

    private static AndWireGate createAndGate(Matcher m) {
        return AndWireGate.builder()
                .input1(Wire.from(m.group(1)))
                .input2(Wire.from(m.group(2)))
                .output(Wire.from(m.group(3)))
                .build();
    }

    private static OrGate createOrGate(Matcher m) {
        return OrGate.builder()
                .input1(Wire.from(m.group(1)))
                .input2(Wire.from(m.group(2)))
                .output(Wire.from(m.group(3)))
                .build();
    }

    private static LeftShiftGate createLShiftGate(Matcher m) {
        return LeftShiftGate.builder()
                .input(Wire.from(m.group(1)))
                .amount(Integer.parseInt(m.group(2)))
                .output(Wire.from(m.group(3)))
                .build();
    }

    private static RightShiftGate createRShiftGate(Matcher m) {
        return RightShiftGate.builder()
                .input(Wire.from(m.group(1)))
                .amount(Integer.parseInt(m.group(2)))
                .output(Wire.from(m.group(3)))
                .build();
    }

    private static ComplementGate createComplementGate(Matcher inputMatcher) {
        return ComplementGate.builder()
                .input(Wire.from(inputMatcher.group(1)))
                .output(Wire.from(inputMatcher.group(2)))
                .build();
    }

    private static InputSignal createInputSignal(Matcher inputMatcher) {
        return InputSignal.builder()
                .amount(Integer.parseInt(inputMatcher.group(1)))
                .output(Wire.from(inputMatcher.group(2)))
                .build();
    }

    private static PipeGate createPipeGate(Matcher inputMatcher) {
        return PipeGate.builder()
                .input(Wire.from(inputMatcher.group(1)))
                .output(Wire.from(inputMatcher.group(2)))
                .build();
    }

    private static List<LogicGate> overrideWireBInputWithWireAOutput(List<LogicGate> gates, Circuit firstPass) {
        return Stream
                .concat(
                        gates.stream().filter(gate -> !gate.getOutput().equals(WIRE_B)),
                        Stream.of(InputSignal.builder().output(WIRE_B).amount(firstPass.getSignal(WIRE_A)).build()))
                .toList();
    }
}
