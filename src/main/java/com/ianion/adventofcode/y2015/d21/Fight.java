package com.ianion.adventofcode.y2015.d21;

import lombok.Builder;

import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Fight(
        Competitor turn,
        Warrior player,
        Boss boss
) {

    public static Fight between(Warrior player, Boss boss) {
        return Fight.builder().turn(Competitor.PLAYER).player(player).boss(boss).build();
    }

    public boolean playerWins() {
        Fight result = Stream.iterate(this, Fight::doFightersStillHaveHp, Fight::nextRound)
                .reduce(this, (first, second) -> second)
                .nextRound();

        return result.getWinner() instanceof Warrior;
    }

    private Fighter getWinner() {
        return player.hp() > 0 ? player : boss;
    }

    private boolean doFightersStillHaveHp() {
        return player.hp() > 0 && boss.hp() > 0;
    }

    private Fight nextRound() {
        return turn.equals(Competitor.PLAYER)
                ? playerAttacksBoss()
                : bossAttacksPlayer();
    }

    private Fight playerAttacksBoss() {
        int damageAmount = player.calculateDamageCausedTo(boss);
        return this.toBuilder()
                .boss(boss.deductHp(damageAmount))
                .turn(Competitor.BOSS)
                .build();
    }

    private Fight bossAttacksPlayer() {
        int damageAmount = boss.calculateDamageCausedTo(player);
        return this.toBuilder()
                .player(player.deductHp(damageAmount))
                .turn(Competitor.PLAYER)
                .build();
    }
}
