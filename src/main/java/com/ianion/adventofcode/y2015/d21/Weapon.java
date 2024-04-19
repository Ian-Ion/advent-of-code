package com.ianion.adventofcode.y2015.d21;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Weapon implements Equipment {
    DAGGER(8, 4, 0),
    SHORTSWORD(10, 5, 0),
    WARHAMMER(25, 6, 0),
    LONGSWORD(40, 7, 0),
    GREATAXE(74, 8, 0);

    final int cost;
    final int damage;
    final int armor;
}
