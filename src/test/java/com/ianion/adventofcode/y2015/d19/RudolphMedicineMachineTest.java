package com.ianion.adventofcode.y2015.d19;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RudolphMedicineMachineTest {

    private static final Pattern REPLACEMENT = Pattern.compile("(\\w+) => (\\w+)");

    private static final List<String> TEST_REPLACEMENTS = List.of(
            "H => HO",
            "H => OH",
            "O => HH");

    @ParameterizedTest
    @MethodSource("calibrateTestArgs")
    void testCalibrate(List<String> replacementsString, String molecule, int expectedDistinctMolecules) {
        List<Replacement> replacements = parseAsReplacements(replacementsString);

        int result = RudolphMedicineMachine.initialize(replacements)
                .calibrate(molecule);

        assertThat(result).isEqualTo(expectedDistinctMolecules);
    }

    private List<Replacement> parseAsReplacements(List<String> replacementsString) {
        return replacementsString.stream()
                .map(RudolphMedicineMachineTest::parseAsReplacement)
                .toList();
    }

    private static Replacement parseAsReplacement(String r) {
        Matcher matcher = REPLACEMENT.matcher(r);
        matcher.find();

        return Replacement.builder()
                .from(matcher.group(1))
                .to(matcher.group(2))
                .build();
    }

    private static Stream<Arguments> calibrateTestArgs() {
        return Stream.of(
                Arguments.of(TEST_REPLACEMENTS, "HOH", 4),
                Arguments.of(TEST_REPLACEMENTS, "HOHOHO", 7),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d19/replacements.txt"),
                        FileLoader.readFileAsString("src/test/resources/inputs/y2015/d19/molecule.txt"),
                        535)
        );
    }
}
