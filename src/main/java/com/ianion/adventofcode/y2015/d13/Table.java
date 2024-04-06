package com.ianion.adventofcode.y2015.d13;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Table(
        List<Person> seats
) {

    public static Table empty() {
        return Table.builder().seats(List.of()).build();
    }

    public int calculateHappinessScore(Map<Person.Relationship, Integer> happinessCoefficients) {
        return IntStream.range(0, seats.size())
                .map(seat -> calculateHappinessScoreForPersonInSeat(seat, happinessCoefficients))
                .reduce(0, Integer::sum);
    }

    private int calculateHappinessScoreForPersonInSeat(int seat, Map<Person.Relationship, Integer> happinessCoefficients) {
        Person person = getPersonInSeat(seat);
        Person leftNeighbour = getLeftSidedNeighbourOfPersonInSeat(seat);
        Person rightNeighbour = getRightSidedNeighbourOfPersonInSeat(seat);

        return happinessCoefficients.get(relationshipBetween(person, leftNeighbour))
                + happinessCoefficients.get(relationshipBetween(person, rightNeighbour));
    }

    private static Person.Relationship relationshipBetween(Person person, Person leftNeighbour) {
        return Person.Relationship.builder().self(person).other(leftNeighbour).build();
    }

    private Person getPersonInSeat(int seat) {
        return seats.get(seat);
    }

    private Person getLeftSidedNeighbourOfPersonInSeat(int seat) {
        return seat == 0
                ? seats.get(seats.size() - 1)
                : seats.get(seat - 1);
    }

    private Person getRightSidedNeighbourOfPersonInSeat(int seat) {
        return seat == seats.size() - 1
                ? seats.get(0)
                : seats.get(seat + 1);
    }

    public Table seat(Person person) {
        return this.toBuilder()
                .seats(Stream.concat(seats.stream(), Stream.of(person)).toList())
                .build();
    }
}
