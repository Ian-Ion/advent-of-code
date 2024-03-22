package com.ianion.adventofcode.y2015.d1;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ApartmentTraverser {

    public static int calculateFinalFloor(List<Direction> directions) {
        long upDirections = directions.stream().filter(d -> d.equals(Direction.UP)).count();
        long downDirections = directions.stream().filter(d -> d.equals(Direction.DOWN)).count();

        return (int)(upDirections - downDirections);
    }

    public enum Direction {
        UP('('),
        DOWN(')');

        final char character;

        Direction(char character) {
            this.character = character;
        }
    }
}
