package com.stickwar.legacy.ai;

import com.stickwar.legacy.campaign.LevelConfig;
import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.buildings.Barracks;
import com.stickwar.legacy.resources.ResourceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AIController functionality.
 * Tests AI behavior, spawn timing, and unit selection logic.
 */
class AIControllerTest {
    
    private Player aiPlayer;
    private Barracks aiBarracks;
    private ResourceManager aiResources;
    private AIController aiController;
    private LevelConfig testLevel;

    @BeforeEach
    void setUp() {
        aiPlayer = new Player(2, "AI", false);
        aiResources = new ResourceManager();
        aiResources.addGold(10000); // Give AI plenty of gold
        aiBarracks = new Barracks(100, 100, aiPlayer, aiResources);
        testLevel = LevelConfig.LEVEL_1; // 6.0s spawn interval, 30% combat preference
        aiController = new AIController(testLevel, aiPlayer, aiBarracks, aiResources, 12345L);
    }

    @Test
    void aiControllerInitializesCorrectly() {
        assertEquals(testLevel, aiController.getLevelConfig());
        assertEquals(aiPlayer, aiController.getAiPlayer());
        assertEquals(0f, aiController.getSpawnTimer());
        assertTrue(aiController.isActive());
        assertEquals(6.0f, aiController.getTimeUntilNextSpawn());
    }

    @Test
    void updateIncrementsSpawnTimer() {
        float deltaTime = 1.5f;
        aiController.update(deltaTime);
        
        assertEquals(deltaTime, aiController.getSpawnTimer());
        assertEquals(4.5f, aiController.getTimeUntilNextSpawn(), 0.001f);
    }

    @Test
    void updateDoesNotSpawnUnitBeforeInterval() {
        int initialGold = aiResources.getGold();
        
        // Update for less than spawn interval
        aiController.update(5.9f);
        
        // Gold should not have changed (no unit spawned)
        assertEquals(initialGold, aiResources.getGold());
        assertEquals(5.9f, aiController.getSpawnTimer());
    }

    @Test
    void updateSpawnsUnitAfterInterval() {
        int initialGold = aiResources.getGold();
        
        // Update for exactly the spawn interval
        aiController.update(6.0f);
        
        // Gold should have decreased (unit spawned)
        assertTrue(aiResources.getGold() < initialGold, "Gold should decrease after spawning unit");
        assertEquals(0f, aiController.getSpawnTimer()); // Timer should reset
    }

    @Test
    void updateSpawnsMultipleTimesForLongDelta() {
        int initialGold = aiResources.getGold();
        
        // Update for more than one spawn interval
        aiController.update(13.0f); // Should trigger 2 spawns (6.0 + 6.0 + 1.0 remaining)
        
        // Gold should have decreased for at least one unit (maybe two depending on selection)
        assertTrue(aiResources.getGold() < initialGold - 100, "Gold should decrease after multiple spawn attempts");
        assertEquals(1.0f, aiController.getSpawnTimer(), 0.001f);
    }

    @Test
    void inactiveAIDoesNotUpdate() {
        aiController.setActive(false);
        int initialGold = aiResources.getGold();
        
        aiController.update(10.0f);
        
        // Gold should not have changed
        assertEquals(initialGold, aiResources.getGold());
        assertEquals(0f, aiController.getSpawnTimer()); // Timer should not change
    }

    @Test
    void spawnUnitDecreasesGold() {
        int initialGold = aiResources.getGold();
        
        aiController.spawnUnit();
        
        // Gold should have decreased
        assertTrue(aiResources.getGold() < initialGold, "Gold should decrease after spawning unit");
    }

    @Test
    void spawnUnitDoesNothingWhenInactive() {
        aiController.setActive(false);
        int initialGold = aiResources.getGold();
        
        aiController.spawnUnit();
        
        // Gold should not have changed
        assertEquals(initialGold, aiResources.getGold());
    }

    @Test
    void spawnUnitDoesNothingWithNullBarracks() {
        AIController controllerWithNullBarracks = new AIController(testLevel, aiPlayer, null, aiResources);
        
        // Should not throw exception
        assertDoesNotThrow(() -> controllerWithNullBarracks.spawnUnit());
    }

