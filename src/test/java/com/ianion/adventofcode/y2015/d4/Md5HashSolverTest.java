package com.ianion.adventofcode.y2015.d4;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Md5HashSolverTest {

    @ParameterizedTest
    @MethodSource("findLowestSaltProducingHashStartingWithFiveZeroesTestArgs")
    void testFindLowestSaltProducingHashStartingWithFiveZeroes(String input, int expectedOutput) {
        int result = Md5HashSolver.findLowestSaltProducingHashStartingWith(input, "00000");

        assertThat(result).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @MethodSource("findLowestSaltProducingHashStartingWithSixZeroesTestArgs")
    void testFindLowestSaltProducingHashStartingWithSixZeroes(String input, int expectedOutput) {
        int result = Md5HashSolver.findLowestSaltProducingHashStartingWith(input, "000000");

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> findLowestSaltProducingHashStartingWithFiveZeroesTestArgs() {
        return Stream.of(
                Arguments.of("abcdef", 609043),
                Arguments.of("pqrstuv", 1048970),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d4/input.txt"), 282749)
        );
    }

    private static Stream<Arguments> findLowestSaltProducingHashStartingWithSixZeroesTestArgs() {
        return Stream.of(
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d4/input.txt"), 9962624)
        );
    }
}
