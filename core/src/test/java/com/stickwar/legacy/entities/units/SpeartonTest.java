package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Spearton shield defense.
 * **Feature: stick-war-legacy-clone, Property 4: Spearton Shield Defense**
 */
class SpeartonTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 4: Spearton Shield Defense**
     * For any Spearton receiving damage D, the actual damage taken should be D * 0.7 (30% reduction).
     * **Validates: Requirements 10.2**
     */
    @RepeatedTest(100)
    void speartonShieldReducesDamageBy30Percent() {
        Random random = new Random();
        Player player = new Player(1, "Test", true);
        Spearton spearton = new Spearton(player);
        
        int initialHp = spearton.getHp();
        int incomingDamage = random.nextInt(100) + 1;  // 1-100
        int expectedReducedDamage = (int) (incomingDamage * 0.7f);
        int expectedHp = initialHp - expectedReducedDamage;
        
        spearton.takeDamage(incomingDamage);
        
        assertEquals(expectedHp, spearton.getHp(),
            "Spearton should take " + expectedReducedDamage + " damage (70% of " + incomingDamage + ")");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 4: Spearton Shield Defense**
     * Verify defense multiplier is exactly 0.7f.
     * **Validates: Requirements 10.2**
     */
    @Test
    void speartonDefenseMultiplierIs07() {
        Player player = new Player(1, "Test", true);
        Spearton spearton = new Spearton(player);
        
        assertEquals(0.7f, spearton.getDefenseMultiplier(), 0.001f,
            "Spearton defense multiplier should be 0.7");
    }

    @Test
    void speartonHasCorrectStats() {
        Player player = new Player(1, "Test", true);
        Spearton spearton = new Spearton(player);
        
        assertEquals(150, spearton.getMaxHp());
        assertEquals(20, spearton.getAttackPower());
        assertEquals(70, spearton.getMoveSpeed(), 0.001f);
        assertEquals(500, spearton.getCost());
    }

    @Test
    void speartonShieldIsActiveByDefault() {
        Player player = new Player(1, "Test", true);
        Spearton spearton = new Spearton(player);
        
        assertTrue(spearton.isBlocking(), "Spearton shield should be active by default");
    }
}
