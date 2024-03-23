package com.ianion.adventofcode.y2015.d5;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class NiceStringFinderTest {

    @ParameterizedTest
    @MethodSource("findNiceStringsTestArgs")
    void testFindNiceStrings(List<String> input, int expectedOutput) {
        int result = NiceStringFinder.findNiceStrings(input).size();

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> findNiceStringsTestArgs() {
        return Stream.of(
                Arguments.of(List.of("ugknbfddgicrmopn"), 1),
                Arguments.of(List.of("aaa"), 1),
                Arguments.of(List.of("jchzalrnumimnmhp"), 0),
                Arguments.of(List.of("haegwjzuvuyypxyu"), 0),
                Arguments.of(List.of("dvszwmarrgswjxmb"), 0),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d5/input.txt"), 258)
        );
    }
}
