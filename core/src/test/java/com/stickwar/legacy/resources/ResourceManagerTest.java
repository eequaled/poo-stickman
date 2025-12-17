package com.stickwar.legacy.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for ResourceManager round-trip operations.
 * **Feature: stick-war-legacy-clone, Property 3: Resource Manager Round-Trip**
 */
class ResourceManagerTest {

    private ResourceManager resources;

    @BeforeEach
    void setUp() {
        resources = new ResourceManager(0);  // Start with 0 gold
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 3: Resource Manager Round-Trip**
     * For any ResourceManager and any positive integer amount, calling addGold(amount)
     * followed by getGold() should return a value that includes the added amount.
     * **Validates: Requirements 19.2, 19.3, 19.4**
     */
    @RepeatedTest(100)
    void addGoldIncreasesBalance() {
        Random random = new Random();
        int startingGold = random.nextInt(1000);  // 0-999
        int addAmount = random.nextInt(500) + 1;   // 1-500
        
        resources.setGold(startingGold);
        resources.addGold(addAmount);
        
        assertEquals(startingGold + addAmount, resources.getGold(),
            "Gold should increase by exactly the added amount");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 3: Resource Manager Round-Trip**
     * For any starting balance and spend amount where balance >= spend,
     * spendGold(spend) should return true and reduce balance by exactly spend.
     * **Validates: Requirements 19.2, 19.3, 19.4**
     */
    @RepeatedTest(100)
    void spendGoldWithSufficientBalanceSucceeds() {
        Random random = new Random();
        int startingGold = random.nextInt(1000) + 100;  // 100-1099
        int spendAmount = random.nextInt(startingGold) + 1;  // 1 to startingGold
        
        resources.setGold(startingGold);
        boolean result = resources.spendGold(spendAmount);
        
        assertTrue(result, "spendGold should return true when balance >= amount");
        assertEquals(startingGold - spendAmount, resources.getGold(),
            "Gold should decrease by exactly the spent amount");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 3: Resource Manager Round-Trip**
     * For any starting balance and spend amount where balance < spend,
     * spendGold(spend) should return false and balance should remain unchanged.
     * **Validates: Requirements 19.2, 19.3, 19.4**
     */
    @RepeatedTest(100)
    void spendGoldWithInsufficientBalanceFails() {
        Random random = new Random();
        int startingGold = random.nextInt(100);  // 0-99
        int spendAmount = startingGold + random.nextInt(100) + 1;  // More than starting
        
        resources.setGold(startingGold);
        boolean result = resources.spendGold(spendAmount);
        
        assertFalse(result, "spendGold should return false when balance < amount");
        assertEquals(startingGold, resources.getGold(),
            "Gold should remain unchanged when spend fails");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 3: Resource Manager Round-Trip**
     * Round-trip: add then spend should work correctly.
     * **Validates: Requirements 19.2, 19.3, 19.4**
     */
    @RepeatedTest(100)
    void addThenSpendRoundTrip() {
        Random random = new Random();
        int addAmount = random.nextInt(500) + 1;  // 1-500
        
        resources.setGold(0);
        resources.addGold(addAmount);
        boolean result = resources.spendGold(addAmount);
        
        assertTrue(result, "Should be able to spend exactly what was added");
        assertEquals(0, resources.getGold(), "Balance should be 0 after spending all added gold");
    }

    @Test
    void initialGoldIsSetCorrectly() {
        ResourceManager rm = new ResourceManager(500);
        assertEquals(500, rm.getGold());
    }

    @Test
    void defaultStartingGoldIs200() {
        ResourceManager rm = new ResourceManager();
        assertEquals(200, rm.getGold());
    }

    @Test
    void canAffordReturnsTrueWhenSufficient() {
        resources.setGold(100);
        assertTrue(resources.canAfford(100));
        assertTrue(resources.canAfford(50));
    }

    @Test
    void canAffordReturnsFalseWhenInsufficient() {
        resources.setGold(100);
        assertFalse(resources.canAfford(101));
    }

    @Test
    void resetSetsGoldToDefault() {
        resources.setGold(1000);
        resources.reset();
        assertEquals(ResourceManager.DEFAULT_STARTING_GOLD, resources.getGold());
    }
}
