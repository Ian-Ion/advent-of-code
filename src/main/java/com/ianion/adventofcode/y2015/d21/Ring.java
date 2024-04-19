package com.ianion.adventofcode.y2015.d21;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Ring implements Equipment {
    DAMAGE_PLUS_1(25, 1, 0),
    DAMAGE_PLUS_2(50, 2, 0),
    DAMAGE_PLUS_3(100, 3, 0),
    DEFENSE_PLUS_1(20, 0, 1),
    DEFENSE_PLUS_2(40, 0, 2),
    DEFENSE_PLUS_3(80, 0, 3);

    final int cost;
    final int damage;
    final int armor;
}
