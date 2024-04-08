package com.ianion.adventofcode.y2015.d14;

import lombok.Builder;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record Race(
        Set<Racer> racers,
        int secondsElapsed,
        int secondsRemaining
) {

    public static final Comparator<Racer> BY_DISTANCE_FLOWN = Comparator.comparing(Racer::distanceFlown);
    public static final Comparator<Racer> BY_POINTS_AWARDED = Comparator.comparing(Racer::pointsAwarded);

    public int getWinningDistance() {
        return getRaceLeaders().stream().findAny().map(Racer::distanceFlown).orElse(0);
    }

    public int getWinningPoints() {
        return getRacerWithMostPoints().map(Racer::pointsAwarded).orElse(0);
    }

    public static Race initialize(Set<Reindeer> competitors, int raceTimeInSeconds) {
        return Race.builder()
                .racers(competitors.stream().map(Racer::from).collect(Collectors.toSet()))
                .secondsElapsed(0)
                .secondsRemaining(raceTimeInSeconds)
                .build();
    }

    public Race raceForOneSecond() {
        return advanceRacers()
                .awardPoints()
                .updateSeconds();
    }

    private Race updateSeconds() {
        return this.toBuilder()
                .secondsElapsed(secondsElapsed + 1)
                .secondsRemaining(secondsRemaining - 1)
                .build();
    }

    private Race advanceRacers() {
        return this.toBuilder()
                .racers(advanceRacersWhoAreFlying())
                .build();
    }

    private Race awardPoints() {
        return this.toBuilder()
                .racers(rewardPointsToLeaders())
                .build();
    }

    public boolean isNotOver() {
        return secondsRemaining >= 0;
    }

    private Set<Racer> advanceRacersWhoAreFlying() {
        return racers.stream()
                .map(racer -> racer.updateDistanceFlownIfFlyingAt(secondsElapsed))
                .collect(Collectors.toSet());
    }

    private Set<Racer> rewardPointsToLeaders() {
        return racers.stream()
                .map(this::awardPointIfLeader)
                .collect(Collectors.toSet());
    }

    private Racer awardPointIfLeader(Racer racer) {
        return getRaceLeaders().stream()
                .map(Racer::reindeer).collect(Collectors.toSet())
                .contains(racer.reindeer()) ? racer.awardPoint() : racer;
    }

    private Optional<Racer> getRacerWithMostPoints() {
        return racers.stream().max(BY_POINTS_AWARDED);
    }

    private Set<Racer> getRaceLeaders() {
        return racers.stream().max(BY_DISTANCE_FLOWN)
                .map(Racer::distanceFlown)
                .map(this::getAllRacersWhoHaveFlown)
                .orElse(Set.of());
    }

    private Set<Racer> getAllRacersWhoHaveFlown(Integer leadingDistance) {
        return racers.stream()
                .filter(racer -> racer.distanceFlown().equals(leadingDistance))
                .collect(Collectors.toSet());
    }
}
