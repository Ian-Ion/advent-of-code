package com.ianion.adventofcode.y2015.d22;

public interface Fighter {

    int hp();

    int getDamageScore();

    int getArmorScore();

    Fighter deductHp(int amount);

    default int calculateDamageCausedTo(Fighter opponent) {
        return Math.max(1, this.getDamageScore() - opponent.getArmorScore());
    }
}