    @Test
    void resetSpawnTimerWorks() {
        aiController.update(3.0f);
        assertEquals(3.0f, aiController.getSpawnTimer());
        
        aiController.resetSpawnTimer();
        assertEquals(0f, aiController.getSpawnTimer());
    }

    @Test
    void setSpawnTimerWorks() {
        aiController.setSpawnTimer(2.5f);
        assertEquals(2.5f, aiController.getSpawnTimer());
        
        // Should not allow negative values
        aiController.setSpawnTimer(-1.0f);
        assertEquals(0f, aiController.getSpawnTimer());
    }

    @Test
    void setActiveWorks() {
        assertTrue(aiController.isActive());
        
        aiController.setActive(false);
        assertFalse(aiController.isActive());
        
        aiController.setActive(true);
        assertTrue(aiController.isActive());
    }

    @Test
    void forceSpawnResetsTimer() {
        int initialGold = aiResources.getGold();
        
        aiController.update(3.0f);
        assertEquals(3.0f, aiController.getSpawnTimer());
        
        aiController.forceSpawn();
        
        // Gold should have decreased
        assertTrue(aiResources.getGold() < initialGold, "Gold should decrease after force spawn");
        assertEquals(0f, aiController.getSpawnTimer());
    }

    @Test
    void differentLevelConfigsAffectBehavior() {
        // Test with Level 3 (hard) - faster spawning
        AIController hardAI = new AIController(LevelConfig.LEVEL_3, aiPlayer, aiBarracks, aiResources);
        
        assertEquals(LevelConfig.LEVEL_3, hardAI.getLevelConfig());
        assertEquals(3.0f, hardAI.getTimeUntilNextSpawn()); // Faster than Level 1's 6.0f
    }

    @Test
    void timeUntilNextSpawnCalculatesCorrectly() {
        assertEquals(6.0f, aiController.getTimeUntilNextSpawn());
        
        aiController.update(2.0f);
        assertEquals(4.0f, aiController.getTimeUntilNextSpawn());
        
        aiController.update(4.0f); // Should trigger spawn and reset
        assertEquals(6.0f, aiController.getTimeUntilNextSpawn());
    }

    @Test
    void constructorWithoutSeedWorks() {
        AIController unseededAI = new AIController(testLevel, aiPlayer, aiBarracks, aiResources);
        
        assertNotNull(unseededAI);
        assertEquals(testLevel, unseededAI.getLevelConfig());
        assertTrue(unseededAI.isActive());
    }

    @Test
    void spawnUnitFallsBackToMinerWhenLowGold() {
        // Give AI only enough gold for a miner
        aiResources.spendGold(aiResources.getGold() - 150);
        assertEquals(150, aiResources.getGold());
        
        // Try to spawn many units - should eventually spawn miners
        for (int i = 0; i < 50; i++) {
            aiController.spawnUnit();
            if (aiResources.getGold() == 0) {
                break; // Successfully spawned a miner
            }
        }
        
        // Should have spawned at least one unit (miner)
        assertEquals(0, aiResources.getGold(), "Should have spawned a miner when gold is limited");
    }

    @Test
    void aiSpawnsUnitsBasedOnCombatPreference() {
        // Test with different levels to see different behavior
        AIController easyAI = new AIController(LevelConfig.LEVEL_1, aiPlayer, aiBarracks, aiResources, 42L);
        AIController hardAI = new AIController(LevelConfig.LEVEL_3, aiPlayer, aiBarracks, aiResources, 42L);
        
        // Both should be able to spawn units
        int easyInitialGold = aiResources.getGold();
        easyAI.spawnUnit();
        assertTrue(aiResources.getGold() < easyInitialGold, "Easy AI should spawn units");
        
        // Reset gold for hard AI test
        aiResources.addGold(10000);
        int hardInitialGold = aiResources.getGold();
        hardAI.spawnUnit();
        assertTrue(aiResources.getGold() < hardInitialGold, "Hard AI should spawn units");
    }
}