package com.ianion.adventofcode.y2015.d21;

public interface Participant {

    int hp();

    int getDamageScore();

    int getArmorScore();

    Participant sufferDamage(int amount);

    default int calculateDamageCausedTo(Participant opponent) {
        return Math.max(1, this.getDamageScore() - opponent.getArmorScore());
    }
}
