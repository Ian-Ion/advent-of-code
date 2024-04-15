package com.ianion.adventofcode.y2015.d19;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;

@Slf4j
@Builder(toBuilder = true)
public record MoleculeFabricator(
        List<Replacement> reversedReplacements,
        String molecule,
        int numberOfSteps,
        int maxReplacementLength
) {

    private static final Comparator<MoleculeFabricator> NUM_STEPS =
            Comparator.comparing(MoleculeFabricator::numberOfSteps);

    public static MoleculeFabricator fabricate(List<Replacement> replacements, String molecule) {
        return MoleculeFabricator.initialize(replacements, molecule)
                .reverseEngineerUntilSolved();
    }

    private static MoleculeFabricator initialize(List<Replacement> replacements, String molecule) {
        return MoleculeFabricator.builder()
                .reversedReplacements(replacements.stream().map(Replacement::reverse).sorted().toList())
                .molecule(molecule)
                .build();
    }

    private MoleculeFabricator reverseEngineerUntilSolved() {
        return isAnswer() ? this : reverseEngineer();
    }

    private MoleculeFabricator reverseEngineer() {
        return generateReverseEngineeredMolecules().stream()
                .map(this::reverseEngineerForMolecule)
                .filter(MoleculeFabricator::isAnswer)
                .reduce(BinaryOperator.minBy(NUM_STEPS))
                .orElse(this);
    }

    private MoleculeFabricator reverseEngineerForMolecule(String molecule) {
        return this.toBuilder()
                .molecule(molecule)
                .numberOfSteps(numberOfSteps + 1)
                .build()
                .reverseEngineerUntilSolved();
    }

    private Set<String> generateReverseEngineeredMolecules() {
        return reversedReplacements.stream()
                .map(replacement -> MoleculeGenerator.spawnMolecules(replacement, molecule))
                .flatMap(Set::stream)
                .findFirst().map(Set::of).orElse(Set.of());
    }

    private boolean isAnswer() {
        return molecule.equals("e");
    }
}
