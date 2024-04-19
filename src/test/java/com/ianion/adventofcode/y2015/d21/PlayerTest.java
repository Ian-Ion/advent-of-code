package com.ianion.adventofcode.y2015.d21;

import com.ianion.adventofcode.common.FileLoader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    private static final Pattern HIT_POINTS = Pattern.compile("Hit Points: (\\d+)");
    private static final Pattern DAMAGE = Pattern.compile("Damage: (\\d+)");
    private static final Pattern ARMOR = Pattern.compile("Armor: (\\d+)");

    @Test
    void testCalculateNumberOfPresentsDeliveredToHouseV1() {
        Boss boss = readAsBoss(
                FileLoader.readFileAsStringList("src/test/resources/inputs/y2015/d21/input.txt"));

        Player player = Player.findLowestCostEquipmentToWinAgainst(boss);

        assertThat(player.getEquipmentCost()).isEqualTo(91);
    }

    private Boss readAsBoss(List<String> input) {
        Matcher hitPointsMatcher = HIT_POINTS.matcher(input.get(0));
        Matcher damageMatcher = DAMAGE.matcher(input.get(1));
        Matcher armorMatcher = ARMOR.matcher(input.get(2));

        hitPointsMatcher.find();
        damageMatcher.find();
        armorMatcher.find();

        return Boss.builder()
                .hp(Integer.parseInt(hitPointsMatcher.group(1)))
                .damage(Integer.parseInt(damageMatcher.group(1)))
                .armor(Integer.parseInt(armorMatcher.group(1)))
                .build();
    }
}
