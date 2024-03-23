package com.ianion.adventofcode.y2015.d5;

import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
public class NiceStringFinder {

    private static final List<Character> VOWELS = List.of('a', 'e', 'i', 'o', 'u');
    private static final List<String> ILLEGAL_PATTERNS = List.of("ab", "cd", "pq", "xy");

    private static final List<String> DOUBLE_LETTERS = IntStream.rangeClosed('a', 'z')
            .mapToObj(i -> String.valueOf((char) i) + (char) i)
            .toList();

    public static List<String> findNiceStrings(List<String> strings) {
        return strings.stream()
                .map(StringMetadata::from)
                .filter(m -> !m.containsIllegalPattern() && m.countVowels() >= 3 && m.hasDoubleLetter())
                .map(m -> m.str)
                .toList();
    }

    @Builder
    public record StringMetadata(
            String str,
            List<Character> chars
    ) {

        public static StringMetadata from(String str) {
            return StringMetadata.builder()
                    .str(str)
                    .chars(str.chars().mapToObj(c -> (char) c).toList())
                    .build();
        }

        public int countVowels() {
            return (int) chars.stream().filter(VOWELS::contains).count();
        }

        public boolean hasDoubleLetter() {
            return DOUBLE_LETTERS.stream().anyMatch(str::contains);
        }

        public boolean containsIllegalPattern() {
            return ILLEGAL_PATTERNS.stream().anyMatch(str::contains);
        }
    }
}
