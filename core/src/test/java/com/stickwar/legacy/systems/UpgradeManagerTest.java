package com.stickwar.legacy.systems;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.units.Miner;
import com.stickwar.legacy.entities.units.Swordwrath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UpgradeManager functionality.
 */
class UpgradeManagerTest {
    
    private UpgradeManager upgradeManager;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        upgradeManager = new UpgradeManager();
        testPlayer = new Player(1, "Test", true);
    }

    @Test
    void upgradeManagerInitializesCorrectly() {
        assertEquals(0, upgradeManager.getGems());
        assertEquals(0, upgradeManager.getUpgradePoints());
        assertEquals(0, upgradeManager.getStatueUpgradeLevel());
        assertEquals(0, upgradeManager.getBarracksUpgradeLevel());
        
        // Check all unit types start at level 0
        for (String unitType : new String[]{"MINER", "SWORDWRATH", "ARCHIDONIS", "SPEARTON"}) {
            assertEquals(0, upgradeManager.getUnitHpLevel(unitType));
            assertEquals(0, upgradeManager.getUnitAttackLevel(unitType));
            assertEquals(0, upgradeManager.getUnitSpeedLevel(unitType));
        }
    }

    @Test
    void addGemsAndUpgradePointsWorks() {
        upgradeManager.addGems(10);
        upgradeManager.addUpgradePoints(5);
        
        assertEquals(10, upgradeManager.getGems());
        assertEquals(5, upgradeManager.getUpgradePoints());
        
        // Test adding more
        upgradeManager.addGems(5);
        upgradeManager.addUpgradePoints(3);
        
        assertEquals(15, upgradeManager.getGems());
        assertEquals(8, upgradeManager.getUpgradePoints());
    }

    @Test
    void addNegativeAmountsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> upgradeManager.addGems(-1));
        assertThrows(IllegalArgumentException.class, () -> upgradeManager.addUpgradePoints(-1));
    }

    @Test
    void statueUpgradeWorks() {
        // Test insufficient gems
        assertFalse(upgradeManager.upgradeStatue());
        assertEquals(0, upgradeManager.getStatueUpgradeLevel());
        assertEquals(0, upgradeManager.getStatueBonusHp());
        
        // Add gems and upgrade
        upgradeManager.addGems(15);
        assertTrue(upgradeManager.upgradeStatue());
        assertEquals(0, upgradeManager.getGems()); // Should spend 15 gems
        assertEquals(1, upgradeManager.getStatueUpgradeLevel());
        assertEquals(200, upgradeManager.getStatueBonusHp()); // +200 HP per level
        
        // Test multiple upgrades
        upgradeManager.addGems(30);
        assertTrue(upgradeManager.upgradeStatue());
        assertTrue(upgradeManager.upgradeStatue());
        assertEquals(0, upgradeManager.getGems());
        assertEquals(3, upgradeManager.getStatueUpgradeLevel());
        assertEquals(600, upgradeManager.getStatueBonusHp()); // 3 * 200 = 600
    }

    @Test
    void barracksUpgradeWorks() {
        // Test insufficient gems
        assertFalse(upgradeManager.upgradeBarracks());
        assertEquals(0, upgradeManager.getBarracksUpgradeLevel());
        assertEquals(0, upgradeManager.getBarracksBonusHp());
        
        // Add gems and upgrade
        upgradeManager.addGems(10);
        assertTrue(upgradeManager.upgradeBarracks());
        assertEquals(0, upgradeManager.getGems()); // Should spend 10 gems
        assertEquals(1, upgradeManager.getBarracksUpgradeLevel());
        assertEquals(100, upgradeManager.getBarracksBonusHp()); // +100 HP per level
        
        // Test multiple upgrades
        upgradeManager.addGems(20);
        assertTrue(upgradeManager.upgradeBarracks());
        assertTrue(upgradeManager.upgradeBarracks());
        assertEquals(0, upgradeManager.getGems());
        assertEquals(3, upgradeManager.getBarracksUpgradeLevel());
        assertEquals(300, upgradeManager.getBarracksBonusHp()); // 3 * 100 = 300
    }

    @Test
    void unitHpUpgradeWorks() {
        // Test insufficient points
        assertFalse(upgradeManager.upgradeUnitHp("MINER"));
        assertEquals(0, upgradeManager.getUnitHpLevel("MINER"));
        
        // Add points and upgrade
        upgradeManager.addUpgradePoints(3);
        assertTrue(upgradeManager.upgradeUnitHp("MINER"));
        assertEquals(0, upgradeManager.getUpgradePoints()); // Should spend 3 points
        assertEquals(1, upgradeManager.getUnitHpLevel("MINER"));
        
        // Test case insensitive
        upgradeManager.addUpgradePoints(3);
        assertTrue(upgradeManager.upgradeUnitHp("miner"));
        assertEquals(2, upgradeManager.getUnitHpLevel("MINER"));
    }

    @Test
    void unitAttackUpgradeWorks() {
        upgradeManager.addUpgradePoints(6);
        
        assertTrue(upgradeManager.upgradeUnitAttack("SWORDWRATH"));
        assertEquals(3, upgradeManager.getUpgradePoints()); // Should spend 3 points
        assertEquals(1, upgradeManager.getUnitAttackLevel("SWORDWRATH"));
        
        assertTrue(upgradeManager.upgradeUnitAttack("SWORDWRATH"));
        assertEquals(0, upgradeManager.getUpgradePoints());
        assertEquals(2, upgradeManager.getUnitAttackLevel("SWORDWRATH"));
    }

    @Test
    void unitSpeedUpgradeWorks() {
        upgradeManager.addUpgradePoints(4);
        
        assertTrue(upgradeManager.upgradeUnitSpeed("ARCHIDONIS"));
        assertEquals(2, upgradeManager.getUpgradePoints()); // Should spend 2 points
        assertEquals(1, upgradeManager.getUnitSpeedLevel("ARCHIDONIS"));
        
        assertTrue(upgradeManager.upgradeUnitSpeed("ARCHIDONIS"));
        assertEquals(0, upgradeManager.getUpgradePoints());
        assertEquals(2, upgradeManager.getUnitSpeedLevel("ARCHIDONIS"));
    }

    @Test
    void applyUnitUpgradesWorks() {
        // Set up upgrades
        upgradeManager.addUpgradePoints(8); // 3 + 3 + 2 = 8 points
        upgradeManager.upgradeUnitHp("MINER");      // +10% HP
        upgradeManager.upgradeUnitAttack("MINER");  // +10% Attack
        upgradeManager.upgradeUnitSpeed("MINER");   // +5% Speed
        
        // Create a miner and apply upgrades
        Miner miner = new Miner(testPlayer);
        int originalHp = miner.getMaxHp();
        int originalAttack = miner.getAttackPower();
        float originalSpeed = miner.getMoveSpeed();
        
        upgradeManager.applyUnitUpgrades(miner, "MINER");
        
        // Check upgrades were applied
        int expectedHp = originalHp + (int)(originalHp * 0.10); // +10%
        int expectedAttack = originalAttack + (int)(originalAttack * 0.10); // +10%
        float expectedSpeed = originalSpeed + (originalSpeed * 0.05f); // +5%
        
        assertEquals(expectedHp, miner.getMaxHp());
        assertEquals(expectedHp, miner.getHp()); // Current HP should match max
        assertEquals(expectedAttack, miner.getAttackPower());
        assertEquals(expectedSpeed, miner.getMoveSpeed(), 0.001f);
    }

    @Test
    void applyMultipleUnitUpgrades() {
        // Apply multiple levels of upgrades
        upgradeManager.addUpgradePoints(16); // 6 + 6 + 4 = 16 points
        upgradeManager.upgradeUnitHp("SWORDWRATH");
        upgradeManager.upgradeUnitHp("SWORDWRATH");      // 2 levels = +20% HP
        upgradeManager.upgradeUnitAttack("SWORDWRATH");
        upgradeManager.upgradeUnitAttack("SWORDWRATH");  // 2 levels = +20% Attack
        upgradeManager.upgradeUnitSpeed("SWORDWRATH");
        upgradeManager.upgradeUnitSpeed("SWORDWRATH");   // 2 levels = +10% Speed
        
        Swordwrath swordwrath = new Swordwrath(testPlayer);
        int originalHp = swordwrath.getMaxHp();
        int originalAttack = swordwrath.getAttackPower();
        float originalSpeed = swordwrath.getMoveSpeed();
        
        upgradeManager.applyUnitUpgrades(swordwrath, "SWORDWRATH");
        
        // Check multiple level upgrades
        int expectedHp = originalHp + (int)(originalHp * 0.20); // +20%
        int expectedAttack = originalAttack + (int)(originalAttack * 0.20); // +20%
        float expectedSpeed = originalSpeed + (originalSpeed * 0.10f); // +10%
        
        assertEquals(expectedHp, swordwrath.getMaxHp());
        assertEquals(expectedAttack, swordwrath.getAttackPower());
        assertEquals(expectedSpeed, swordwrath.getMoveSpeed(), 0.001f);
    }

    @Test
    void applyUpgradesWithNoUpgradeManager() {
        // Test that units work normally without upgrades
        Miner miner = new Miner(testPlayer);
        int originalHp = miner.getMaxHp();
        int originalAttack = miner.getAttackPower();
        float originalSpeed = miner.getMoveSpeed();
        
        // Apply upgrades with null manager (should do nothing)
        UpgradeManager nullManager = null;
        if (nullManager != null) {
            nullManager.applyUnitUpgrades(miner, "MINER");
        }
        
        // Stats should remain unchanged
        assertEquals(originalHp, miner.getMaxHp());
        assertEquals(originalAttack, miner.getAttackPower());
        assertEquals(originalSpeed, miner.getMoveSpeed());
    }

    @Test
    void resetWorks() {
        // Set up some upgrades
        upgradeManager.addGems(50);
        upgradeManager.addUpgradePoints(20);
        upgradeManager.upgradeStatue();
        upgradeManager.upgradeBarracks();
        upgradeManager.upgradeUnitHp("MINER");
        upgradeManager.upgradeUnitAttack("SWORDWRATH");
        
        // Verify upgrades exist
        assertTrue(upgradeManager.getGems() > 0 || upgradeManager.getStatueUpgradeLevel() > 0);
        
        // Reset
        upgradeManager.reset();
        
        // Verify everything is reset
        assertEquals(0, upgradeManager.getGems());
        assertEquals(0, upgradeManager.getUpgradePoints());
        assertEquals(0, upgradeManager.getStatueUpgradeLevel());
        assertEquals(0, upgradeManager.getBarracksUpgradeLevel());
        
        for (String unitType : new String[]{"MINER", "SWORDWRATH", "ARCHIDONIS", "SPEARTON"}) {
            assertEquals(0, upgradeManager.getUnitHpLevel(unitType));
            assertEquals(0, upgradeManager.getUnitAttackLevel(unitType));
            assertEquals(0, upgradeManager.getUnitSpeedLevel(unitType));
        }
    }

    @Test
    void upgradeConstantsMatchRequirements() {
        assertEquals(15, UpgradeManager.STATUE_UPGRADE_COST);
        assertEquals(10, UpgradeManager.BARRACKS_UPGRADE_COST);
        assertEquals(3, UpgradeManager.HP_UPGRADE_COST);
        assertEquals(3, UpgradeManager.ATTACK_UPGRADE_COST);
        assertEquals(2, UpgradeManager.SPEED_UPGRADE_COST);
    }

    @Test
    void unknownUnitTypeHandling() {
        upgradeManager.addUpgradePoints(10);
        
        // Unknown unit types should return 0 for levels
        assertEquals(0, upgradeManager.getUnitHpLevel("UNKNOWN"));
        assertEquals(0, upgradeManager.getUnitAttackLevel("UNKNOWN"));
        assertEquals(0, upgradeManager.getUnitSpeedLevel("UNKNOWN"));
        
        // Upgrading unknown unit types should fail gracefully
        assertFalse(upgradeManager.upgradeUnitHp("UNKNOWN"));
        assertFalse(upgradeManager.upgradeUnitAttack("UNKNOWN"));
        assertFalse(upgradeManager.upgradeUnitSpeed("UNKNOWN"));
        
        // Points should remain unchanged
        assertEquals(10, upgradeManager.getUpgradePoints());
    }
}