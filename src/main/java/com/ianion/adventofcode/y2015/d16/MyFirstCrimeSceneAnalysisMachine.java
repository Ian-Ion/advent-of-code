package com.ianion.adventofcode.y2015.d16;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class MyFirstCrimeSceneAnalysisMachine {

    public static Optional<AuntSue> findMatchingSue(
            List<AuntSue> candidateSues,
            Set<AuntSue.Quality> requiredQualities
    ) {
        return candidateSues.stream()
                .filter(sue -> requiredQualities.containsAll(sue.qualities()))
                .findFirst();
    }
}
