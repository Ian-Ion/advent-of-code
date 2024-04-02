package com.ianion.adventofcode.y2015.d8;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Builder
public record SantasList(
        List<ListItem> listItems
) {

    private static final int COST_OF_WRAPPING_STRING_IN_QUOTES = 2;

    private static final Pattern BACKSLASH = Pattern.compile("\\\\");
    private static final Pattern QUOTE = Pattern.compile("\"");

    private static final Pattern ESCAPED_HEX = Pattern.compile("\\\\x[0-9a-f]{2}");
    private static final Pattern ESCAPED_BACKSLASH = Pattern.compile("\\\\{2}");
    private static final Pattern ESCAPED_QUOTE_NOT_PRECEDED_BY_BACKSLASH = Pattern.compile("[^\\\\]\\\\\"");
    private static final Pattern ESCAPED_QUOTE_PRECEDED_BY_ESCAPED_QUOTE = Pattern.compile("\\\\\"\\\\\"");
    private static final Pattern ESCAPED_QUOTE_PRECEDED_BY_ESCAPED_BACKSLASH = Pattern.compile("\\\\\\\\\\\\\"");
    private static final Pattern STRING_WRAPPING_QUOTES = Pattern.compile("\"(.*)\"");

    public int countExtraCharactersUsedToEncode() {
        return listItems.stream()
                .map(ListItem::countExtraCharactersUsedToEncode)
                .reduce(0, Integer::sum);
    }

    public int countExtraCharactersRequiredToEncode() {
        return listItems.stream()
                .map(ListItem::countExtraCharactersRequiredToEncode)
                .reduce(0, Integer::sum);
    }

    @Builder
    public record ListItem(
            String text
    ) {

        public int countExtraCharactersUsedToEncode() {
            return costOfEscapedHexChars()
                    + costOfEscapedBackslashChars()
                    + costOfEscapedQuoteChars()
                    + costOfWrappingQuoteChars();
        }

        public int countExtraCharactersRequiredToEncode() {
            return COST_OF_WRAPPING_STRING_IN_QUOTES
                    + costOfEscapingExistingBackslashChars()
                    + costOfEscapingExistingQuoteChars();
        }

        private int costOfEscapedBackslashChars() {
            return costOf(ESCAPED_BACKSLASH, 1);
        }

        private int costOfEscapedQuoteChars() {
            return costOf(ESCAPED_QUOTE_NOT_PRECEDED_BY_BACKSLASH, 1)
                    + costOf(ESCAPED_QUOTE_PRECEDED_BY_ESCAPED_QUOTE, 1)
                    + costOf(ESCAPED_QUOTE_PRECEDED_BY_ESCAPED_BACKSLASH, 1);
        }

        private int costOfEscapedHexChars() {
            return costOf(ESCAPED_HEX, 3);
        }

        private int costOfWrappingQuoteChars() {
            return costOf(STRING_WRAPPING_QUOTES, 2);
        }

        private int costOfEscapingExistingBackslashChars() {
            return costOf(BACKSLASH, 1);
        }

        private int costOfEscapingExistingQuoteChars() {
            return costOf(QUOTE, 1);
        }

        private int costOf(Pattern pattern, int costPerEscape) {
            Matcher matcher = pattern.matcher(text);
            return matcher.matches() || matcher.find()
                    ? (int) matcher.reset().results().count() * costPerEscape
                    : 0;
        }
    }

}


