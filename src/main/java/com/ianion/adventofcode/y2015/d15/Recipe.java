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
        return generatePossibleQuantityCombinationsForUnusedIngredients().stream()
                .map(this::withQuantities)
                .map(Recipe::generateScore)
                .reduce(BinaryOperator.maxBy(HIGHEST_SCORE))
                .orElse(this);
    }

    private List<List<Quantity>> generatePossibleQuantityCombinationsForUnusedIngredients() {
        return findAnyUnusedIngredient()
                .map(this::generatePossibleQuantitiesForIngredient)
                .orElse(List.of(quantities));
    }

    private List<List<Quantity>> generatePossibleQuantitiesForIngredient(Ingredient nextIngredient) {
        return possibleTeaspoonAmountsForUnusedIngredient()
                .mapToObj(nextIngredientQuantity -> generateRecursiveListOfPossibleQuantitiesWith(
                        nextIngredient, nextIngredientQuantity))
                .flatMap(List::stream)
                .toList();
    }

    private List<List<Quantity>> generateRecursiveListOfPossibleQuantitiesWith(
            Ingredient nextIngredient,
            int nextIngredientQuantity
    ) {
        Quantity nextQuantity = Quantity.builder()
                .ingredient(nextIngredient)
                .teaspoons(nextIngredientQuantity)
                .build();

        List<Quantity> updatedQuantities = Stream
                .concat(quantities.stream(), Stream.of(nextQuantity))
                .toList();

        return withQuantities(updatedQuantities)
                .generatePossibleQuantityCombinationsForUnusedIngredients();
    }

    private IntStream possibleTeaspoonAmountsForUnusedIngredient() {
        return IntStream
                .iterate(100 - sumQuantities(), i -> i > 0, i -> i - 1);
    }

    private Optional<Ingredient> findAnyUnusedIngredient() {
        return ingredients.stream()
                .filter(i -> quantities.stream().noneMatch(q -> q.ingredient().equals(i)))
                .findFirst();
    }

    private int sumQuantities() {
        return quantities.stream().map(Quantity::teaspoons).reduce(0, Integer::sum);
    }

    private Recipe withQuantities(List<Quantity> quantities) {
        return this.toBuilder()
                .quantities(quantities)
                .build();
    }

    private Recipe generateScore() {
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
