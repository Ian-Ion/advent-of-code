package com.ianion.adventofcode.y2015.d9;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SantaRouterTest {

    private static final Pattern DISTANCE = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");

    @ParameterizedTest
    @MethodSource("findShortestDistanceThatVisitsAllTestArgs")
    void testFindShortestDistanceThatVisitsAll(List<String> input, int expectedOutput) {
        SantaRouter.Places places = parceAsPlaces(input);

        int result = SantaRouter.findShortestDistanceThatVisitsAll(places);

        assertThat(result).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> findShortestDistanceThatVisitsAllTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "London to Dublin = 464",
                        "London to Belfast = 518",
                        "Dublin to Belfast = 141"
                ), 605),
                Arguments.of(FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d9/input.txt"), 0)
        );
    }

    private static SantaRouter.Places parceAsPlaces(List<String> input) {
        Set<SantaRouter.LocationConnection> locationConnections = input.stream()
                .map(i -> {
                    Matcher matcher = DISTANCE.matcher(i);

                    return matcher.find()
                            ? Optional.of(parseLocationConnection(matcher))
                            : Optional.<SantaRouter.LocationConnection>empty();
                })
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return SantaRouter.Places.builder()
                .locationsConnections(locationConnections)
                .build();
    }

    private static SantaRouter.LocationConnection parseLocationConnection(Matcher m) {
        return SantaRouter.LocationConnection.builder()
                .locations(
                        SantaRouter.LocationPair.builder()
                                .first(SantaRouter.Location.builder().name(m.group(1)).build())
                                .second(SantaRouter.Location.builder().name(m.group(2)).build())
                                .build())
                .distance(Integer.parseInt(m.group(3)))
                .build();
    }
}
