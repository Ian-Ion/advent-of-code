package com.ianion.adventofcode.y2015.d20;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ElfTest {

    @ParameterizedTest
    @MethodSource("calculateNumberOfPresentsDeliveredToHouseV1TestArgs")
    void testCalculateNumberOfPresentsDeliveredToHouseV1(int houseNumber, int expectedNumberOfPresents) {
        int numberOfPresents = Elf.calculateNumberOfPresentsDeliveredToHouseV1(houseNumber);

        assertThat(numberOfPresents).isEqualTo(expectedNumberOfPresents);
    }

    @Test
    void testCalculateLowestHouseNumberToReceiveV1() {
        int numberOfPresents = Integer.parseInt(
                FileLoader.readFileAsString("src/test/resources/inputs/y2015/d20/input.txt"));

        int houseNumber = Elf.calculateLowestHouseNumberToReceiveV1(numberOfPresents);

        assertThat(houseNumber).isEqualTo(831600);
    }

    @Test
    void testCalculateLowestHouseNumberToReceiveV2() {
        int numberOfPresents = Integer.parseInt(
                FileLoader.readFileAsString("src/test/resources/inputs/y2015/d20/input.txt"));

        int houseNumber = Elf.calculateLowestHouseNumberToReceiveV2(numberOfPresents);

        assertThat(houseNumber).isEqualTo(884520);
    }

    private static Stream<Arguments> calculateNumberOfPresentsDeliveredToHouseV1TestArgs() {
        return Stream.of(
                Arguments.of(1, 10),
                Arguments.of(2, 30),
                Arguments.of(3, 40),
                Arguments.of(4, 70),
                Arguments.of(5, 60),
                Arguments.of(6, 120),
                Arguments.of(7, 80),
                Arguments.of(8, 150),
                Arguments.of(9, 130)
        );
    }
}
