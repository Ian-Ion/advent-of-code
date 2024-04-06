package com.ianion.adventofcode.y2015.d13;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SeatingPlanTest {

    private static final Pattern POSIIVE_COEFFICIENT =
            Pattern.compile("(\\w+) would gain (\\d+) happiness units by sitting next to (\\w+).");
    private static final Pattern NEGATIVE_COEFFICIENT =
            Pattern.compile("(\\w+) would lose (\\d+) happiness units by sitting next to (\\w+).");

    @ParameterizedTest
    @MethodSource("generateOptimalPlanTestArgs")
    void testGenerateOptimalPlan(List<String> input, int expectedHappinessScore) {
        Map<Person.Relationship, Integer> coefficients = parseAsHappinessCoefficients(input);

        int score = SeatingPlan.generateOptimalPlan(coefficients).calculateHappiness();

        assertThat(score).isEqualTo(expectedHappinessScore);
    }

    private Map<Person.Relationship, Integer> parseAsHappinessCoefficients(List<String> input) {
        return input.stream()
                .map(this::parseAsHappinessCoefficient)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Person.Relationship, Integer> parseAsHappinessCoefficient(String input) {
        return POSIIVE_COEFFICIENT.matcher(input).find()
                ? buildPositiveCoefficient(input)
                : buildNegativeCoefficient(input);
    }

    private Map.Entry<Person.Relationship, Integer> buildPositiveCoefficient(String input) {
        Matcher m = POSIIVE_COEFFICIENT.matcher(input);
        m.find();
        return new AbstractMap.SimpleEntry<>(
                Person.Relationship.builder()
                        .self(Person.builder().name(m.group(1)).build())
                        .other(Person.builder().name(m.group(3)).build())
                        .build(),
                Integer.parseInt(m.group(2)));
    }

    private Map.Entry<Person.Relationship, Integer> buildNegativeCoefficient(String input) {
        Matcher m = NEGATIVE_COEFFICIENT.matcher(input);
        m.find();
        return new AbstractMap.SimpleEntry<>(
                Person.Relationship.builder()
                        .self(Person.builder().name(m.group(1)).build())
                        .other(Person.builder().name(m.group(3)).build())
                        .build(),
                -Integer.parseInt(m.group(2)));
    }

    private static Stream<Arguments> generateOptimalPlanTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "Alice would gain 54 happiness units by sitting next to Bob.",
                        "Alice would lose 79 happiness units by sitting next to Carol.",
                        "Alice would lose 2 happiness units by sitting next to David.",
                        "Bob would gain 83 happiness units by sitting next to Alice.",
                        "Bob would lose 7 happiness units by sitting next to Carol.",
                        "Bob would lose 63 happiness units by sitting next to David.",
                        "Carol would lose 62 happiness units by sitting next to Alice.",
                        "Carol would gain 60 happiness units by sitting next to Bob.",
                        "Carol would gain 55 happiness units by sitting next to David.",
                        "David would gain 46 happiness units by sitting next to Alice.",
                        "David would lose 7 happiness units by sitting next to Bob.",
                        "David would gain 41 happiness units by sitting next to Carol."), 330),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d13/input.txt"), 733)
        );
    }
}
