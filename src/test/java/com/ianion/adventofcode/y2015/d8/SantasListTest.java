package com.ianion.adventofcode.y2015.d8;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SantasListTest {

    @ParameterizedTest
    @MethodSource("countExtraCharactersUsedToEncodeTestArgs")
    void testCountExtraCharactersUsedToEncode(List<String> input, int expectedOutput) {
        SantasList santasList = SantasList.builder().listItems(parseAsListItems(input)).build();

        int result = santasList.countExtraCharactersUsedToEncode();

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("countExtraCharactersRequiredToEncodeTestArgs")
    void testCountExtraCharactersRequiredToEncode(List<String> input, int expectedOutput) {
        SantasList santasList = SantasList.builder().listItems(parseAsListItems(input)).build();

        int result = santasList.countExtraCharactersRequiredToEncode();

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> countExtraCharactersUsedToEncodeTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "\"\"",
                        "\"abc\"",
                        "\"aaa\\\"aaa\"",
                        "\"\\x27\""
                ), 12),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d8/input.txt"), 1350)
        );
    }

    private static Stream<Arguments> countExtraCharactersRequiredToEncodeTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "\"\"",
                        "\"abc\"",
                        "\"aaa\\\"aaa\"",
                        "\"\\x27\""
                ), 19),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d8/input.txt"), 2085)
        );
    }

    private static List<SantasList.ListItem> parseAsListItems(List<String> input) {
        return input.stream()
                .map(s -> SantasList.ListItem.builder().text(s).build())
                .toList();
    }
}
