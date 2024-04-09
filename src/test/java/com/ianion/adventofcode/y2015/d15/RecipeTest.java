package com.ianion.adventofcode.y2015.d15;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {

    private static final Pattern INGREDIENT = Pattern.compile(
            "(\\w+): capacity (-*\\d+), durability (-*\\d+), flavor (-*\\d+), texture (-*\\d+), calories (-*\\d+)");

    @ParameterizedTest
    @MethodSource("findPerfectRecipeTestArgs")
    void testFindPerfectRecipe(List<String> input, int expectedPerfectRecipeScore) {
        Set<Ingredient> ingredients = parseAsIngredients(input);

        Recipe perfectRecipe = Recipe.findPerfectRecipe(ingredients);

        assertThat(perfectRecipe.score()).isEqualTo(expectedPerfectRecipeScore);
    }

    @ParameterizedTest
    @MethodSource("findPerfectRecipeWith500CaloriesTestArgs")
    void testFindPerfectRecipeWith500Calories(List<String> input, int expectedPerfectRecipeScore) {
        Set<Ingredient> ingredients = parseAsIngredients(input);

        Recipe perfectRecipe = Recipe.findPerfectRecipeWithCalories(ingredients, 500);

        assertThat(perfectRecipe.score()).isEqualTo(expectedPerfectRecipeScore);
    }

    private static Stream<Arguments> findPerfectRecipeTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                        "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"), 62842880),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d15/input.txt"), 13882464)
        );
    }

    private static Stream<Arguments> findPerfectRecipeWith500CaloriesTestArgs() {
        return Stream.of(
                Arguments.of(List.of(
                        "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                        "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"), 57600000),
                Arguments.of(
                        FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d15/input.txt"), 11171160)
        );
    }

    private static Set<Ingredient> parseAsIngredients(List<String> input) {
        return input.stream()
                .map(RecipeTest::parseAsIngredient)
                .collect(Collectors.toSet());
    }

    private static Ingredient parseAsIngredient(String input) {
        Matcher matcher = INGREDIENT.matcher(input);
        matcher.find();
        return Ingredient.builder()
                .name(matcher.group(1))
                .capacity(Integer.parseInt(matcher.group(2)))
                .durability(Integer.parseInt(matcher.group(3)))
                .flavor(Integer.parseInt(matcher.group(4)))
                .texture(Integer.parseInt(matcher.group(5)))
                .calories(Integer.parseInt(matcher.group(6)))
                .build();
    }
}
