package com.stickwar.legacy.systems;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.units.Swordwrath;
import com.stickwar.legacy.entities.units.Spearton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for combat damage variance.
 * **Feature: stick-war-legacy-clone, Property 5: Combat Damage Variance**
 */
class CombatSystemTest {

    private CombatSystem combatSystem;
    private Player player;
    private Player enemy;

    @BeforeEach
    void setUp() {
        combatSystem = new CombatSystem();
        player = new Player(1, "Player", true);
        enemy = new Player(2, "Enemy", false);
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 5: Combat Damage Variance**
     * For any attack with base damage D, the actual damage dealt should be
     * within the range [D, D + 5] (base + random(0-5)).
     * **Validates: Requirements 20.1**
     */
    @RepeatedTest(100)
    void damageVarianceIsWithinExpectedRange() {
        Random random = new Random();
        int baseDamage = random.nextInt(50) + 1;  // 1-50
        
        int damage = combatSystem.calculateRawDamage(baseDamage);
        
        assertTrue(damage >= baseDamage,
            "Damage (" + damage + ") should be >= base damage (" + baseDamage + ")");
        assertTrue(damage <= baseDamage + 5,
            "Damage (" + damage + ") should be <= base damage + 5 (" + (baseDamage + 5) + ")");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 5: Combat Damage Variance**
     * Verify damage calculation with actual units.
     * **Validates: Requirements 20.1**
     */
    @RepeatedTest(100)
    void unitDamageCalculationIncludesVariance() {
        Swordwrath attacker = new Swordwrath(player);
        Swordwrath target = new Swordwrath(enemy);
        
        int baseDamage = attacker.getEffectiveAttackPower();
        int damage = combatSystem.calculateDamage(attacker, target);
        
        assertTrue(damage >= baseDamage,
            "Damage should be >= base attack power");
        assertTrue(damage <= baseDamage + 5,
            "Damage should be <= base attack power + 5");
    }

    @Test
    void speartonDefenseIsAppliedInCombat() {
        Swordwrath attacker = new Swordwrath(player);
        Spearton target = new Spearton(enemy);
        
        int initialHp = target.getHp();
        int baseDamage = attacker.getEffectiveAttackPower();
        int maxPossibleDamage = (int) ((baseDamage + 5) * 0.7f);
        
        combatSystem.processAttack(attacker, target);
        
        int actualDamage = initialHp - target.getHp();
        assertTrue(actualDamage <= maxPossibleDamage,
            "Damage to Spearton should be reduced by shield");
    }

    @Test
    void processAttackReducesTargetHp() {
        Swordwrath attacker = new Swordwrath(player);
        Swordwrath target = new Swordwrath(enemy);
        
        int initialHp = target.getHp();
        combatSystem.processAttack(attacker, target);
        
        assertTrue(target.getHp() < initialHp,
            "Target HP should be reduced after attack");
    }

    @Test
    void nullTargetDoesNotCauseError() {
        Swordwrath attacker = new Swordwrath(player);
        
        assertDoesNotThrow(() -> combatSystem.processAttack(attacker, null));
    }

    @Test
    void nullAttackerDoesNotCauseError() {
        Swordwrath target = new Swordwrath(enemy);
        
        assertDoesNotThrow(() -> combatSystem.processAttack(null, target));
    }

    @Test
    void isInRangeReturnsTrueWhenClose() {
        Swordwrath attacker = new Swordwrath(0, 0, player);
        Swordwrath target = new Swordwrath(30, 0, enemy);
        
        assertTrue(combatSystem.isInRange(attacker, target));
    }

    @Test
    void isInRangeReturnsFalseWhenFar() {
        Swordwrath attacker = new Swordwrath(0, 0, player);
        Swordwrath target = new Swordwrath(100, 0, enemy);
        
        assertFalse(combatSystem.isInRange(attacker, target));
    }
}
