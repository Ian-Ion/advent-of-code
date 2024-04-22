package com.ianion.adventofcode.y2015.d21;

import lombok.Builder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Warrior(
        int hp,
        Set<Equipment> equipment
) implements Fighter {

    private static int PLAYER_HP = 100;

    private static final Comparator<Warrior> BY_COST = Comparator.comparing(Warrior::getEquipmentCost);

    public static Warrior findLowestCostEquipmentToWinAgainst(Boss boss) {
        return Warrior.initialize()
                .generatePlayersWithAllLegalEquipmentPermutations().stream()
                .filter(warrior -> Fight.between(warrior, boss).playerWins())
                .reduce(BinaryOperator.minBy(BY_COST))
                .orElseThrow(() -> new RuntimeException("No equipment combination that can defeat the boss"));
    }

    public static Warrior findHighestCostEquipmentToLoseAgainst(Boss boss) {
        return Warrior.initialize()
                .generatePlayersWithAllLegalEquipmentPermutations().stream()
                .filter(warrior -> !Fight.between(warrior, boss).playerWins())
                .reduce(BinaryOperator.maxBy(BY_COST))
                .orElseThrow(() -> new RuntimeException("No equipment combination that can defeat the boss"));
    }

    private static Warrior initialize() {
        return Warrior.builder()
                .hp(PLAYER_HP)
                .equipment(Set.of())
                .build();
    }

    private List<Warrior> generatePlayersWithAllLegalEquipmentPermutations() {
        return equipEveryPossibleWeapon()
                .map(Warrior::equipNothingOrEveryPossibleArmor)
                .flatMap(Stream::distinct)
                .map(Warrior::equipNothingOrEveryPossibleRing)
                .flatMap(Stream::distinct)
                .map(Warrior::equipNothingOrEveryPossibleRing)
                .flatMap(Stream::distinct)
                .toList();
    }

    private Stream<Warrior> equipEveryPossibleWeapon() {
        return Arrays.stream(Weapon.values())
                .map(weapon -> this.toBuilder().equipment(
                        Stream.concat(equipment.stream(), Stream.of(weapon)).collect(Collectors.toSet())).build());
    }

    private Stream<Warrior> equipNothingOrEveryPossibleArmor() {
        return Stream.concat(
                Stream.of(this),
                Arrays.stream(Armor.values())
                        .map(armor -> this.toBuilder().equipment(
                                Stream.concat(equipment.stream(), Stream.of(armor)).collect(Collectors.toSet())).build()));
    }

    private Stream<Warrior> equipNothingOrEveryPossibleRing() {
        return Stream.concat(
                Stream.of(this),
                Arrays.stream(Ring.values())
                        .map(ring -> this.toBuilder().equipment(
                                Stream.concat(equipment.stream(), Stream.of(ring)).collect(Collectors.toSet())).build()));
    }

    @Override
    public int getDamageScore() {
        return equipment.stream().reduce(
                0,
                (damageAcc, item) -> damageAcc + item.getDamage(),
                (first, second) -> second);
    }

    @Override
    public int getArmorScore() {
        return equipment.stream().reduce(
                0,
                (armorAcc, item) -> armorAcc + item.getArmor(),
                (first, second) -> second);
    }

    @Override
    public Warrior deductHp(int amount) {
        return this.toBuilder().hp(hp - amount).build();
    }

    public int getEquipmentCost() {
        return equipment.stream().reduce(
                0,
                (costAcc, item) -> costAcc + item.getCost(),
                (first, second) -> second);
    }
}
