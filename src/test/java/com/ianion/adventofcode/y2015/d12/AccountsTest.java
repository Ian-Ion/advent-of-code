package com.ianion.adventofcode.y2015.d12;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AccountsTest {

    @ParameterizedTest
    @MethodSource("sumOfAllNumbersTestArgs")
    void testSumOfAllNumbers(String accountsJson, int expectedSumOfAllNumbers) {
        int result = Accounts.fromJson(accountsJson).sumOfAllNumbers();

        assertThat(result).isEqualTo(expectedSumOfAllNumbers);
    }

    private static Stream<Arguments> sumOfAllNumbersTestArgs() {
        return Stream.of(
                Arguments.of("[1,2,3]", 6),
                Arguments.of("{\"a\":2,\"b\":4}", 6),
                Arguments.of("[[[3]]]", 3),
                Arguments.of("{\"a\":{\"b\":4},\"c\":-1}", 3),
                Arguments.of("{\"a\":[-1,1]}", 0),
                Arguments.of("[-1,{\"a\":1}]", 0),
                Arguments.of("[]", 0),
                Arguments.of("{}", 0),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d12/input.txt"), 156366)
        );
    }
}
