package com.ianion.adventofcode.y2015.d20;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Slf4j
@Builder
public record Elf(
        int elfNumber
) {

    private static final int PRESENTS_PER_ELF_NUMBER_V1 = 10;
    private static final int PRESENTS_PER_ELF_NUMBER_V2 = 11;
    private static final int NUMBER_OF_HOUSES_PER_ELF_LIMIT = 50;
    private static final int CHUNK_SIZE = 100_000;

    public static int calculateNumberOfPresentsDeliveredToHouseV1(int houseNumber) {
        return calculateNumberOfPresentsPerHouse(PRESENTS_PER_ELF_NUMBER_V1, 1, houseNumber, (house, elf) -> true)
                .get(houseNumber);
    }

    public static int calculateLowestHouseNumberToReceiveV1(int desiredMinimumAmountOfPresents) {
        return IntStream.iterate(1, i -> i + CHUNK_SIZE)
                .mapToObj(firstHouseInChunk -> calculateLowestHouseNumberToReceive(
                        desiredMinimumAmountOfPresents,
                        PRESENTS_PER_ELF_NUMBER_V1,
                        firstHouseInChunk,
                        firstHouseInChunk + CHUNK_SIZE, (house, elf) -> true))
                .dropWhile(Optional::isEmpty)
                .findFirst()
                .orElse(Optional.empty())
                .orElseThrow(() -> new RuntimeException("Could not find a viable house"));
    }

    public static int calculateLowestHouseNumberToReceiveV2(int desiredMinimumAmountOfPresents) {
        return IntStream.iterate(1, i -> i + CHUNK_SIZE)
                .mapToObj(firstHouseInChunk -> calculateLowestHouseNumberToReceive(
                        desiredMinimumAmountOfPresents,
                        PRESENTS_PER_ELF_NUMBER_V2,
                        firstHouseInChunk,
                        firstHouseInChunk + CHUNK_SIZE,
                        (house, elf) -> house <= elf * 50))
                .dropWhile(Optional::isEmpty)
                .findFirst()
                .orElse(Optional.empty())
                .orElseThrow(() -> new RuntimeException("Could not find a viable house"));
    }

    private static Optional<Integer> calculateLowestHouseNumberToReceive(
            int desiredMinimumAmountOfPresents,
            int presentsPerElfNumber,
            int firstHouse,
            int lastHouse,
            BiPredicate<Integer, Integer> filterBasedOnHouseNumberAndElfNumber
    ) {

        return calculateNumberOfPresentsPerHouse(presentsPerElfNumber, firstHouse, lastHouse, filterBasedOnHouseNumberAndElfNumber)
                .entrySet().stream()
                .filter(e -> e.getValue() >= desiredMinimumAmountOfPresents)
                .map(Map.Entry::getKey)
                .sorted()
                .findFirst();
    }

    private static Map<Integer, Integer> calculateNumberOfPresentsPerHouse(
            int presentsPerElfNumber,
            int firstHouse,
            int lastHouse,
            BiPredicate<Integer, Integer> filterBasedOnHouseNumberAndElfNumber
    ) {
        return IntStream.iterate(1, i -> i <= lastHouse, i -> i + 1)
                .mapToObj(Elf::number)
                .map(e -> e.calculatePresentsDeliveredPerHouse(presentsPerElfNumber, firstHouse, lastHouse, filterBasedOnHouseNumberAndElfNumber))
                .flatMap(m -> m.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));
    }

    private static Elf number(int number) {
        return Elf.builder().elfNumber(number).build();
    }

    private Map<Integer, Integer> calculatePresentsDeliveredPerHouse(
            int presentsPerElfNumber,
            int firstHouse,
            int lastHouse,
            BiPredicate<Integer, Integer> filterBasedOnHouseNumberAndElfNumber
    ) {
        return IntStream.iterate(elfNumber, house -> house <= lastHouse, house -> house + elfNumber).boxed()
                .filter(house -> house >= firstHouse && house <= lastHouse)
                .filter(house -> filterBasedOnHouseNumberAndElfNumber.test(house, elfNumber))
                .collect(Collectors.toMap(
                        Function.identity(),
                        i -> elfNumber * presentsPerElfNumber));
    }
}
