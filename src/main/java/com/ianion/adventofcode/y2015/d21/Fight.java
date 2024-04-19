package com.ianion.adventofcode.y2015.d21;

import lombok.Builder;

import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Fight(
        Participant attacker,
        Participant defender
) {

    public static Fight between(Player player, Boss boss) {
        return Fight.builder().attacker(player).defender(boss).build();
    }

    public boolean playerWins() {
        Fight result = Stream.iterate(this, Fight::doParticipantsStillHaveHp, Fight::nextRound)
                .reduce(this, (first, second) -> second)
                .nextRound();

        return result.getWinner() instanceof Player;
    }

    private Participant getWinner() {
        return attacker.hp() > 0 ? attacker : defender;
    }

    private boolean doParticipantsStillHaveHp() {
        return attacker.hp() > 0 && defender.hp() > 0;
    }

    private Fight nextRound() {
        int damageAmount = attacker.calculateDamageCausedTo(defender);

        return this.toBuilder()
                .attacker(defender.sufferDamage(damageAmount))
                .defender(attacker)
                .build();
    }
}
