package com.ianion.adventofcode.y2015.d17;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RefridgeratorTest {

    @ParameterizedTest
    @MethodSource("findNumberOfPossibleWaysToStoreExactlyTestArgs")
    void testFindNumberOfPossibleWaysToStoreExactly(
            List<String> input,
            int litresOfEggnogToStore,
            int expectedNumberOfStorageCombinations
    ) {
        Set<Container> containers = parseAsContainers(input);

        int result = Refridgerator.withContainers(containers)
                .findNumberOfPossibleWaysToStoreExactly(litresOfEggnogToStore);

        assertThat(result).isEqualTo(expectedNumberOfStorageCombinations);
    }

    @ParameterizedTest
    @MethodSource("findNumberOfPossibleWaysToStoreExactlyUsingMinimumContainersTestArgs")
    void testFindNumberOfPossibleWaysToStoreExactlyUsingMinimumContainers(
            List<String> input,
            int litresOfEggnogToStore,
            int expectedNumberOfStorageCombinations
    ) {
        Set<Container> containers = parseAsContainers(input);

        int result = Refridgerator.withContainers(containers)
                .findNumberOfPossibleWaysToStoreExactlyUsingMinimumContainers(litresOfEggnogToStore);

        assertThat(result).isEqualTo(expectedNumberOfStorageCombinations);
    }

    private static Stream<Arguments> findNumberOfPossibleWaysToStoreExactlyTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "20", "15", "10", "5", "5"), 25, 4),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d17/input.txt"), 150, 654)
        );
    }

    private static Stream<Arguments> findNumberOfPossibleWaysToStoreExactlyUsingMinimumContainersTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "20", "15", "10", "5", "5"), 25, 3),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d17/input.txt"), 150, 57)
        );
    }

    private static Set<Container> parseAsContainers(List<String> input) {
        return input.stream()
                .map(i -> Container.builder()
                        .id(UUID.randomUUID().toString())
                        .capacityInLitres(Integer.parseInt(i))
                        .build())
                .collect(Collectors.toSet());
    }
}
