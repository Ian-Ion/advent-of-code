package com.ianion.adventofcode.y2015.d19;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Builder
public record RudolphMedicineMachine(
        List<Replacement> replacements
) {

    public static RudolphMedicineMachine initialize(List<Replacement> replacements) {
        return RudolphMedicineMachine.builder()
                .replacements(replacements)
                .build();
    }

    public int calibrate(String molecule) {
        return replacements.stream()
                .map(replacement -> spawnMolecules(replacement, molecule))
                .flatMap(Set::stream)
                .collect(Collectors.toSet())
                .size();
    }

    private Set<String> spawnMolecules(Replacement replacement, String molecule) {
        MoleculeGenerator generator = MoleculeGenerator.initialize(replacement, molecule);

        return Stream
                .iterate(
                        generator,
                        MoleculeGenerator::hasNext,
                        MoleculeGenerator::next)
                .reduce(generator, (first, second) -> second)
                .next()
                .spawnedMolecules();
    }

}
