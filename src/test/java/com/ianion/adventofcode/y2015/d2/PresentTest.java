package com.ianion.adventofcode.y2015.d2;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PresentTest {

    @ParameterizedTest
    @MethodSource("calculatePaperRequiredTestArgs")
    void testCalculatePaperRequired(List<String> input, int expectedOutput) {
        List<Present> presents = parseAsPresentsList(input);

        int result = Present.calculatePaperRequired(presents);

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("calculateRibbonRequiredTestArgs")
    void testCalculateRibbonRequired(List<String> input, int expectedOutput) {
        List<Present> presents = parseAsPresentsList(input);

        int result = Present.calculateRibbonRequired(presents);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> calculatePaperRequiredTestArgs() {
        return Stream.of(
                Arguments.of(List.of("2x3x4"), 58),
                Arguments.of(List.of("1x1x10"), 43),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d2/input.txt"), 1598415)
        );
    }

    private static Stream<Arguments> calculateRibbonRequiredTestArgs() {
        return Stream.of(
                Arguments.of(List.of("2x3x4"), 34),
                Arguments.of(List.of("1x1x10"), 14),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d2/input.txt"), 3812909)
        );
    }

    private static List<Present> parseAsPresentsList(List<String> input) {
        return input.stream()
                .map(line -> line.split("x"))
                .map(PresentTest::toPresent)
                .toList();
    }

    private static Present toPresent(String[] dimensions) {
        return Present.builder()
                .length(Integer.parseInt(dimensions[0]))
                .width(Integer.parseInt(dimensions[1]))
                .height(Integer.parseInt(dimensions[2]))
                .build();
    }
}
