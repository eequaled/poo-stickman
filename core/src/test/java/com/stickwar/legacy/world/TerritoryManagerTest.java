package com.stickwar.legacy.world;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for TerritoryManager frontline calculation.
 * **Feature: stick-war-legacy-clone, Property 8: Territory Frontline Calculation**
 */
class TerritoryManagerTest {

    /**
     * Concrete test unit for testing TerritoryManager.
     */
    static class TestUnit extends Unit {
        public TestUnit(float x, float y, Player owner) {
            super(x, y, 100, 10, 100, 100, owner);
        }

        @Override
        public void update(float deltaTime) {
            // No-op for testing
        }
    }

    private GameMap map;
    private TerritoryManager territoryManager;
    private Player player;
    private Player enemy;

    @BeforeEach
    void setUp() {
        map = new GameMap(100, 10);
        territoryManager = new TerritoryManager(map);
        player = new Player(1, "Player", true);
        enemy = new Player(2, "Enemy", false);
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 8: Territory Frontline Calculation**
     * For any set of player units, the TerritoryManager should set ownership
     * for all tiles with x <= max(unit.x) to the player.
     * **Validates: Requirements 3.1, 3.2**
     */
    @RepeatedTest(100)
    void frontlineCalculationSetsOwnershipCorrectly() {
        Random random = new Random();
        List<Unit> units = new ArrayList<>();
        
        // Generate random number of units (1-10)
        int numUnits = random.nextInt(10) + 1;
        int expectedMaxTileX = 0;
        
        for (int i = 0; i < numUnits; i++) {
            // Generate world coordinates (units are positioned in world space)
            int worldX = random.nextInt(map.getWidth() * TerritoryManager.WORLD_UNITS_PER_TILE);
            TestUnit unit = new TestUnit(worldX, 0, player);
            units.add(unit);
            int tileX = worldX / TerritoryManager.WORLD_UNITS_PER_TILE;
            expectedMaxTileX = Math.max(expectedMaxTileX, tileX);
        }
        
        // Add some enemy units (should not affect player frontline)
        int numEnemyUnits = random.nextInt(5);
        for (int i = 0; i < numEnemyUnits; i++) {
            int worldX = random.nextInt(map.getWidth() * TerritoryManager.WORLD_UNITS_PER_TILE);
            units.add(new TestUnit(worldX, 0, enemy));
        }
        
        territoryManager.updateFrontline(player, units);
        
        // Verify all tiles up to frontline are owned by player
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                if (x <= expectedMaxTileX) {
                    assertEquals(player, tile.getOwner(),
                        "Tile at x=" + x + " should be owned by player (frontline at " + expectedMaxTileX + ")");
                }
            }
        }
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 8: Territory Frontline Calculation**
     * Frontline calculation returns correct max tile X position.
     * **Validates: Requirements 3.1, 3.2**
     */
    @RepeatedTest(100)
    void calculateFrontlineReturnsMaxUnitX() {
        Random random = new Random();
        List<Unit> units = new ArrayList<>();
        
        int numUnits = random.nextInt(10) + 1;
        int expectedMaxTileX = 0;
        
        for (int i = 0; i < numUnits; i++) {
            // Generate world coordinates (units are positioned in world space)
            int worldX = random.nextInt(map.getWidth() * TerritoryManager.WORLD_UNITS_PER_TILE);
            units.add(new TestUnit(worldX, 0, player));
            int tileX = worldX / TerritoryManager.WORLD_UNITS_PER_TILE;
            expectedMaxTileX = Math.max(expectedMaxTileX, tileX);
        }
        
        int frontline = territoryManager.calculateFrontline(player, units);
        
        assertEquals(expectedMaxTileX, frontline,
            "Frontline should equal the maximum tile X position of player units");
    }

    @Test
    void emptyUnitListResultsInZeroFrontline() {
        List<Unit> units = new ArrayList<>();
        
        int frontline = territoryManager.calculateFrontline(player, units);
        
        assertEquals(0, frontline, "Empty unit list should result in frontline at 0");
    }

    @Test
    void enemyUnitsDoNotAffectPlayerFrontline() {
        List<Unit> units = new ArrayList<>();
        units.add(new TestUnit(10 * TerritoryManager.WORLD_UNITS_PER_TILE, 0, player));
        units.add(new TestUnit(50 * TerritoryManager.WORLD_UNITS_PER_TILE, 0, enemy));  // Enemy further ahead
        
        int frontline = territoryManager.calculateFrontline(player, units);
        
        assertEquals(10, frontline, "Enemy units should not affect player frontline");
    }
}
