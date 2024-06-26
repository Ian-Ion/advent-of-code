package com.ianion.adventofcode.y2015.d2;

import lombok.Builder;

import java.util.List;

@Builder
public record Present(
        int height,
        int width,
        int length
) {

    public static int calculatePaperRequired(List<Present> presents) {
        return presents.stream()
                .map(Present::calculatePaperRequired)
                .reduce(0, Integer::sum);
    }

    public static int calculateRibbonRequired(List<Present> presents) {
        return presents.stream()
                .map(Present::calculateRibbonRequired)
                .reduce(0, Integer::sum);
    }

    private int calculatePaperRequired() {
        return getSurfaceArea() + getSmallestSide();
    }

    private int calculateRibbonRequired() {
        return getSmallestPerimeterOfAnyFace() + calculateVolume();
    }

    private int getSurfaceArea() {
        return (2 * length * width) +
                (2 * width * height) +
                (2 * height * length);
    }

    private int getSmallestSide() {
        return Math.min(length * width, Math.min(width * height, height * length));
    }

    private int getSmallestPerimeterOfAnyFace() {
        return ((length + width + height) - findLongestEdge()) * 2;
    }

    private int calculateVolume() {
        return length * width * height;
    }

    private int findLongestEdge() {
        return Math.max(length, (Math.max(width, height)));
    }
}

