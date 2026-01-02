package com.stickwar.legacy.campaign;

/**
 * Configuration for campaign levels with increasing difficulty.
 * Defines AI behavior parameters for each of the 3 campaign levels.
 */
public class LevelConfig {
    private final int levelNumber;
    private final String levelName;
    private final float aiSpawnInterval;
    private final float combatPreference;
    
    // Level definitions
    public static final LevelConfig LEVEL_1 = new LevelConfig(1, "Easy", 6.0f, 0.30f);
    public static final LevelConfig LEVEL_2 = new LevelConfig(2, "Medium", 4.5f, 0.60f);
    public static final LevelConfig LEVEL_3 = new LevelConfig(3, "Hard", 3.0f, 0.90f);
    
    private LevelConfig(int levelNumber, String levelName, float aiSpawnInterval, float combatPreference) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.aiSpawnInterval = aiSpawnInterval;
        this.combatPreference = combatPreference;
    }
    
    /**
     * Gets the level configuration for a specific level number.
     * 
     * @param level Level number (1, 2, or 3)
     * @return LevelConfig for the specified level
     * @throws IllegalArgumentException if level is not 1, 2, or 3
     */
    public static LevelConfig getLevel(int level) {
        switch (level) {
            case 1:
                return LEVEL_1;
            case 2:
                return LEVEL_2;
            case 3:
                return LEVEL_3;
            default:
                throw new IllegalArgumentException("Invalid level: " + level + ". Must be 1, 2, or 3.");
        }
    }
    
    /**
     * Gets all available levels.
     * 
     * @return Array of all level configurations
     */
    public static LevelConfig[] getAllLevels() {
        return new LevelConfig[]{LEVEL_1, LEVEL_2, LEVEL_3};
    }
    
    /**
     * Gets the total number of campaign levels.
     * 
     * @return Total number of levels (3)
     */
    public static int getTotalLevels() {
        return 3;
    }
    
    // Getters
    public int getLevelNumber() {
        return levelNumber;
    }
    
    public String getLevelName() {
        return levelName;
    }
    
    /**
     * Gets the AI spawn interval in seconds.
     * Lower values mean faster spawning (harder difficulty).
     * 
     * @return Spawn interval in seconds
     */
    public float getAiSpawnInterval() {
        return aiSpawnInterval;
    }
    
    /**
     * Gets the combat preference ratio (0.0 to 1.0).
     * Higher values mean AI prefers combat units over economic units.
     * 
     * @return Combat preference ratio
     */
    public float getCombatPreference() {
        return combatPreference;
    }
    
    /**
     * Checks if this is the first level.
     * 
     * @return true if this is level 1
     */
    public boolean isFirstLevel() {
        return levelNumber == 1;
    }
    
    /**
     * Checks if this is the final level.
     * 
     * @return true if this is the last level
     */
    public boolean isFinalLevel() {
        return levelNumber == getTotalLevels();
    }
    
    @Override
    public String toString() {
        return "Level " + levelNumber + " (" + levelName + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LevelConfig that = (LevelConfig) obj;
        return levelNumber == that.levelNumber;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(levelNumber);
    }
}