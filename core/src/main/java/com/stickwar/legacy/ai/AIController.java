package com.stickwar.legacy.ai;

import com.stickwar.legacy.campaign.LevelConfig;
import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.buildings.Barracks;
import com.stickwar.legacy.resources.ResourceManager;

import java.util.Random;

/**
 * AI controller that manages computer player behavior based on level configuration.
 * Handles unit spawning with difficulty-based timing and unit type preferences.
 */
public class AIController {
    private final LevelConfig levelConfig;
    private final Player aiPlayer;
    private final Barracks aiBarracks;
    private final ResourceManager aiResources;
    private final Random random;
    
    private float spawnTimer;
    private boolean isActive;
    private Unit lastSpawnedUnit; // Track last spawned unit for GameScreen collection
    
    // Unit type preferences based on combat preference
    private static final String[] ECONOMIC_UNITS = {"MINER"};
    private static final String[] COMBAT_UNITS = {"SWORDWRATH", "ARCHIDONIS", "SPEARTON"};
    
    public AIController(LevelConfig levelConfig, Player aiPlayer, Barracks aiBarracks, ResourceManager aiResources) {
        this.levelConfig = levelConfig;
        this.aiPlayer = aiPlayer;
        this.aiBarracks = aiBarracks;
        this.aiResources = aiResources;
        this.random = new Random();
        this.spawnTimer = 0f;
        this.isActive = true;
    }
    
    /**
     * Constructor with seed for testing.
     * 
     * @param levelConfig Level configuration
     * @param aiPlayer AI player
     * @param aiBarracks AI barracks
     * @param aiResources AI resource manager
     * @param seed Random seed for reproducible behavior
     */
    public AIController(LevelConfig levelConfig, Player aiPlayer, Barracks aiBarracks, 
                       ResourceManager aiResources, long seed) {
        this.levelConfig = levelConfig;
        this.aiPlayer = aiPlayer;
        this.aiBarracks = aiBarracks;
        this.aiResources = aiResources;
        this.random = new Random(seed);
        this.spawnTimer = 0f;
        this.isActive = true;
    }
    
    /**
     * Updates the AI controller, handling spawn timing and unit creation.
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    public void update(float deltaTime) {
        if (!isActive) {
            return;
        }
        
        spawnTimer += deltaTime;
        
        // Handle multiple spawns if deltaTime is very large
        while (spawnTimer >= levelConfig.getAiSpawnInterval()) {
            Unit newUnit = spawnUnit();
            if (newUnit != null) {
                // Store the spawned unit for collection by GameScreen
                lastSpawnedUnit = newUnit;
            }
            spawnTimer -= levelConfig.getAiSpawnInterval();
        }
    }
    
    /**
     * Attempts to spawn a unit based on combat preference and available resources.
     * Uses level configuration to determine unit type preference.
     * 
     * @return The spawned unit, or null if spawning failed
     */
    public Unit spawnUnit() {
        if (!isActive || aiBarracks == null) {
            return null;
        }
        
        String unitType = selectUnitType();
        Unit spawnedUnit = aiBarracks.spawnUnit(unitType);
        
        // If spawning failed (insufficient resources), try a cheaper unit
        if (spawnedUnit == null && !unitType.equals("MINER")) {
            spawnedUnit = aiBarracks.spawnUnit("MINER");
        }
        
        return spawnedUnit;
    }
    
    /**
     * Selects a unit type based on the level's combat preference.
     * Higher combat preference means more likely to spawn combat units.
     * 
     * @return Unit type string
     */
    private String selectUnitType() {
        float combatRoll = random.nextFloat();
        
        if (combatRoll < levelConfig.getCombatPreference()) {
            // Spawn combat unit
            return COMBAT_UNITS[random.nextInt(COMBAT_UNITS.length)];
        } else {
            // Spawn economic unit
            return ECONOMIC_UNITS[random.nextInt(ECONOMIC_UNITS.length)];
        }
    }
    
    /**
     * Gets the current spawn timer value.
     * 
     * @return Current spawn timer in seconds
     */
    public float getSpawnTimer() {
        return spawnTimer;
    }
    
    /**
     * Resets the spawn timer to 0.
     */
    public void resetSpawnTimer() {
        this.spawnTimer = 0f;
    }
    
    /**
     * Sets the spawn timer to a specific value.
     * 
     * @param timer Timer value in seconds
     */
    public void setSpawnTimer(float timer) {
        this.spawnTimer = Math.max(0f, timer);
    }
    
    /**
     * Checks if the AI is currently active.
     * 
     * @return true if AI is active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the AI active state.
     * 
     * @param active true to activate AI, false to deactivate
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Gets the level configuration used by this AI.
     * 
     * @return Level configuration
     */
    public LevelConfig getLevelConfig() {
        return levelConfig;
    }
    
    /**
     * Gets the AI player.
     * 
     * @return AI player instance
     */
    public Player getAiPlayer() {
        return aiPlayer;
    }
    
    /**
     * Gets the time remaining until next spawn attempt.
     * 
     * @return Time remaining in seconds
     */
    public float getTimeUntilNextSpawn() {
        return Math.max(0f, levelConfig.getAiSpawnInterval() - spawnTimer);
    }
    
    /**
     * Gets and clears the last spawned unit.
     * 
     * @return The last spawned unit, or null if none
     */
    public Unit getAndClearLastSpawnedUnit() {
        Unit unit = lastSpawnedUnit;
        lastSpawnedUnit = null;
        return unit;
    }
    
    /**
     * Forces an immediate spawn attempt (resets timer).
     * 
     * @return The spawned unit, or null if spawning failed
     */
    public Unit forceSpawn() {
        Unit unit = spawnUnit();
        spawnTimer = 0f;
        return unit;
    }
}