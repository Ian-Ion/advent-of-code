package com.ianion.adventofcode.y2015.d19;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Builder(toBuilder = true)
public record MoleculeGenerator(
        Replacement replacement,
        String originalMolecule,
        int currentIndex,
        Set<String> spawnedMolecules
) {

    public static MoleculeGenerator initialize(Replacement replacement, String molecule) {
        return MoleculeGenerator.builder()
                .replacement(replacement)
                .originalMolecule(molecule)
                .currentIndex(0)
                .spawnedMolecules(Set.of())
                .build();
    }

    public MoleculeGenerator next() {
        int index = originalMolecule.indexOf(replacement.from(), currentIndex);
        return index == -1 ? this : spawnNext(index);
    }

    private MoleculeGenerator spawnNext(int index) {
        String prefix = originalMolecule.substring(0, index);
        String suffix = originalMolecule.substring(index + replacement().from().length());

        String spawnedMolecule = prefix + replacement.to() + suffix;

        Set<String> updatedSpawnedMolecules = Stream.concat(
                spawnedMolecules.stream(),
                Stream.of(spawnedMolecule)).collect(Collectors.toSet());

        return this.toBuilder()
                .currentIndex(index + 1)
                .spawnedMolecules(updatedSpawnedMolecules)
                .build();
    }

    public boolean hasNext() {
        return originalMolecule.indexOf(replacement.from(), currentIndex) != -1;
    }
}
