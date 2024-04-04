package com.ianion.adventofcode.y2015.d11;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

    @ParameterizedTest
    @MethodSource("isInvalidTestArgs")
    void testIsInvalid(String currentPassword, boolean expectedIsInvalid) {
        boolean isInvalid = Password.of(currentPassword).isInvalid();

        assertThat(isInvalid).isEqualTo(expectedIsInvalid);
    }

    @ParameterizedTest
    @MethodSource("getNextValidPasswordTestArgs")
    void testGetNextValidPassword(String currentPassword, String expectedNextValidPassword) {
        Password nextValidPassword = Password.of(currentPassword).getNextValidPassword();

        assertThat(nextValidPassword.value()).isEqualTo(expectedNextValidPassword);
    }

    private static Stream<Arguments> isInvalidTestArgs() {
        return Stream.of(
                Arguments.of("hijklmmn", true),
                Arguments.of("abbceffg", true),
                Arguments.of("abbcegjk", true),
                Arguments.of("cqjxxyzz", false)
        );
    }

    private static Stream<Arguments> getNextValidPasswordTestArgs() {
        return Stream.of(
                Arguments.of("abcdefgh", "abcdffaa"),
                Arguments.of("ghijklmn", "ghjaabcc"),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d11/input.txt"), "cqjxxyzz")
        );
    }
}
