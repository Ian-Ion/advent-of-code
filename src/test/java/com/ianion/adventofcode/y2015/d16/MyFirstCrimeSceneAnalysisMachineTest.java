package com.ianion.adventofcode.y2015.d16;

import com.ianion.adventofcode.common.FileLoader;
import lombok.Builder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MyFirstCrimeSceneAnalysisMachineTest {

    private static final Pattern AUNT_SUE_NAME = Pattern.compile("(\\w+ \\d+)");
    private static final Pattern AUNT_SUE_QUALITY = Pattern.compile("(\\w+): (\\d+)");

    @Test
    void testFindMatchingSue() {
        Set<AuntSue.Quality> requiredQualities = Set.of(
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.CHILDREN).amount(3).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.CATS).amount(7).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.SAMOYEDS).amount(2).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.POMERANIANS).amount(3).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.AKITAS).amount(0).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.VIZSLAS).amount(0).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.GOLDFISH).amount(5).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.TREES).amount(3).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.CARS).amount(2).build(),
                AuntSue.Quality.builder().attribute(AuntSue.Attribute.PERFUMES).amount(1).build());

        List<AuntSue> auntSues = parseAsAuntSues(
                FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d16/input.txt"));

        Optional<AuntSue> matchingSue = MyFirstCrimeSceneAnalysisMachine
                .findMatchingSue(auntSues, requiredQualities);

        assertThat(matchingSue).isPresent().hasValueSatisfying(
                sue -> assertThat(sue.name()).isEqualTo("Sue 40"));
    }

    private List<AuntSue> parseAsAuntSues(List<String> input) {
        return input.stream()
                .map(i -> AuntSue.builder()
                        .name(extractName(i))
                        .qualities(extractQualities(i))
                        .build())
                .toList();
    }

    private static String extractName(String input) {
        Matcher nameMatcher = AUNT_SUE_NAME.matcher(input);
        nameMatcher.find();
        return nameMatcher.group();
    }

    private static Set<AuntSue.Quality> extractQualities(String input) {
        Matcher qualityMatcher = AUNT_SUE_QUALITY.matcher(input);
        QualityParser initialParser = QualityParser.initialise(qualityMatcher);

        return Stream
                .iterate(
                        initialParser,
                        QualityParser::isNotDone,
                        QualityParser::next)
                .reduce(initialParser, (first, second) -> second.next())
                .qualities();
    }

    @Builder(toBuilder = true)
    private record QualityParser(
            Matcher matcher,
            Set<AuntSue.Quality> qualities
    ) {

        private static QualityParser initialise(Matcher matcher) {
            return QualityParser.builder().matcher(matcher).qualities(Set.of()).build();
        }

        private boolean isNotDone() {
            return matcher.find();
        }

        private QualityParser next() {
            return this.toBuilder()
                    .qualities(Stream.concat(
                            qualities.stream(),
                            Stream.of(parseQuality())).collect(Collectors.toSet()))
                    .build();
        }

        private AuntSue.Quality parseQuality() {
            return AuntSue.Quality.builder()
                    .attribute(AuntSue.Attribute.valueOf(matcher.group(1).toUpperCase()))
                    .amount(Integer.parseInt(matcher.group(2)))
                    .build();
        }
    }
}
