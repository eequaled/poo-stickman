package com.stickwar.legacy.systems;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Entity;
import com.stickwar.legacy.entities.units.Miner;
import com.stickwar.legacy.entities.units.Spearton;
import com.stickwar.legacy.entities.units.Swordwrath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CombatSystem functionality.
 * Tests damage calculation, range checking, and attack processing.
 */
class CombatSystemTest {
    
    private CombatSystem combatSystem;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        combatSystem = new CombatSystem(12345L); // Seeded for reproducible tests
        player1 = new Player(1, "Player1", true);
        player2 = new Player(2, "Player2", false);
    }

    @Test
    void combatSystemInitializesCorrectly() {
        CombatSystem system = new CombatSystem();
        assertNotNull(system.getRandom());
        
        // Test seeded constructor
        CombatSystem seededSystem = new CombatSystem(42L);
        assertNotNull(seededSystem.getRandom());
    }

    @Test
    void calculateRawDamageAddsVariance() {
        // Test with seeded random for predictable results
        int baseDamage = 20;
        int damage1 = combatSystem.calculateRawDamage(baseDamage);
        int damage2 = combatSystem.calculateRawDamage(baseDamage);
        
        // Damage should be base + [0-5]
        assertTrue(damage1 >= baseDamage && damage1 <= baseDamage + 5);
        assertTrue(damage2 >= baseDamage && damage2 <= baseDamage + 5);
    }

    @RepeatedTest(100)
    @Tag("property-test")
    @Tag("feature:combat")
    @Tag("property:damage-variance")
    void propertyDamageVarianceIsWithinBounds() {
        // Property 9: Combat Damage Variance
        // Validates: Requirements 15.1
        CombatSystem system = new CombatSystem(); // Use unseeded for true randomness
        
        int baseDamage = 15;
        int damage = system.calculateRawDamage(baseDamage);
        
        // Damage should always be base + [0-5]
        assertTrue(damage >= baseDamage, 
            "Damage " + damage + " should be at least base damage " + baseDamage);
        assertTrue(damage <= baseDamage + 5, 
            "Damage " + damage + " should be at most base damage + 5 (" + (baseDamage + 5) + ")");
    }

    @Test
    void calculateDamageWithSpeartonDefense() {
        Swordwrath attacker = new Swordwrath(player1);
        Spearton speartonTarget = new Spearton(player2);
        
        // Calculate damage multiple times to account for variance
        int totalDamage = 0;
        int iterations = 100;
        
        for (int i = 0; i < iterations; i++) {
            int damage = combatSystem.calculateDamage(attacker, speartonTarget);
            totalDamage += damage;
            
            // Damage should be reduced by Spearton's defense (30% reduction)
            int maxPossibleDamage = (int)((attacker.getEffectiveAttackPower() + 5) * 0.7f);
            int minPossibleDamage = (int)(attacker.getEffectiveAttackPower() * 0.7f);
            
            assertTrue(damage >= minPossibleDamage && damage <= maxPossibleDamage,
                "Damage " + damage + " should be within Spearton defense range [" + 
                minPossibleDamage + ", " + maxPossibleDamage + "]");
        }
        
        // Average damage should be roughly (base + 2.5) * 0.7
        double expectedAverage = (attacker.getEffectiveAttackPower() + 2.5) * 0.7;
        double actualAverage = (double) totalDamage / iterations;
        
        // Allow 20% tolerance for randomness
        assertTrue(Math.abs(actualAverage - expectedAverage) / expectedAverage < 0.2,
            "Average damage " + actualAverage + " should be close to expected " + expectedAverage);
    }

    @Test
    void calculateDamageWithNormalTarget() {
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        int damage = combatSystem.calculateDamage(attacker, target);
        
        // Should be base damage + [0-5] without defense reduction
        int expectedMin = attacker.getEffectiveAttackPower();
        int expectedMax = attacker.getEffectiveAttackPower() + 5;
        
        assertTrue(damage >= expectedMin && damage <= expectedMax,
            "Damage " + damage + " should be within range [" + expectedMin + ", " + expectedMax + "]");
    }

    @Test
    void isInRangeCalculatesCorrectly() {
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        // Set positions
        attacker.setPosition(0, 0);
        target.setPosition(30, 40); // Distance = sqrt(30² + 40²) = 50
        
        // Swordwrath has attack range of 50
        assertTrue(combatSystem.isInRange(attacker, target),
            "Target at distance 50 should be within Swordwrath range of 50");
        
        // Move target further away
        target.setPosition(60, 80); // Distance = sqrt(60² + 80²) = 100
        assertFalse(combatSystem.isInRange(attacker, target),
            "Target at distance 100 should be outside Swordwrath range of 50");
        
        // Test exact range boundary
        target.setPosition(50, 0); // Distance = 50
        assertTrue(combatSystem.isInRange(attacker, target),
            "Target at exact range should be within attack range");
    }

    @RepeatedTest(100)
    @Tag("property-test")
    @Tag("feature:combat")
    @Tag("property:range-calculation")
    void propertyRangeCalculationIsAccurate() {
        // Property 10: Combat Range Calculation
        // Validates: Requirements 15.3
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        // Generate random positions
        float x1 = (float)(Math.random() * 200 - 100); // -100 to 100
        float y1 = (float)(Math.random() * 200 - 100);
        float x2 = (float)(Math.random() * 200 - 100);
        float y2 = (float)(Math.random() * 200 - 100);
        
        attacker.setPosition(x1, y1);
        target.setPosition(x2, y2);
        
        // Calculate expected distance using Euclidean formula
        float dx = x2 - x1;
        float dy = y2 - y1;
        float expectedDistance = (float) Math.sqrt(dx * dx + dy * dy);
        
        boolean inRange = combatSystem.isInRange(attacker, target);
        boolean shouldBeInRange = expectedDistance <= attacker.getAttackRange();
        
        assertEquals(shouldBeInRange, inRange,
            "Range check should match Euclidean distance calculation. " +
            "Distance: " + expectedDistance + ", Range: " + attacker.getAttackRange());
    }

    @Test
    void processAttackAppliesDamage() {
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        int originalHp = target.getHp();
        
        combatSystem.processAttack(attacker, target);
        
        // Target should have taken damage
        assertTrue(target.getHp() < originalHp,
            "Target should have taken damage from attack");
        
        // Damage should be within expected range
        int damageTaken = originalHp - target.getHp();
        int expectedMin = attacker.getEffectiveAttackPower();
        int expectedMax = attacker.getEffectiveAttackPower() + 5;
        
        assertTrue(damageTaken >= expectedMin && damageTaken <= expectedMax,
            "Damage taken " + damageTaken + " should be within range [" + expectedMin + ", " + expectedMax + "]");
    }

    @Test
    void processAttackSkipsDeadTargets() {
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        // Kill the target
        target.takeDamage(target.getHp());
        assertTrue(target.isMarkedForRemoval(), "Target should be marked for removal");
        
        int deadHp = target.getHp();
        
        // Attack should be ignored
        combatSystem.processAttack(attacker, target);
        
        assertEquals(deadHp, target.getHp(),
            "Dead target HP should not change when attacked");
    }

    @Test
    void processAttackHandlesNullInputs() {
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        // Should not throw exceptions with null inputs
        assertDoesNotThrow(() -> combatSystem.processAttack(null, target));
        assertDoesNotThrow(() -> combatSystem.processAttack(attacker, null));
        assertDoesNotThrow(() -> combatSystem.processAttack(null, null));
    }

    @Test
    void isInRangeHandlesNullInputs() {
        Swordwrath attacker = new Swordwrath(player1);
        Miner target = new Miner(player2);
        
        // Should return false for null inputs
        assertFalse(combatSystem.isInRange(null, target));
        assertFalse(combatSystem.isInRange(attacker, null));
        assertFalse(combatSystem.isInRange(null, null));
    }

    @Test
    void damageIsNeverNegative() {
        // Create a very weak attacker
        Miner weakAttacker = new Miner(player1);
        Spearton strongDefender = new Spearton(player2);
        
        // Even with defense reduction and low attack, damage should be >= 0
        for (int i = 0; i < 100; i++) {
            int damage = combatSystem.calculateDamage(weakAttacker, strongDefender);
            assertTrue(damage >= 0, "Damage should never be negative, got: " + damage);
        }
    }

    @Test
    void userControlledUnitsGetAttackBonus() {
        // Create user-controlled and AI-controlled units
        Player userPlayer = new Player(1, "User", true);
        Player aiPlayer = new Player(2, "AI", false);
        
        Swordwrath userUnit = new Swordwrath(userPlayer);
        Swordwrath aiUnit = new Swordwrath(aiPlayer);
        Miner target = new Miner(aiPlayer);
        
        userUnit.setUserControlled(true);
        aiUnit.setUserControlled(false);
        
        // Calculate damage multiple times and compare averages
        int userDamageTotal = 0;
        int aiDamageTotal = 0;
        int iterations = 100;
        
        for (int i = 0; i < iterations; i++) {
            userDamageTotal += combatSystem.calculateDamage(userUnit, target);
            aiDamageTotal += combatSystem.calculateDamage(aiUnit, target);
        }
        
        double userAverage = (double) userDamageTotal / iterations;
        double aiAverage = (double) aiDamageTotal / iterations;
        
        // User-controlled units should deal more damage on average (2x multiplier)
        assertTrue(userAverage > aiAverage * 1.5,
            "User-controlled units should deal significantly more damage. " +
            "User avg: " + userAverage + ", AI avg: " + aiAverage);
    }
}