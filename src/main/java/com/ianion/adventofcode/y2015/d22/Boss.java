package com.ianion.adventofcode.y2015.d22;

import lombok.Builder;

@Builder(toBuilder = true)
public record Boss(
        int hp,
        int damage,
        int armor,
        Poison poison
) implements Fighter {

    @Override
    public int getDamageScore() {
        return damage;
    }

    @Override
    public int getArmorScore() {
        return armor;
    }

    @Override
    public Boss deductHp(int amount) {
        return this.toBuilder().hp(hp - amount).build();
    }

    public Boss poison(Poison poison) {
        return this.toBuilder().poison(poison).build();
    }

    public Boss applyPoison() {
        int poisonDuration = poison == null ? 0 : poison.duration() - 1;
        return this.toBuilder()
                .hp(poison == null ? hp : hp - poison.damage())
                .poison(poisonDuration == 0 ? null : poison.toBuilder().duration(poisonDuration).build())
                .build();
    }

    public boolean isPoisoned() {
        return poison != null;
    }
}
