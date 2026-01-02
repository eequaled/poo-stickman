package com.stickwar.legacy.resources;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for ResourceManager gold operations.
 */
class ResourceManagerTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 7: ResourceManager Gold Round-Trip**
     * For any ResourceManager with starting balance B, adding amount A should result in balance (B + A).
     * For any balance B and spend amount S where B >= S, spendGold(S) should return true and result in balance (B - S).
     * For any balance B and spend amount S where B < S, spendGold(S) should return false and balance should remain B.
     * **Validates: Requirements 11.3, 11.4, 11.5**
     */
    @RepeatedTest(100)
    void resourceManagerGoldRoundTrip() {
        Random random = new Random();
        int startingGold = random.nextInt(1000) + 100; // 100-1099
        ResourceManager rm = new ResourceManager(startingGold);
        
        // Test adding gold
        int addAmount = random.nextInt(500) + 1; // 1-500
        int expectedAfterAdd = startingGold + addAmount;
        
        rm.addGold(addAmount);
        assertEquals(expectedAfterAdd, rm.getGold(),
            "Adding gold should increase balance by exact amount");
        
        // Test spending gold (affordable)
        int spendAmount = random.nextInt(expectedAfterAdd + 1); // 0 to expectedAfterAdd
        boolean canAfford = expectedAfterAdd >= spendAmount;
        int expectedAfterSpend = canAfford ? expectedAfterAdd - spendAmount : expectedAfterAdd;
        
        boolean spendResult = rm.spendGold(spendAmount);
        
        assertEquals(canAfford, spendResult,
            "spendGold should return true when affordable, false otherwise");
        assertEquals(expectedAfterSpend, rm.getGold(),
            "Gold balance should be correct after spend attempt");
        
        // Test spending more than available
        int excessAmount = rm.getGold() + random.nextInt(100) + 1; // More than current
        int goldBeforeExcess = rm.getGold();
        
        boolean excessResult = rm.spendGold(excessAmount);
        
        assertFalse(excessResult, "Should not be able to spend more than available");
        assertEquals(goldBeforeExcess, rm.getGold(),
            "Gold should remain unchanged when spending excess");
    }

    /**
     * Test that ResourceManager handles edge cases correctly.
     */
    @RepeatedTest(100)
    void resourceManagerEdgeCases() {
        Random random = new Random();
        ResourceManager rm = new ResourceManager();
        
        // Test spending exactly all gold
        int currentGold = rm.getGold();
        boolean result = rm.spendGold(currentGold);
        
        assertTrue(result, "Should be able to spend exactly all gold");
        assertEquals(0, rm.getGold(), "Should have 0 gold after spending all");
        
        // Test spending 0 gold
        boolean zeroSpend = rm.spendGold(0);
        assertTrue(zeroSpend, "Should be able to spend 0 gold");
        assertEquals(0, rm.getGold(), "Gold should remain 0");
        
        // Test adding 0 gold
        rm.addGold(0);
        assertEquals(0, rm.getGold(), "Adding 0 gold should not change balance");
    }

    /**
     * Test multiple operations in sequence.
     */
    @RepeatedTest(100)
    void multipleOperationsSequence() {
        Random random = new Random();
        ResourceManager rm = new ResourceManager();
        int expectedGold = ResourceManager.DEFAULT_STARTING_GOLD;
        
        // Perform 5-10 random operations
        int operations = random.nextInt(6) + 5;
        for (int i = 0; i < operations; i++) {
            if (random.nextBoolean() && expectedGold > 0) {
                // Spend operation
                int spendAmount = random.nextInt(expectedGold + 50); // Sometimes more than available
                boolean canAfford = expectedGold >= spendAmount;
                boolean result = rm.spendGold(spendAmount);
                
                assertEquals(canAfford, result, "Spend result should match affordability");
                if (canAfford) {
                    expectedGold -= spendAmount;
                }
            } else {
                // Add operation
                int addAmount = random.nextInt(100) + 1; // 1-100
                rm.addGold(addAmount);
                expectedGold += addAmount;
            }
            
            assertEquals(expectedGold, rm.getGold(),
                "Gold balance should match expected after operation " + i);
        }
    }

    @Test
    void resourceManagerInitializesCorrectly() {
        ResourceManager rm1 = new ResourceManager();
        assertEquals(ResourceManager.DEFAULT_STARTING_GOLD, rm1.getGold(),
            "Default constructor should use DEFAULT_STARTING_GOLD");
        
        ResourceManager rm2 = new ResourceManager(500);
        assertEquals(500, rm2.getGold(),
            "Constructor with parameter should use provided amount");
    }

    @Test
    void resourceManagerConstantsMatchRequirements() {
        assertEquals(200, ResourceManager.DEFAULT_STARTING_GOLD,
            "Default starting gold should be 200");
    }

    @Test
    void canAffordMethodWorks() {
        ResourceManager rm = new ResourceManager(100);
        
        assertTrue(rm.canAfford(50), "Should be able to afford less than balance");
        assertTrue(rm.canAfford(100), "Should be able to afford exact balance");
        assertFalse(rm.canAfford(150), "Should not be able to afford more than balance");
        assertTrue(rm.canAfford(0), "Should be able to afford 0");
    }

    @Test
    void resetMethodWorks() {
        ResourceManager rm = new ResourceManager(500);
        rm.addGold(300);
        assertEquals(800, rm.getGold());
        
        rm.reset();
        assertEquals(ResourceManager.DEFAULT_STARTING_GOLD, rm.getGold(),
            "Reset should restore default starting gold");
    }

    @Test
    void setGoldMethodWorks() {
        ResourceManager rm = new ResourceManager();
        
        rm.setGold(1000);
        assertEquals(1000, rm.getGold(), "setGold should set exact amount");
        
        rm.setGold(0);
        assertEquals(0, rm.getGold(), "setGold should work with 0");
    }

    @Test
    void negativeAmountsThrowExceptions() {
        ResourceManager rm = new ResourceManager();
        
        assertThrows(IllegalArgumentException.class, () -> rm.addGold(-1),
            "Adding negative gold should throw exception");
        assertThrows(IllegalArgumentException.class, () -> rm.spendGold(-1),
            "Spending negative gold should throw exception");
        assertThrows(IllegalArgumentException.class, () -> rm.setGold(-1),
            "Setting negative gold should throw exception");
    }

    @Test
    void getAllResourcesReturnsCorrectMap() {
        ResourceManager rm = new ResourceManager(300);
        var resources = rm.getAllResources();
        
        assertEquals(1, resources.size(), "Should have exactly one resource type");
        assertTrue(resources.containsKey("Gold"), "Should contain Gold key");
        assertEquals(300, resources.get("Gold"), "Gold value should match");
        
        // Verify it's a copy (modifying returned map doesn't affect original)
        resources.put("Gold", 999);
        assertEquals(300, rm.getGold(), "Original should be unchanged");
    }
}