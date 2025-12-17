package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.*;
import com.stickwar.legacy.resources.ResourceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Barracks unit spawning.
 * **Feature: stick-war-legacy-clone, Property 7: Barracks Unit Spawning**
 */
class BarracksTest {

    private Player player;
    private ResourceManager resources;
    private Barracks barracks;

    @BeforeEach
    void setUp() {
        player = new Player(1, "Test", true);
        resources = new ResourceManager(0);  // Start with 0 gold
        barracks = new Barracks(100, 50, player, resources);
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 7: Barracks Unit Spawning**
     * For any unit type with cost C, when Barracks.spawnUnit() is called with gold G >= C,
     * the gold should be reduced by C and a unit should be created.
     * **Validates: Requirements 21.1, 21.2**
     */
    @RepeatedTest(100)
    void spawnUnitWithSufficientGoldDeductsAndCreates() {
        Random random = new Random();
        String[] unitTypes = {"Miner", "Swordwrath", "Archidonis", "Spearton"};
        String unitType = unitTypes[random.nextInt(unitTypes.length)];
        
        int cost = barracks.getUnitCost(unitType);
        int extraGold = random.nextInt(500);  // 0-499 extra gold
        int startingGold = cost + extraGold;
        
        resources.setGold(startingGold);
        
        Unit unit = barracks.spawnUnit(unitType);
        
        assertNotNull(unit, "Unit should be created when gold >= cost");
        assertEquals(startingGold - cost, resources.getGold(),
            "Gold should be reduced by exactly the unit cost");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 7: Barracks Unit Spawning**
     * When G < C, no unit should be created and gold should remain unchanged.
     * **Validates: Requirements 21.1, 21.2**
     */
    @RepeatedTest(100)
    void spawnUnitWithInsufficientGoldFailsAndPreservesGold() {
        Random random = new Random();
        String[] unitTypes = {"Miner", "Swordwrath", "Archidonis", "Spearton"};
        String unitType = unitTypes[random.nextInt(unitTypes.length)];
        
        int cost = barracks.getUnitCost(unitType);
        int startingGold = random.nextInt(cost);  // 0 to cost-1
        
        resources.setGold(startingGold);
        
        Unit unit = barracks.spawnUnit(unitType);
        
        assertNull(unit, "Unit should not be created when gold < cost");
        assertEquals(startingGold, resources.getGold(),
            "Gold should remain unchanged when spawn fails");
    }

    @Test
    void spawnMinerDeductsCorrectCost() {
        resources.setGold(200);
        Unit unit = barracks.spawnUnit("Miner");
        
        assertNotNull(unit);
        assertTrue(unit instanceof Miner);
        assertEquals(200 - Miner.COST, resources.getGold());
    }

    @Test
    void spawnSwordwrathDeductsCorrectCost() {
        resources.setGold(300);
        Unit unit = barracks.spawnUnit("Swordwrath");
        
        assertNotNull(unit);
        assertTrue(unit instanceof Swordwrath);
        assertEquals(300 - Swordwrath.COST, resources.getGold());
    }

    @Test
    void spawnedUnitHasCorrectOwner() {
        resources.setGold(500);
        Unit unit = barracks.spawnUnit("Miner");
        
        assertNotNull(unit);
        assertEquals(player, unit.getOwner());
    }

    @Test
    void spawnedUnitHasCorrectPosition() {
        resources.setGold(500);
        barracks.setSpawnPosition(100, 50);
        Unit unit = barracks.spawnUnit("Miner");
        
        assertNotNull(unit);
        assertEquals(100, unit.getX(), 0.001f);
        assertEquals(50, unit.getY(), 0.001f);
    }

    @Test
    void invalidUnitTypeReturnsNull() {
        resources.setGold(1000);
        Unit unit = barracks.spawnUnit("InvalidUnit");
        
        assertNull(unit);
        assertEquals(1000, resources.getGold(), "Gold should not be deducted for invalid unit type");
    }
}
