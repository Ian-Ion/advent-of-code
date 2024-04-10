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

    public static final Comparator<Refridgerator> BY_NUMBER_OF_FILLED_CONTAINERS =
            Comparator.comparing(r -> r.filledContainers().size());

    public static int findNumberOfPossibleRefridgeratorsWhichFitExactly(
            Set<Container> containers,
            int litresToStore
    ) {
        return Refridgerator.initialize(containers, litresToStore)
                .generateAllPossibleFilledContainerRefridgerators().size();
    }

    public static int findNumberOfPossibleRefridgeratorsUsingMinContainersWhichFitExactly(
            Set<Container> containers,
            int litresToStore
    ) {
        Set<Refridgerator> possibleRefridgerators = Refridgerator.initialize(containers, litresToStore)
                .generateAllPossibleFilledContainerRefridgerators();

        int minFilledContainersNeeded = possibleRefridgerators.stream()
                .min(BY_NUMBER_OF_FILLED_CONTAINERS)
                .map(r -> r.filledContainers().size())
                .orElse(0);

        return (int) possibleRefridgerators.stream()
                .filter(r -> r.filledContainers().size() == minFilledContainersNeeded)
                .count();
    }

    private static Refridgerator initialize(Set<Container> containers, int litresToStore) {
        return Refridgerator.builder()
                .emptyContainers(containers)
                .filledContainers(Set.of())
                .litresToStore(litresToStore)
                .build();
    }

    private Set<Refridgerator> generateAllPossibleFilledContainerRefridgerators() {
        Set<Container> viableContainers = findAllEmptyContainerWhichCanBeFilled();

        return viableContainers.isEmpty() ? Set.of(this) : viableContainers.stream()
                .map(this::fillNextContainer)
                .map(Refridgerator::generateAllPossibleFilledContainerRefridgerators)
                .flatMap(Set::stream)
                .filter(Refridgerator::hasStoredAllLitres)
                .collect(Collectors.toSet());
    }

    private boolean hasStoredAllLitres() {
        return litresToStore == 0;
    }

    private Refridgerator fillNextContainer(Container emptyContainer) {
        return this.toBuilder()
                .emptyContainers(emptyContainers.stream().filter(c -> !c.equals(emptyContainer)).collect(Collectors.toSet()))
                .filledContainers(Stream.concat(filledContainers.stream(), Stream.of(emptyContainer)).collect(Collectors.toSet()))
                .litresToStore(litresToStore - emptyContainer.capacityInLitres())
                .build();
    }

    private Set<Container> findAllEmptyContainerWhichCanBeFilled() {
        return emptyContainers.stream()
                .filter(container -> container.capacityInLitres() <= litresToStore)
                .collect(Collectors.toSet());
    }
}
