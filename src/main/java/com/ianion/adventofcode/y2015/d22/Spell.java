package com.ianion.adventofcode.y2015.d22;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.UnaryOperator;

@Getter
@RequiredArgsConstructor
public enum Spell {
    MAGIC_MISSILE(53, fight -> fight.damageBoss(4)),
    DRAIN(73, fight -> fight.damageBoss(2).healPlayer(2)),
    SHIELD(113, fight -> fight.activateShield(7, 6)),
    POISON(173, fight -> fight.poisonBoss(3, 6)),
    RECHARGE(229, fight -> fight.activateRecharge(101, 5));

    final int cost;
    final UnaryOperator<Fight> effect;
}
