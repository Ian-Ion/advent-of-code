package com.ianion.adventofcode.y2015.d21;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Armor implements Equipment {
    LEATHER(13, 0, 1),
    CHAINMAIL(31, 0, 2),
    SPLINTMAIL(53, 0, 3),
    BANDEDMAIL(75, 0, 4),
    PLATEMAIL(102, 0, 5);

    final int cost;
    final int damage;
    final int armor;
}
