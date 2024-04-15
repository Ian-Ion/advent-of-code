package com.ianion.adventofcode.y2015.d20;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Slf4j
@Builder
public record Elf(
        int number
) {

    private static final int PRESENTS_PER_ELF_NUMBER = 10;
    private static final int MAX_HOUSES = 1_000_000;

    public static int calculateNumberOfPresentsDeliveredToHouse(int houseNumber) {
        return calculateNumberOfPresentsPerHouse(houseNumber)
                .get(houseNumber);
    }

    public static int calculateLowestHouseNumberToReceive(int presents) {
        return calculateNumberOfPresentsPerHouse(MAX_HOUSES)
                .entrySet().stream()
                .filter(e -> e.getValue() >= presents)
                .map(Map.Entry::getKey)
                .sorted()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find a viable house"));

    }

    private static Map<Integer, Integer> calculateNumberOfPresentsPerHouse(int maxHouses) {
        return IntStream.rangeClosed(1, maxHouses)
                .mapToObj(Elf::number)
                .map(e -> e.calculatePresentsDeliveredPerHouse(maxHouses))
                .flatMap(m -> m.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));
    }

    private static Elf number(int number) {
        return Elf.builder().number(number).build();
    }

    private Map<Integer, Integer> calculatePresentsDeliveredPerHouse(int houseLimit) {
        return Stream.iterate(number, i -> i <= houseLimit + number, i -> i + number)
                .collect(Collectors.toMap(
                        Function.identity(),
                        i -> number * PRESENTS_PER_ELF_NUMBER));
    }
}
