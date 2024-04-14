package com.ianion.adventofcode.y2015.d19;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;

@Slf4j
@Builder(toBuilder = true)
public record MoleculeFabricatorV2(
        List<Replacement> replacements,
        String molecule,
        int numberOfSteps,
        int maxReplacementLength
) {

    private static final Comparator<MoleculeFabricatorV2> NUM_STEPS =
            Comparator.comparing(MoleculeFabricatorV2::numberOfSteps);

    public static MoleculeFabricatorV2 fabricate(List<Replacement> replacements, String molecule) {
        return MoleculeFabricatorV2.initialize(replacements, molecule)
                .fabricateUntilSolved();
    }

    private static MoleculeFabricatorV2 initialize(List<Replacement> replacements, String molecule) {
        return MoleculeFabricatorV2.builder()
                .replacements(replacements.stream().map(Replacement::reverse).sorted().toList())
                .molecule(molecule)
                .build();
    }

    private MoleculeFabricatorV2 fabricateUntilSolved() {
        return isAnswer() ? this : fabricate();
    }

    private MoleculeFabricatorV2 fabricate() {
        return generateReverseEngineeredMolecules().stream()
                .map(this::fabricateForMolecule)
                .filter(MoleculeFabricatorV2::isAnswer)
                .reduce(BinaryOperator.minBy(NUM_STEPS))
                .orElse(this);
    }

    private MoleculeFabricatorV2 fabricateForMolecule(String molecule) {
        return this.toBuilder()
                .molecule(molecule)
                .numberOfSteps(numberOfSteps + 1)
                .build()
                .fabricateUntilSolved();
    }

    private Set<String> generateReverseEngineeredMolecules() {
        return replacements.stream()
                .map(replacement -> MoleculeGenerator.spawnMolecules(replacement, molecule))
                .flatMap(Set::stream)
                .findFirst().map(Set::of).orElse(Set.of());
    }

    private boolean isAnswer() {
        return molecule.equals("e");
    }
}
