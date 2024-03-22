package com.ianion.adventofcode.y2015.d2;

import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PresentWrapper {

    public static int calculatePaperRequired(List<Present> presents) {
        return presents.stream()
                .map(Present::calculatePaperToWrap)
                .reduce(0, Integer::sum);
    }

    @Builder
    public record Present(
            int height,
            int width,
            int length
    ) {

        public int calculatePaperToWrap() {
            return getSurfaceArea() + getSmallestSide();
        }

        public int getSurfaceArea() {
            return (2 * length * width) +
                    (2 * width * height) +
                    (2 * height * length);
        }

        public int getSmallestSide() {
            return Math.min(length * width, Math.min(width * height, height * length));
        }
    }

}
