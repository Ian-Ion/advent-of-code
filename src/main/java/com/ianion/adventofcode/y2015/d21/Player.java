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
public record Player(
        int hp,
        Set<Equipment> equipment
) implements Participant {

    private static int PLAYER_HP = 100;

    private static final Comparator<Player> BY_COST = Comparator.comparing(Player::getEquipmentCost);

    public static Player findLowestCostEquipmentToWinAgainst(Boss boss) {
        return Player.initialize()
                .generatePlayersWithAllLegalEquipmentPermutations().stream()
                .filter(player -> Fight.between(player, boss).playerWins())
                .reduce(BinaryOperator.minBy(BY_COST))
                .orElseThrow(() -> new RuntimeException("No equipment combination that can defeat the boss"));
    }

    public static Player findHighestCostEquipmentToLoseAgainst(Boss boss) {
        return Player.initialize()
                .generatePlayersWithAllLegalEquipmentPermutations().stream()
                .filter(player -> !Fight.between(player, boss).playerWins())
                .reduce(BinaryOperator.maxBy(BY_COST))
                .orElseThrow(() -> new RuntimeException("No equipment combination that can defeat the boss"));
    }

    private static Player initialize() {
        return Player.builder()
                .hp(PLAYER_HP)
                .equipment(Set.of())
                .build();
    }

    private List<Player> generatePlayersWithAllLegalEquipmentPermutations() {
        return equipEveryPossibleWeapon()
                .map(Player::equipNothingOrEveryPossibleArmor)
                .flatMap(Stream::distinct)
                .map(Player::equipNothingOrEveryPossibleRing)
                .flatMap(Stream::distinct)
                .map(Player::equipNothingOrEveryPossibleRing)
                .flatMap(Stream::distinct)
                .toList();
    }

    private Stream<Player> equipEveryPossibleWeapon() {
        return Arrays.stream(Weapon.values())
                .map(weapon -> this.toBuilder().equipment(
                        Stream.concat(equipment.stream(), Stream.of(weapon)).collect(Collectors.toSet())).build());
    }

    private Stream<Player> equipNothingOrEveryPossibleArmor() {
        return Stream.concat(
                Stream.of(this),
                Arrays.stream(Armor.values())
                        .map(armor -> this.toBuilder().equipment(
                                Stream.concat(equipment.stream(), Stream.of(armor)).collect(Collectors.toSet())).build()));
    }

    private Stream<Player> equipNothingOrEveryPossibleRing() {
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
    public Participant sufferDamage(int amount) {
        return this.toBuilder().hp(hp - amount).build();
    }

    public int getEquipmentCost() {
        return equipment.stream().reduce(
                0,
                (costAcc, item) -> costAcc + item.getCost(),
                (first, second) -> second);
    }
}
