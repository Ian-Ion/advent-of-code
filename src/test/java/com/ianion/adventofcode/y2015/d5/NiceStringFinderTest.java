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
    @MethodSource("findNiceStringsV1TestArgs")
    void testFindNiceStringsV1(List<String> input, int expectedOutput) {
        int result = NiceStringFinder.findNiceStringsV1(input).size();

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("findNiceStringsV2TestArgs")
    void testFindNiceStringsV2(List<String> input, int expectedOutput) {
        int result = NiceStringFinder.findNiceStringsV2(input).size();

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> findNiceStringsV1TestArgs() {
        return Stream.of(
                Arguments.of(List.of("ugknbfddgicrmopn"), 1),
                Arguments.of(List.of("aaa"), 1),
                Arguments.of(List.of("jchzalrnumimnmhp"), 0),
                Arguments.of(List.of("haegwjzuvuyypxyu"), 0),
                Arguments.of(List.of("dvszwmarrgswjxmb"), 0),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d5/input.txt"), 258)
        );
    }

    private static Stream<Arguments> findNiceStringsV2TestArgs() {
        return Stream.of(
                Arguments.of(List.of("qjhvhtzxzqqjkmpb"), 1),
                Arguments.of(List.of("xxyxx"), 1),
                Arguments.of(List.of("uurcxstgmygtbstg"), 0),
                Arguments.of(List.of("ieodomkazucvgmuy"), 0),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d5/input.txt"), 53)
        );
    }
}
