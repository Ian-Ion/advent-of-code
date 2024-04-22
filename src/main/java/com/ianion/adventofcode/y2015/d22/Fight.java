package com.ianion.adventofcode.y2015.d22;

import lombok.Builder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

@Builder(toBuilder = true)
public record Fight(
        Competitor turn,
        Wizard player,
        Boss boss,
        Competitor winner
) {

    private static final Comparator<Fight> BY_MANA_SPENT = Comparator.comparing(Fight::getTotalManaSpent);
    private static final int MAX_CASTS_BEFORE_GIVING_UP = 10;

    public int getTotalManaSpent() {
        return player.getTotalManaSpent();
    }

    public static Fight between(Wizard player, Boss boss) {
        return Fight.builder().turn(Competitor.PLAYER).player(player).boss(boss).build();
    }

    public Fight applyEffectsAndPlayRemainingRoundsOptimallyIfFightNotOver() {
        return isOver()
                ? this
                : applyEffects().playRemainingRoundsOptimallyIfFightNotOver();
    }

    private Fight playRemainingRoundsOptimallyIfFightNotOver() {
        return isOver()
                ? this
                : playRemainingRoundsOptimally();
    }

    private Fight playRemainingRoundsOptimally() {
        return turn.equals(Competitor.BOSS)
                ? castNextSpellOptimallyAfterPlayingRound(List.of(attackPlayer()))
                : castNextSpellOptimallyAfterPlayingRound(generateVariantsFromLegalSpells());
    }

    private Fight castNextSpellOptimallyAfterPlayingRound(List<Fight> variants) {
        return variants.stream()
                .map(Fight::applyEffectsAndPlayRemainingRoundsOptimallyIfFightNotOver)
                .filter(Fight::playerWon)
                .reduce(BinaryOperator.minBy(BY_MANA_SPENT))
                .orElse(this);
    }

    private boolean isOver() {
        return winner != null;
    }

    private boolean playerWon() {
        return winner != null && winner.equals(Competitor.PLAYER);
    }

    private Fight applyEffects() {
        return decreaseShieldTimer()
                .rechargeMana()
                .poisonBoss();
    }

    private Fight decreaseShieldTimer() {
        return this.toBuilder().player(player.decreaseShield()).build();
    }

    private Fight rechargeMana() {
        return this.toBuilder().player(player.rechargeMana()).build();
    }

    private Fight poisonBoss() {
        return this.toBuilder()
                .boss(boss.applyPoison())
                .build();
    }

    private List<Fight> generateVariantsFromLegalSpells() {
        List<Spell> legalSpells = calculateLegalSpells();

        return legalSpells.isEmpty()
                ? List.of(this.toBuilder().winner(Competitor.BOSS).build())
                : legalSpells.stream().map(this::cast).toList();
    }

    private List<Spell> calculateLegalSpells() {
        if (player.spellsCast().size() > MAX_CASTS_BEFORE_GIVING_UP) {
            return List.of();
        }
        return Arrays.stream(Spell.values()).filter(this::canCast)
                .filter(spell -> !spell.equals(Spell.POISON) || !boss.isPoisoned())
                .filter(spell -> !spell.equals(Spell.RECHARGE) || !player.isRecharging())
                .filter(spell -> !spell.equals(Spell.SHIELD) || !player.hasShield())
                .toList();
    }

    private boolean canCast(Spell spell) {
        return player.canAffordToCast(spell);
    }

    private Fight cast(Spell spell) {
        Fight afterSpell = spell.getEffect().apply(this);
        Wizard updatedPlayer = afterSpell.player.cast(spell);

        return afterSpell.toBuilder()
                .player(updatedPlayer)
                .turn(Competitor.BOSS)
                .build();
    }

    private Fight attackPlayer() {
        int damageAmount = boss.calculateDamageCausedTo(player);
        Wizard updatedPlayer = player.deductHp(damageAmount);

        return this.toBuilder()
                .player(updatedPlayer)
                .turn(Competitor.PLAYER)
                .winner(updatedPlayer.hp() <= 0 ? Competitor.BOSS : null)
                .build();
    }

    public Fight damageBoss(int amount) {
        Boss updatedBoss = boss.deductHp(amount);
        return this.toBuilder()
                .boss(updatedBoss)
                .winner(updatedBoss.hp() <= 0 ? Competitor.PLAYER : null)
                .build();
    }

    public Fight healPlayer(int amount) {
        return this.toBuilder()
                .player(player.deductHp(-amount))
                .build();
    }

    public Fight activateShield(int armor, int numberOfTurns) {
        return this.toBuilder()
                .player(player.activate(
                        Shield.builder()
                                .armor(armor)
                                .duration(numberOfTurns)
                                .build()))
                .build();
    }

    public Fight poisonBoss(int damagePerTurn, int numberOfTurns) {
        return this.toBuilder()
                .boss(boss.poison(
                        Poison.builder()
                                .damage(damagePerTurn)
                                .duration(numberOfTurns).build()))
                .build();
    }

    public Fight activateRecharge(int manaPerTurn, int numberOfTurns) {
        return this.toBuilder()
                .player(player.activate(
                        Recharge.builder()
                                .manaPerTurn(manaPerTurn)
                                .duration(numberOfTurns)
                                .build()))
                .build();
    }
}
