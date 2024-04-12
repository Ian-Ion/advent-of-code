package com.ianion.adventofcode.y2015.d17;

import lombok.Builder;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Refridgerator(
        Set<Container> emptyContainers,
        Set<Container> filledContainers,
        int litresToStore
) {

    private static final Comparator<Refridgerator> BY_NUMBER_OF_FILLED_CONTAINERS =
            Comparator.comparing(r -> r.filledContainers().size());

    public static Refridgerator withContainers(Set<Container> containers) {
        return Refridgerator.builder()
                .emptyContainers(containers)
                .filledContainers(Set.of())
                .build();
    }

    public int findNumberOfPossibleWaysToStoreExactly(int litresToStore) {
        return prepareToStore(litresToStore)
                .generateAllPossibleRefridgeratorsAfterStoring().size();
    }

    public int findNumberOfPossibleWaysToStoreExactlyUsingMinimumContainers(int litresToStore) {
        Set<Refridgerator> possibleRefridgerators = prepareToStore(litresToStore)
                .generateAllPossibleRefridgeratorsAfterStoring();

        int minNumberOfFilledContainers = getMinNumberOfFilledContainers(possibleRefridgerators);

        return countNumberOfRefridgeratorsWithExactly(possibleRefridgerators, minNumberOfFilledContainers);
    }

    private static Integer getMinNumberOfFilledContainers(Set<Refridgerator> possibleRefridgerators) {
        return possibleRefridgerators.stream()
                .min(BY_NUMBER_OF_FILLED_CONTAINERS)
                .map(r -> r.filledContainers().size())
                .orElse(0);
    }

    private static int countNumberOfRefridgeratorsWithExactly(
            Set<Refridgerator> possibleRefridgerators,
            int numberOfFilledContainers
    ) {
        return (int) possibleRefridgerators.stream()
                .filter(r -> r.filledContainers().size() == numberOfFilledContainers)
                .count();
    }

    private Refridgerator prepareToStore(int litresToStore) {
        return this.toBuilder()
                .litresToStore(litresToStore)
                .build();
    }

    private Set<Refridgerator> generateAllPossibleRefridgeratorsAfterStoring() {
        Set<Container> viableContainers = findAllEmptyContainerWhichCanBeFilled();

        return viableContainers.isEmpty() ? Set.of(this) : viableContainers.stream()
                .map(this::fillNextContainer)
                .map(Refridgerator::generateAllPossibleRefridgeratorsAfterStoring)
                .flatMap(Set::stream)
                .filter(Refridgerator::hasStoredAllLitres)
                .collect(Collectors.toSet());
    }

    private boolean hasStoredAllLitres() {
        return litresToStore == 0;
    }

    private Refridgerator fillNextContainer(Container container) {
        return this.toBuilder()
                .emptyContainers(removeFromEmptyContainers(container))
                .filledContainers(addToFilledContainers(container))
                .litresToStore(litresToStore - container.capacityInLitres())
                .build();
    }

    private Set<Container> removeFromEmptyContainers(Container container) {
        return emptyContainers.stream()
                .filter(c -> !c.equals(container))
                .collect(Collectors.toSet());
    }

    private Set<Container> addToFilledContainers(Container container) {
        return Stream.concat(filledContainers.stream(), Stream.of(container))
                .collect(Collectors.toSet());
    }

    private Set<Container> findAllEmptyContainerWhichCanBeFilled() {
        return emptyContainers.stream()
                .filter(container -> container.capacityInLitres() <= litresToStore)
                .collect(Collectors.toSet());
    }
}
