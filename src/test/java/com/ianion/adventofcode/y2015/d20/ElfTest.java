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
    @MethodSource("calculateNumberOfPresentsDeliveredToHouseTestArgs")
    void testCalculateNumberOfPresentsDeliveredToHouse(int houseNumber, int expectedNumberOfPresents) {
        int numberOfPresents = Elf.calculateNumberOfPresentsDeliveredToHouse(houseNumber);

        assertThat(numberOfPresents).isEqualTo(expectedNumberOfPresents);
    }

    @Test
    void testCalculateLowestHouseNumberToReceive() {
        int numberOfPresents = Integer.parseInt(
                FileLoader.readFileAsString("src/test/resources/inputs/y2015/d20/input.txt"));

        int houseNumber = Elf.calculateLowestHouseNumberToReceive(numberOfPresents);

        assertThat(houseNumber).isEqualTo(831600);
    }

    private static Stream<Arguments> calculateNumberOfPresentsDeliveredToHouseTestArgs() {
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
