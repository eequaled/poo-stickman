package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Spearton shield defense (polymorphism).
 */
class SpeartonTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 6: Spearton Shield Defense (Polymorphism)**
     * For any Spearton receiving damage D, the actual hp reduction should be
     * (D × 0.7) rounded to integer, demonstrating polymorphic override of takeDamage().
     * **Validates: Requirements 7.2**
     */
    @RepeatedTest(100)
    void speartonShieldDefensePolymorphism() {
        Random random = new Random();
        int initialHp = random.nextInt(100) + 50; // 50-149 (less than max to avoid death)
        int damage = random.nextInt(30) + 1; // 1-30 damage
        
        Spearton spearton = new Spearton(null);
        spearton.setHp(initialHp); // Set to test value
        
        int expectedReducedDamage = (int)(damage * Spearton.DEFENSE_MULTIPLIER);
        int expectedHp = initialHp - expectedReducedDamage;
        
        spearton.takeDamage(damage);
        
        assertEquals(expectedHp, spearton.getHp(),
            "Spearton should take reduced damage due to shield (damage × 0.7)");
        
        // Verify the defense multiplier is correct
        assertEquals(0.7f, Spearton.DEFENSE_MULTIPLIER, 0.001f,
            "Defense multiplier should be 0.7 (30% damage reduction)");
    }

    /**
     * Test that Spearton shield works with various damage amounts.
     */
    @RepeatedTest(100)
    void speartonShieldWorksWithVariousDamageAmounts() {
        Random random = new Random();
        Spearton spearton = new Spearton(null);
        
        // Test with different damage amounts
        int damage = random.nextInt(200) + 1; // 1-200 damage
        int initialHp = spearton.getHp();
        
        int expectedReducedDamage = (int)(damage * 0.7f);
        int expectedHp = Math.max(0, initialHp - expectedReducedDamage);
        
        spearton.takeDamage(damage);
        
        assertEquals(expectedHp, spearton.getHp(),
            "Shield should reduce damage consistently regardless of amount");
        
        if (expectedHp <= 0) {
            assertTrue(spearton.isMarkedForRemoval(),
                "Spearton should be marked for removal when HP <= 0");
        }
    }

    /**
     * Test that multiple attacks accumulate correctly with shield.
     */
    @RepeatedTest(100)
    void multipleAttacksWithShield() {
        Random random = new Random();
        Spearton spearton = new Spearton(null);
        
        int damage1 = random.nextInt(20) + 1; // 1-20
        int damage2 = random.nextInt(20) + 1; // 1-20
        
        int initialHp = spearton.getHp();
        int expectedDamage1 = (int)(damage1 * 0.7f);
        int expectedDamage2 = (int)(damage2 * 0.7f);
        int expectedFinalHp = initialHp - expectedDamage1 - expectedDamage2;
        
        spearton.takeDamage(damage1);
        spearton.takeDamage(damage2);
        
        assertEquals(expectedFinalHp, spearton.getHp(),
            "Multiple attacks should accumulate with shield reduction");
    }

    /**
     * Test that Spearton shield is always active.
     */
    @Test
    void speartonShieldIsAlwaysActive() {
        Spearton spearton = new Spearton(null);
        
        assertTrue(spearton.isBlocking(),
            "Spearton shield should be active by default");
        assertEquals(0.7f, spearton.getDefenseMultiplier(), 0.001f,
            "Defense multiplier should be 0.7");
    }

    @Test
    void speartonInitializesWithCorrectStats() {
        Player player = new Player(1, "Test", true);
        Spearton spearton = new Spearton(10, 20, player);
        
        assertEquals(10, spearton.getX());
        assertEquals(20, spearton.getY());
        assertEquals(Spearton.HP, spearton.getHp());
        assertEquals(Spearton.HP, spearton.getMaxHp());
        assertEquals(Spearton.ATTACK, spearton.getAttackPower());
        assertEquals(Spearton.SPEED, spearton.getMoveSpeed());
        assertEquals(Spearton.COST, spearton.getCost());
        assertEquals(Spearton.SPEAR_RANGE, spearton.getAttackRange());
        assertEquals(player, spearton.getOwner());
        assertTrue(spearton.isBlocking());
    }

    @Test
    void speartonStatsMatchRequirements() {
        assertEquals(150, Spearton.HP, "Spearton HP should be 150");
        assertEquals(20, Spearton.ATTACK, "Spearton Attack should be 20");
        assertEquals(70, Spearton.SPEED, "Spearton Speed should be 70");
        assertEquals(500, Spearton.COST, "Spearton Cost should be 500");
        assertEquals(0.7f, Spearton.DEFENSE_MULTIPLIER, 0.001f, 
            "Spearton Defense Multiplier should be 0.7");
    }

    /**
     * Test edge case: Spearton dies when shield-reduced damage is enough.
     */
    @Test
    void speartonDiesWhenShieldReducedDamageIsEnough() {
        Spearton spearton = new Spearton(null);
        spearton.setHp(10); // Low HP
        
        // Deal enough damage to kill even with shield
        int damage = 50; // 50 * 0.7 = 35 damage, more than 10 HP
        spearton.takeDamage(damage);
        
        assertTrue(spearton.getHp() <= 0, "Spearton should die from high damage");
        assertTrue(spearton.isMarkedForRemoval(), "Spearton should be marked for removal");
    }
}