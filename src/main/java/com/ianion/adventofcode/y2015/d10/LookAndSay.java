package com.ianion.adventofcode.y2015.d10;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Builder(toBuilder = true)
public record LookAndSay(
        String sequence,
        int roundsRemaining
) {

    private static final Pattern CONSECUTIVE_DIGITS = Pattern.compile("(\\d)\\1*");

    public static LookAndSay play(String startingSequence, int roundsToPlay) {
        return LookAndSay.initialize(startingSequence).playMoreRounds(roundsToPlay);
    }

    private static LookAndSay initialize(String sequence) {
        return LookAndSay.builder()
                .sequence(sequence)
                .build();
    }

    private LookAndSay playMoreRounds(int rounds) {
        return Stream
                .iterate(
                        withNumberOfRoundsToPlay(rounds),
                        LookAndSay::hasRoundsRemaining,
                        LookAndSay::playOneRound)
                .reduce(this, (first, second) -> second);
    }

    private LookAndSay withNumberOfRoundsToPlay(int rounds) {
        return this.toBuilder().roundsRemaining(rounds).build();
    }

    private boolean hasRoundsRemaining() {
        return roundsRemaining >= 0;
    }

    private LookAndSay playOneRound() {
        Matcher currentSequenceMatcher = CONSECUTIVE_DIGITS.matcher(sequence);
        String nextSequence = currentSequenceMatcher.find()
                ? generateNextSequence(currentSequenceMatcher.reset())
                : sequence;

        return this.toBuilder()
                .sequence(nextSequence)
                .roundsRemaining(roundsRemaining - 1)
                .build();
    }

    private String generateNextSequence(Matcher currentSequenceMatcher) {
        return Stream
                .iterate(
                        NextSequenceBuilder.initialize(currentSequenceMatcher),
                        NextSequenceBuilder::isNotFinished,
                        NextSequenceBuilder::parseNextRunOfDigits)
                .reduce((first, second) -> second)
                .map(NextSequenceBuilder::getNextSequence).orElse("");
    }

    @Builder(toBuilder = true)
    private record NextSequenceBuilder(
            Matcher currentSequenceMatcher,
            StringBuilder nextSequence
    ) {

        private static NextSequenceBuilder initialize(Matcher matcher) {
            return NextSequenceBuilder.builder()
                    .currentSequenceMatcher(matcher)
                    .nextSequence(new StringBuilder())
                    .build();
        }

        private boolean isNotFinished() {
            return currentSequenceMatcher.find();
        }

        private NextSequenceBuilder parseNextRunOfDigits() {
            String nextDigit = currentSequenceMatcher.group(0);
            return this.toBuilder()
                    .nextSequence(
                            nextSequence
                                    .append(nextDigit.length())
                                    .append(nextDigit.charAt(0)))
                    .build();
        }

        private String getNextSequence() {
            return nextSequence.toString();
        }
    }
}
