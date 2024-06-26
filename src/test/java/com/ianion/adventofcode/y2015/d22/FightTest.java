package com.ianion.adventofcode.y2015.d22;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class FightTest {

    private static final Pattern HIT_POINTS = Pattern.compile("Hit Points: (\\d+)");
    private static final Pattern DAMAGE = Pattern.compile("Damage: (\\d+)");

    @Test
    void testPlayOptimallyIfFightNotOver() {
        Boss boss = readAsBoss(
                FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d22/input.txt"));

        Fight fight = Fight.between(Wizard.initialize(), boss, Mode.NORMAL).playOptimallyIfFightNotOver();

        assertThat(fight.getTotalManaSpent()).isEqualTo(900);
    }

    @Test
    void testPlayOptimallyIfFightNotOver_hardMode() {
        Boss boss = readAsBoss(
                FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d22/input.txt"));

        Fight fight = Fight.between(Wizard.initialize(), boss, Mode.HARD).playOptimallyIfFightNotOver();

        assertThat(fight.getTotalManaSpent()).isEqualTo(1216);
    }

    private Boss readAsBoss(List<String> input) {
        Matcher hitPointsMatcher = HIT_POINTS.matcher(input.get(0));
        Matcher damageMatcher = DAMAGE.matcher(input.get(1));

        hitPointsMatcher.find();
        damageMatcher.find();

        return Boss.builder()
                .hp(Integer.parseInt(hitPointsMatcher.group(1)))
                .damage(Integer.parseInt(damageMatcher.group(1)))
                .build();
    }
}
