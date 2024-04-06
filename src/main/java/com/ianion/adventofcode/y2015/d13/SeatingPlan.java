package com.ianion.adventofcode.y2015.d13;

import lombok.Builder;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record SeatingPlan(
        Map<Person.Relationship, Integer> happinessCoefficients,
        Set<Person> unseatedPersons,
        Table table
) {

    private static final Comparator<SeatingPlan> TABLE_HAPPINESS =
            Comparator.comparing(SeatingPlan::calculateHappiness);

    public static SeatingPlan generateOptimalPlan(Map<Person.Relationship, Integer> happinessCoefficients) {
        return SeatingPlan.initialize(happinessCoefficients).optimize();
    }

    public int calculateHappiness() {
        return table.calculateHappinessScore(happinessCoefficients);
    }

    private static SeatingPlan initialize(Map<Person.Relationship, Integer> happinessCoefficients) {
        return SeatingPlan.builder()
                .happinessCoefficients(happinessCoefficients)
                .unseatedPersons(getAllPersons(happinessCoefficients))
                .table(Table.empty()).build();
    }

    private static Set<Person> getAllPersons(Map<Person.Relationship, Integer> coefficients) {
        return coefficients.keySet().stream()
                .map(Person.Relationship::self)
                .collect(Collectors.toSet());
    }

    private SeatingPlan optimize() {
        return isFinished() ? this : unseatedPersons.stream()
                .map(this::seat)
                .map(SeatingPlan::optimize)
                .filter(SeatingPlan::isFinished)
                .reduce(BinaryOperator.maxBy(TABLE_HAPPINESS))
                .orElse(this);
    }

    private SeatingPlan seat(Person person) {
        return this.toBuilder()
                .unseatedPersons(removeFromUnseatedPersons(person))
                .table(seatAtTable(person))
                .build();
    }

    private Set<Person> removeFromUnseatedPersons(Person person) {
        return unseatedPersons.stream().filter(p -> !p.equals(person)).collect(Collectors.toSet());
    }

    private Table seatAtTable(Person person) {
        return table.seat(person);
    }

    private boolean isFinished() {
        return unseatedPersons.isEmpty();
    }

}
