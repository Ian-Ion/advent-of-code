package com.ianion.adventofcode.y2015.d11;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Builder(toBuilder = true)
public record Password(
        String value
) {

    private static final Pattern CONTAINS_I_O_L = Pattern.compile(".*[iol].*");
    private static final Pattern REPEATING_LETTER = Pattern.compile("(\\w)\\1+");

    private static final List<Pattern> STRAIGHTS_OF_3_CHARS = IntStream
            .rangeClosed('a', 'x')
            .mapToObj(c -> Pattern.compile(".*" + (char) c + (char) (c + 1) + (char) (c + 2) + ".*"))
            .toList();

    public static Password of(String value) {
        return Password.builder().value(value).build();
    }

    public Password getNextValidPassword() {
        return Stream
                .iterate(this, Password::isInvalid, Password::increment)
                .reduce(this, (first, second) -> second.increment());
    }

    public boolean isInvalid() {
        return containsIllegalCharacter()
                || doesNotContainStraightOf3Chars()
                || doesNotHaveTwoDifferentPairsOfLetters();
    }

    private boolean containsIllegalCharacter() {
        return CONTAINS_I_O_L.matcher(value).matches();
    }

    private boolean doesNotContainStraightOf3Chars() {
        return STRAIGHTS_OF_3_CHARS.stream().noneMatch(m -> m.matcher(value).matches());
    }

    private boolean doesNotHaveTwoDifferentPairsOfLetters() {
        Matcher matcher = REPEATING_LETTER.matcher(value);
        return !matcher.find() || matcher.reset().results().toList().size() < 2;
    }

    private Password increment() {
        int charToChange = findIndexOfLastNonZ();
        char changedChar = (char) (value.charAt(charToChange) + 1);

        String unchangedPrefix = value.substring(0, charToChange);

        String rolledSuffix = value.chars()
                .skip(charToChange + 1L)
                .mapToObj(c -> "a")
                .collect(Collectors.joining());

        return this.toBuilder()
                .value(unchangedPrefix + changedChar + rolledSuffix)
                .build();
    }

    private int findIndexOfLastNonZ() {
        return IntStream.range(0, value.length())
                .filter(i -> value.charAt(i) != 'z')
                .reduce(0, (first, last) -> last);
    }
}
