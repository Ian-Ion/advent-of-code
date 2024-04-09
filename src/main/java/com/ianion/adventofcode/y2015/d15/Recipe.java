package com.ianion.adventofcode.y2015.d15;

import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Recipe(
        List<Ingredient> ingredients,
        List<Quantity> quantities,
        int score
) {

    private static final Comparator<Recipe> HIGHEST_SCORE = Comparator.comparing(Recipe::score);

    public static Recipe findPerfectRecipe(Set<Ingredient> ingredients) {
        return Recipe.using(ingredients).perfect();
    }

    private static Recipe using(Set<Ingredient> ingredients) {
        return Recipe.builder()
                .ingredients(ingredients.stream().toList())
                .quantities(List.of())
                .build();
    }

    private Recipe perfect() {
        return generatePossibleQuantityCombinations().stream()
                .map(this::withQuantities)
                .filter(r -> r.sumQuantities() == 100)
                .map(Recipe::generateScore)
                .reduce(BinaryOperator.maxBy(HIGHEST_SCORE))
                .orElse(this);
    }

    private List<List<Quantity>> generatePossibleQuantityCombinations() {
        Optional<Ingredient> unusedIngredient = ingredients.stream()
                .filter(i -> quantities.stream().noneMatch(q -> q.ingredient().equals(i)))
                .findFirst();

        return unusedIngredient
                .map(nextIngredient -> IntStream
                        .iterate(100 - sumQuantities(), i -> i > 0, i -> i - 1)
                        .mapToObj(nextIngredientQuantity -> {
                            Quantity quantity = Quantity.builder()
                                    .ingredient(nextIngredient)
                                    .teaspoons(nextIngredientQuantity)
                                    .build();

                            List<Quantity> updatedQuantities = Stream.concat(quantities.stream(), Stream.of(quantity)).toList();

                            return withQuantities(updatedQuantities)
                                    .generatePossibleQuantityCombinations();
                        })
                        .flatMap(List::stream)
                        .toList())
                .orElse(List.of(quantities));
    }

    private int sumQuantities() {
        return quantities.stream().map(Quantity::teaspoons).reduce(0, Integer::sum);
    }

    private Recipe withQuantities(List<Quantity> quantities) {
        return this.toBuilder()
                .quantities(quantities)
                .build();
    }

    public Recipe generateScore() {
        return this.toBuilder()
                .score(
                        sumOf(Ingredient::capacity)
                                * sumOf(Ingredient::durability)
                                * sumOf(Ingredient::flavor)
                                * sumOf(Ingredient::texture))
                .build();
    }

    private int sumOf(ToIntFunction<Ingredient> property) {
        Integer sumOfProperty = quantities.stream()
                .map(q -> property.applyAsInt(q.ingredient()) * q.teaspoons())
                .reduce(0, Integer::sum);

        return Math.max(0, sumOfProperty);
    }
}
