package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Miner gold collection behavior.
 */
class MinerTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 5: Miner Gold Collection Round-Trip**
     * For any Miner with goldCapacity and any sequence of collectGold() calls,
     * the total collected should never exceed goldCapacity, and depositGold()
     * should return exactly the amount collected and reset carriedGold to 0.
     * **Validates: Requirements 4.2, 4.3**
     */
    @RepeatedTest(100)
    void minerGoldCollectionRoundTrip() {
        Random random = new Random();
        Miner miner = new Miner(null);
        
        // Collect random amounts of gold
        int totalAttempted = 0;
        int expectedCarried = 0;
        
        // Make 1-5 collection attempts
        int attempts = random.nextInt(5) + 1;
        for (int i = 0; i < attempts; i++) {
            int amount = random.nextInt(30) + 1; // 1-30 gold per attempt
            totalAttempted += amount;
            
            int collected = miner.collectGold(amount);
            expectedCarried = Math.min(totalAttempted, Miner.GOLD_CAPACITY);
            
            assertEquals(expectedCarried, miner.getCarriedGold(),
                "Carried gold should never exceed capacity");
            assertTrue(collected >= 0, "Collected amount should be non-negative");
            assertTrue(collected <= amount, "Cannot collect more than offered");
            assertTrue(miner.getCarriedGold() <= Miner.GOLD_CAPACITY,
                "Carried gold should never exceed capacity");
        }
        
        // Test deposit round-trip
        int carriedBeforeDeposit = miner.getCarriedGold();
        int deposited = miner.depositGold();
        
        assertEquals(carriedBeforeDeposit, deposited,
            "Deposited amount should equal what was carried");
        assertEquals(0, miner.getCarriedGold(),
            "Carried gold should be 0 after deposit");
    }

    /**
     * Test that miner respects gold capacity limits.
     */
    @RepeatedTest(100)
    void minerRespectsCapacityLimits() {
        Random random = new Random();
        Miner miner = new Miner(null);
        
        // Try to collect more than capacity
        int excessAmount = Miner.GOLD_CAPACITY + random.nextInt(50) + 1;
        int collected = miner.collectGold(excessAmount);
        
        assertEquals(Miner.GOLD_CAPACITY, collected,
            "Should collect exactly capacity when offered excess");
        assertEquals(Miner.GOLD_CAPACITY, miner.getCarriedGold(),
            "Should carry exactly capacity amount");
        assertFalse(miner.canCarryMore(),
            "Should not be able to carry more when at capacity");
        
        // Try to collect more when already at capacity
        int additionalAttempt = random.nextInt(20) + 1;
        int additionalCollected = miner.collectGold(additionalAttempt);
        
        assertEquals(0, additionalCollected,
            "Should collect 0 when already at capacity");
        assertEquals(Miner.GOLD_CAPACITY, miner.getCarriedGold(),
            "Carried amount should remain at capacity");
    }

    /**
     * Test multiple collect/deposit cycles.
     */
    @RepeatedTest(100)
    void multipleCollectDepositCycles() {
        Random random = new Random();
        Miner miner = new Miner(null);
        
        for (int cycle = 0; cycle < 3; cycle++) {
            // Collect some gold
            int amount = random.nextInt(Miner.GOLD_CAPACITY) + 1;
            int collected = miner.collectGold(amount);
            
            assertTrue(collected > 0, "Should collect some gold");
            assertEquals(collected, miner.getCarriedGold(),
                "Carried gold should match collected amount");
            
            // Deposit it
            int deposited = miner.depositGold();
            assertEquals(collected, deposited,
                "Deposited should equal collected");
            assertEquals(0, miner.getCarriedGold(),
                "Should have no gold after deposit");
            assertTrue(miner.canCarryMore(),
                "Should be able to carry more after deposit");
        }
    }

    @Test
    void minerInitializesWithCorrectStats() {
        Player player = new Player(1, "Test", true);
        Miner miner = new Miner(10, 20, player);
        
        assertEquals(10, miner.getX());
        assertEquals(20, miner.getY());
        assertEquals(Miner.HP, miner.getHp());
        assertEquals(Miner.HP, miner.getMaxHp());
        assertEquals(Miner.ATTACK, miner.getAttackPower());
        assertEquals(Miner.SPEED, miner.getMoveSpeed());
        assertEquals(Miner.COST, miner.getCost());
        assertEquals(Miner.GOLD_CAPACITY, miner.getGoldCapacity());
        assertEquals(0, miner.getCarriedGold());
        assertEquals(player, miner.getOwner());
        assertTrue(miner.canCarryMore());
        assertFalse(miner.isCarryingGold());
    }

    @Test
    void minerStatsMatchRequirements() {
        assertEquals(50, Miner.HP, "Miner HP should be 50");
        assertEquals(5, Miner.ATTACK, "Miner Attack should be 5");
        assertEquals(80, Miner.SPEED, "Miner Speed should be 80");
        assertEquals(150, Miner.COST, "Miner Cost should be 150");
        assertEquals(50, Miner.GOLD_CAPACITY, "Miner Gold Capacity should be 50");
    }
}