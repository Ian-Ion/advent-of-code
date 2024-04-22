package com.ianion.adventofcode.y2015.d21;

import lombok.Builder;

@Builder(toBuilder = true)
public record Boss(
        int hp,
        int damage,
        int armor,
        int poisonDuration
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
}
