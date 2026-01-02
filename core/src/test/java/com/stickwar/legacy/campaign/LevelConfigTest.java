package com.stickwar.legacy.campaign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LevelConfig functionality.
 * Tests the 3-level campaign configuration system.
 */
class LevelConfigTest {

    @Test
    void levelConstantsAreCorrect() {
        // Level 1 - Easy
        assertEquals(1, LevelConfig.LEVEL_1.getLevelNumber());
        assertEquals("Easy", LevelConfig.LEVEL_1.getLevelName());
        assertEquals(6.0f, LevelConfig.LEVEL_1.getAiSpawnInterval());
        assertEquals(0.30f, LevelConfig.LEVEL_1.getCombatPreference());
        
        // Level 2 - Medium
        assertEquals(2, LevelConfig.LEVEL_2.getLevelNumber());
        assertEquals("Medium", LevelConfig.LEVEL_2.getLevelName());
        assertEquals(4.5f, LevelConfig.LEVEL_2.getAiSpawnInterval());
        assertEquals(0.60f, LevelConfig.LEVEL_2.getCombatPreference());
        
        // Level 3 - Hard
        assertEquals(3, LevelConfig.LEVEL_3.getLevelNumber());
        assertEquals("Hard", LevelConfig.LEVEL_3.getLevelName());
        assertEquals(3.0f, LevelConfig.LEVEL_3.getAiSpawnInterval());
        assertEquals(0.90f, LevelConfig.LEVEL_3.getCombatPreference());
    }

    @Test
    void getLevelReturnsCorrectConfigs() {
        assertEquals(LevelConfig.LEVEL_1, LevelConfig.getLevel(1));
        assertEquals(LevelConfig.LEVEL_2, LevelConfig.getLevel(2));
        assertEquals(LevelConfig.LEVEL_3, LevelConfig.getLevel(3));
    }

    @Test
    void getLevelThrowsExceptionForInvalidLevels() {
        assertThrows(IllegalArgumentException.class, () -> LevelConfig.getLevel(0));
        assertThrows(IllegalArgumentException.class, () -> LevelConfig.getLevel(4));
        assertThrows(IllegalArgumentException.class, () -> LevelConfig.getLevel(-1));
        assertThrows(IllegalArgumentException.class, () -> LevelConfig.getLevel(100));
    }

    @Test
    void getAllLevelsReturnsAllThreeLevels() {
        LevelConfig[] levels = LevelConfig.getAllLevels();
        
        assertEquals(3, levels.length);
        assertEquals(LevelConfig.LEVEL_1, levels[0]);
        assertEquals(LevelConfig.LEVEL_2, levels[1]);
        assertEquals(LevelConfig.LEVEL_3, levels[2]);
    }

    @Test
    void getTotalLevelsReturnsThree() {
        assertEquals(3, LevelConfig.getTotalLevels());
    }

    @Test
    void difficultyProgressionIsCorrect() {
        // Spawn intervals should decrease (faster spawning = harder)
        assertTrue(LevelConfig.LEVEL_1.getAiSpawnInterval() > LevelConfig.LEVEL_2.getAiSpawnInterval());
        assertTrue(LevelConfig.LEVEL_2.getAiSpawnInterval() > LevelConfig.LEVEL_3.getAiSpawnInterval());
        
        // Combat preference should increase (more combat units = harder)
        assertTrue(LevelConfig.LEVEL_1.getCombatPreference() < LevelConfig.LEVEL_2.getCombatPreference());
        assertTrue(LevelConfig.LEVEL_2.getCombatPreference() < LevelConfig.LEVEL_3.getCombatPreference());
    }

    @Test
    void isFirstLevelWorks() {
        assertTrue(LevelConfig.LEVEL_1.isFirstLevel());
        assertFalse(LevelConfig.LEVEL_2.isFirstLevel());
        assertFalse(LevelConfig.LEVEL_3.isFirstLevel());
    }

    @Test
    void isFinalLevelWorks() {
        assertFalse(LevelConfig.LEVEL_1.isFinalLevel());
        assertFalse(LevelConfig.LEVEL_2.isFinalLevel());
        assertTrue(LevelConfig.LEVEL_3.isFinalLevel());
    }

    @Test
    void toStringWorks() {
        assertEquals("Level 1 (Easy)", LevelConfig.LEVEL_1.toString());
        assertEquals("Level 2 (Medium)", LevelConfig.LEVEL_2.toString());
        assertEquals("Level 3 (Hard)", LevelConfig.LEVEL_3.toString());
    }

    @Test
    void equalsAndHashCodeWork() {
        // Same level should be equal
        assertEquals(LevelConfig.LEVEL_1, LevelConfig.getLevel(1));
        assertEquals(LevelConfig.LEVEL_1.hashCode(), LevelConfig.getLevel(1).hashCode());
        
        // Different levels should not be equal
        assertNotEquals(LevelConfig.LEVEL_1, LevelConfig.LEVEL_2);
        assertNotEquals(LevelConfig.LEVEL_2, LevelConfig.LEVEL_3);
        
        // Null and different class should not be equal
        assertNotEquals(LevelConfig.LEVEL_1, null);
        assertNotEquals(LevelConfig.LEVEL_1, "Level 1");
    }

    @Test
    void combatPreferenceIsWithinValidRange() {
        // All combat preferences should be between 0.0 and 1.0
        for (LevelConfig level : LevelConfig.getAllLevels()) {
            float preference = level.getCombatPreference();
            assertTrue(preference >= 0.0f && preference <= 1.0f,
                "Combat preference " + preference + " should be between 0.0 and 1.0");
        }
    }

    @Test
    void spawnIntervalsArePositive() {
        // All spawn intervals should be positive
        for (LevelConfig level : LevelConfig.getAllLevels()) {
            float interval = level.getAiSpawnInterval();
            assertTrue(interval > 0.0f,
                "Spawn interval " + interval + " should be positive");
        }
    }

    @Test
    void levelNumbersAreSequential() {
        LevelConfig[] levels = LevelConfig.getAllLevels();
        
        for (int i = 0; i < levels.length; i++) {
            assertEquals(i + 1, levels[i].getLevelNumber(),
                "Level at index " + i + " should have level number " + (i + 1));
        }
    }
}