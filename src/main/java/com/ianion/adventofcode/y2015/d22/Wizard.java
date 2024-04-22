package com.ianion.adventofcode.y2015.d22;

import lombok.Builder;

import java.util.List;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Wizard(
        int hp,
        int mana,
        Shield shield,
        Recharge recharge,
        List<Spell> spellsCast
) implements Fighter {

    private static int PLAYER_HP = 50;
    private static int PLAYER_MANA = 500;

    public static Wizard initialize() {
        return Wizard.builder()
                .hp(PLAYER_HP)
                .mana(PLAYER_MANA)
                .spellsCast(List.of())
                .build();
    }

    @Override
    public int getDamageScore() {
        return 0;
    }

    @Override
    public int getArmorScore() {
        return shield == null ? 0 : shield.armor();
    }

    @Override
    public Wizard deductHp(int amount) {
        return this.toBuilder().hp(hp - amount).build();
    }

    public Wizard activate(Shield shield) {
        return this.toBuilder().shield(shield).build();
    }

    public Wizard activate(Recharge recharge) {
        return this.toBuilder().recharge(recharge).build();
    }

    public int getTotalManaSpent() {
        return spellsCast.stream().reduce(
                0,
                (costAcc, spell) -> costAcc + spell.getCost(),
                (first, second) -> second);
    }

    public Wizard decreaseShield() {
        int shieldDuration = shield == null ? 0 : shield.duration() - 1;
        return this.toBuilder()
                .shield(shieldDuration == 0 ? null : shield.toBuilder().duration(shieldDuration).build())
                .build();
    }

    public Wizard rechargeMana() {
        int rechargeDuration = recharge == null ? 0 : recharge.duration() - 1;
        return this.toBuilder()
                .mana(recharge == null ? mana : mana + recharge.manaPerTurn())
                .recharge(rechargeDuration == 0 ? null : recharge.toBuilder().duration(rechargeDuration).build())
                .build();
    }

    public boolean canAffordToCast(Spell spell) {
        return mana >= spell.cost;
    }

    public Wizard cast(Spell spell) {
        return this.toBuilder()
                .mana(mana - spell.cost)
                .spellsCast(Stream.concat(spellsCast.stream(), Stream.of(spell)).toList())
                .build();
    }

    public boolean isRecharging() {
        return recharge != null;
    }

    public boolean hasShield() {
        return shield != null;
    }
}
