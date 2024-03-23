package com.ianion.adventofcode.y2015.d5;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class NiceStringFinder {

    private static final Pattern VOWELS = Pattern.compile("[aeiou]");
    private static final Pattern REPEATING_LETTER = Pattern.compile("(\\w)\\1+");
    private static final Pattern ILLEGAL_PATTERNS = Pattern.compile("ab|cd|pq|xy");
    private static final Pattern REPEATING_PAIR_NON_OVERLAPPING = Pattern.compile("([a-z][a-z])(.)*\\1");
    private static final Pattern REPEATING_LETTER_WITH_1CHAR_GAP = Pattern.compile("([a-z])(.)\\1");

    public static List<String> findNiceStringsV1(List<String> strings) {
        return strings.stream()
                .filter(s -> !containsIllegalPattern(s) && getNumberOfVowels(s) >= 3 && hasRepeatingLetter(s))
                .toList();
    }

    public static List<String> findNiceStringsV2(List<String> strings) {
        return strings.stream()
                .filter(s -> hasRepeatingLetterWithOneCharacterGap(s) && hasRepeatingNonOverlappingPair(s))
                .toList();
    }

    private static long getNumberOfVowels(String s) {
        return VOWELS.matcher(s).results().count();
    }

    private static boolean hasRepeatingLetter(String s) {
        return REPEATING_LETTER.matcher(s).find();
    }

    private static boolean containsIllegalPattern(String s) {
        return ILLEGAL_PATTERNS.matcher(s).find();
    }

    private static boolean hasRepeatingLetterWithOneCharacterGap(String s) {
        return REPEATING_LETTER_WITH_1CHAR_GAP.matcher(s).find();
    }

    private static boolean hasRepeatingNonOverlappingPair(String s) {
        return REPEATING_PAIR_NON_OVERLAPPING.matcher(s).find();
    }
}
