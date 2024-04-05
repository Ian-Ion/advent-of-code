package com.ianion.adventofcode.y2015.d12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Builder;

import java.util.Collection;

@Builder
public record Accounts(
        JsonElement json
) {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Accounts fromJson(String jsonText) {
        return Accounts.builder().json(gson.fromJson(jsonText, JsonElement.class)).build();
    }

    public int sumOfAllNumbers() {
        return AccountsCalculator.initialize(json).sumOfAllNumbers().result;
    }

    @Builder(toBuilder = true)
    public record AccountsCalculator(
            JsonElement json,
            int result
    ) {

        public static AccountsCalculator initialize(JsonElement root) {
            return AccountsCalculator.builder().json(root).result(0).build();
        }

        private AccountsCalculator sumOfAllNumbers() {
            return json.isJsonPrimitive()
                    ? addToResult(json.getAsJsonPrimitive())
                    : addSumOfChildElementsToResult(json);
        }

        private AccountsCalculator addSumOfChildElementsToResult(JsonElement json) {
            return json.isJsonArray()
                    ? withResult(sumChildElements(json.getAsJsonArray().asList()))
                    : withResult(sumChildElements(json.getAsJsonObject().asMap().values()));
        }

        private AccountsCalculator addToResult(JsonPrimitive primitive) {
            return withResult(result + (primitive.isNumber() ? primitive.getAsInt() : 0));
        }

        private AccountsCalculator withResult(int res) {
            return this.toBuilder().result(res).build();
        }

        private static Integer sumChildElements(Collection<JsonElement> json) {
            return json.stream().reduce(
                    0,
                    (acc, next) -> acc + AccountsCalculator.initialize(next).sumOfAllNumbers().result,
                    (first, second) -> second);
        }
    }
}
