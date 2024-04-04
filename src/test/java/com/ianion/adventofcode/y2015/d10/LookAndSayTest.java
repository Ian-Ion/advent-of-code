package com.ianion.adventofcode.y2015.d10;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LookAndSayTest {

    @ParameterizedTest
    @MethodSource("playTestArgs")
    void testPlay(String startingSequence, int roundsToPlay, int expectedFinalSequenceLength) {
        LookAndSay result = LookAndSay.play(startingSequence, roundsToPlay);

        assertThat(result.sequence()).hasSize(expectedFinalSequenceLength);
    }

    private static Stream<Arguments> playTestArgs() {
        return Stream.of(
                Arguments.of("1", 1, 2),
                Arguments.of("1", 2, 2),
                Arguments.of("1", 3, 4),
                Arguments.of("1", 4, 6),
                Arguments.of("1", 5, 6),
                Arguments.of(FileLoader.readFileAsString("src/test/resources/inputs/y2015/d10/input.txt"), 40, 329356)
        );
    }
}
