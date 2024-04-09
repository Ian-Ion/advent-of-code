package com.ianion.adventofcode.y2015.d16;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

@Builder
public record MyFirstCrimeSceneAnalysisMachine(
        Set<AuntSue.Quality> requiredQualities
) {

    private static final Map<AuntSue.Attribute, BiPredicate<Integer, Integer>> V2_CUSTOM_BI_PREDICATES = Map.of(
            AuntSue.Attribute.CATS, (required, candidate) -> candidate > required,
            AuntSue.Attribute.TREES, (required, candidate) -> candidate > required,
            AuntSue.Attribute.POMERANIANS, (required, candidate) -> candidate < required,
            AuntSue.Attribute.GOLDFISH, (required, candidate) -> candidate < required);

    public static Optional<AuntSue> findMatchingSueV1(
            List<AuntSue> candidates,
            Set<AuntSue.Quality> requiredQualities
    ) {
        return MyFirstCrimeSceneAnalysisMachine.initialize(requiredQualities)
                .findAV1MatchFrom(candidates);
    }

    public static Optional<AuntSue> findMatchingSueV2(
            List<AuntSue> candidates,
            Set<AuntSue.Quality> requiredQualities
    ) {
        return MyFirstCrimeSceneAnalysisMachine.initialize(requiredQualities)
                .findAV2MatchFrom(candidates);
    }

    public static MyFirstCrimeSceneAnalysisMachine initialize(Set<AuntSue.Quality> requiredQualities) {
        return MyFirstCrimeSceneAnalysisMachine.builder().requiredQualities(requiredQualities).build();
    }

    private Optional<AuntSue> findAV1MatchFrom(List<AuntSue> candidates) {
        return candidates.stream()
                .filter(sue -> requiredQualities.containsAll(sue.qualities()))
                .findFirst();
    }

    private Optional<AuntSue> findAV2MatchFrom(List<AuntSue> candidates) {
        return candidates.stream()
                .filter(this::matchesRequiredQualities)
                .findFirst();
    }

    private boolean matchesRequiredQualities(AuntSue candidate) {
        return requiredQualities.stream()
                .allMatch(requiredQuality -> candidate.qualities().stream()
                        .filter(candidateQuality -> candidateQuality.attribute().equals(requiredQuality.attribute()))
                        .findFirst()
                        .map(candidateQuality -> satisfies(candidateQuality, requiredQuality))
                        .orElse(true));
    }

    private static boolean satisfies(AuntSue.Quality candidate, AuntSue.Quality required) {
        return V2_CUSTOM_BI_PREDICATES.getOrDefault(candidate.attribute(), Integer::equals)
                .test(required.amount(), candidate.amount());
    }
}
