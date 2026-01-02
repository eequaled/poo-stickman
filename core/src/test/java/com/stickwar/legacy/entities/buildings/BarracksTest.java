package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.resources.ResourceManager;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Barracks unit spawning behavior.
 */
class BarracksTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 8: Barracks Unit Spawning**
     * For any unit type with cost C, when Barracks.spawnUnit() is called with gold G >= C,
     * the gold should be reduced by exactly C and a non-null unit should be returned.
     * When G < C, null should be returned and gold should remain unchanged.
     * **Validates: Requirements 9.2, 9.3**
     */
    @RepeatedTest(100)
    void barracksUnitSpawning() {
        Random random = new Random();
        Player player = new Player(1, "Test", true);
        ResourceManager resources = new ResourceManager();
        
        // Set random starting gold
        int startingGold = random.nextInt(1000) + 100; // 100-1099
        resources.setGold(startingGold);
        
        Barracks barracks = new Barracks(0, 0, player, resources);
        
        // Test with random unit type
        String[] unitTypes = {"MINER", "SWORDWRATH", "ARCHIDONIS", "SPEARTON"};
        String unitType = unitTypes[random.nextInt(unitTypes.length)];
        
        int unitCost = barracks.getUnitCost(unitType);
        int goldBefore = resources.getGold();
        boolean canAfford = goldBefore >= unitCost;
        
        Unit spawnedUnit = barracks.spawnUnit(unitType);
        int goldAfter = resources.getGold();
        
        if (canAfford) {
            assertNotNull(spawnedUnit, "Should spawn unit when gold is sufficient");
            assertEquals(goldBefore - unitCost, goldAfter,
                "Gold should be reduced by exactly the unit cost");
            assertEquals(player, spawnedUnit.getOwner(),
                "Spawned unit should belong to the correct player");
            assertEquals(barracks.getSpawnX(), spawnedUnit.getX(),
                "Unit should spawn at barracks spawn position X");
            assertEquals(barracks.getSpawnY(), spawnedUnit.getY(),
                "Unit should spawn at barracks spawn position Y");
        } else {
            assertNull(spawnedUnit, "Should not spawn unit when gold is insufficient");
            assertEquals(goldBefore, goldAfter,
                "Gold should remain unchanged when spawn fails");
        }
    }

    /**
     * Test that Barracks handles unknown unit types gracefully.
     */
    @RepeatedTest(100)
    void barracksHandlesUnknownUnitTypes() {
        Random random = new Random();
        Player player = new Player(1, "Test", true);
        ResourceManager resources = new ResourceManager();
        resources.setGold(1000); // Plenty of gold
        
        Barracks barracks = new Barracks(0, 0, player, resources);
        
        // Try to spawn unknown unit type
        String unknownType = "UNKNOWN_UNIT_" + random.nextInt(1000);
        int goldBefore = resources.getGold();
        
        Unit spawnedUnit = barracks.spawnUnit(unknownType);
        int goldAfter = resources.getGold();
        
        assertNull(spawnedUnit, "Should not spawn unit for unknown type");
        assertEquals(goldBefore, goldAfter,
            "Gold should remain unchanged for unknown unit type");
        assertEquals(0, barracks.getUnitCost(unknownType),
            "Unknown unit type should have cost 0");
    }

    /**
     * Test multiple unit spawning in sequence.
     */
    @RepeatedTest(100)
    void multipleUnitSpawning() {
        Random random = new Random();
        Player player = new Player(1, "Test", true);
        ResourceManager resources = new ResourceManager();
        resources.setGold(2000); // Plenty of gold
        
        Barracks barracks = new Barracks(0, 0, player, resources);
        
        String[] unitTypes = {"MINER", "SWORDWRATH"};
        int totalCost = 0;
        int unitsSpawned = 0;
        
        // Spawn 2-5 units
        int spawnCount = random.nextInt(4) + 2;
        for (int i = 0; i < spawnCount; i++) {
            String unitType = unitTypes[random.nextInt(unitTypes.length)];
            int unitCost = barracks.getUnitCost(unitType);
            
            if (resources.getGold() >= unitCost) {
                Unit unit = barracks.spawnUnit(unitType);
                assertNotNull(unit, "Should spawn unit when gold is available");
                totalCost += unitCost;
                unitsSpawned++;
            }
        }
        
        assertTrue(unitsSpawned > 0, "Should have spawned at least one unit");
        assertEquals(2000 - totalCost, resources.getGold(),
            "Total gold spent should match sum of unit costs");
    }

    @Test
    void barracksInitializesWithCorrectValues() {
        Player player = new Player(1, "Test", true);
        ResourceManager resources = new ResourceManager();
        Barracks barracks = new Barracks(10, 20, player, resources);
        
        assertEquals(10, barracks.getX());
        assertEquals(20, barracks.getY());
        assertEquals(Barracks.BASE_HP, barracks.getHp());
        assertEquals(Barracks.BASE_HP, barracks.getMaxHp());
        assertEquals(player, barracks.getOwner());
        assertFalse(barracks.isDestroyed());
        
        // Check spawn position is offset
        assertEquals(40, barracks.getSpawnX()); // x + 30
        assertEquals(20, barracks.getSpawnY()); // same y
    }

    @Test
    void barracksStatsMatchRequirements() {
        assertEquals(500, Barracks.BASE_HP, "Barracks HP should be 500");
    }

    @Test
    void barracksSpawnPositionCanBeChanged() {
        Player player = new Player(1, "Test", true);
        ResourceManager resources = new ResourceManager();
        Barracks barracks = new Barracks(0, 0, player, resources);
        
        barracks.setSpawnPosition(100, 200);
        assertEquals(100, barracks.getSpawnX());
        assertEquals(200, barracks.getSpawnY());
    }

    @Test
    void barracksIsDestroyedWhenHpZero() {
        Player player = new Player(1, "Test", true);
        ResourceManager resources = new ResourceManager();
        Barracks barracks = new Barracks(0, 0, player, resources);
        
        assertFalse(barracks.isDestroyed(), "Barracks should not be destroyed initially");
        
        barracks.takeDamage(Barracks.BASE_HP);
        assertTrue(barracks.isDestroyed(), "Barracks should be destroyed when HP = 0");
        assertTrue(barracks.isMarkedForRemoval(), "Barracks should be marked for removal");
    }
}