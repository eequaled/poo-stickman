package com.stickwar.legacy.systems;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.Archidonis;
import com.stickwar.legacy.entities.units.Miner;
import com.stickwar.legacy.entities.units.Spearton;
import com.stickwar.legacy.entities.units.Swordwrath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UnitFactory functionality.
 * Tests the Factory pattern implementation and upgrade integration.
 */
class UnitFactoryTest {
    
    private UnitFactory factory;
    private Player testPlayer;
    private UpgradeManager upgradeManager;

    @BeforeEach
    void setUp() {
        factory = new UnitFactory();
        testPlayer = new Player(1, "Test", true);
        upgradeManager = new UpgradeManager();
    }

    @Test
    void factoryInitializesCorrectly() {
        assertNotNull(factory);
        
        // Check that all unit costs are available
        Map<String, Integer> costs = factory.getAllUnitCosts();
        assertEquals(4, costs.size());
        assertTrue(costs.containsKey("MINER"));
        assertTrue(costs.containsKey("SWORDWRATH"));
        assertTrue(costs.containsKey("ARCHIDONIS"));
        assertTrue(costs.containsKey("SPEARTON"));
    }

    @Test
    void createMinerWorks() {
        Unit unit = factory.createUnit("MINER", testPlayer);
        
        assertNotNull(unit);
        assertTrue(unit instanceof Miner);
        assertEquals(testPlayer, unit.getOwner());
        assertEquals(Miner.HP, unit.getMaxHp());
        assertEquals(Miner.ATTACK, unit.getAttackPower());
        assertEquals(Miner.SPEED, unit.getMoveSpeed());
    }

    @Test
    void createSwordwrathWorks() {
        Unit unit = factory.createUnit("SWORDWRATH", testPlayer);
        
        assertNotNull(unit);
        assertTrue(unit instanceof Swordwrath);
        assertEquals(testPlayer, unit.getOwner());
        assertEquals(Swordwrath.HP, unit.getMaxHp());
        assertEquals(Swordwrath.ATTACK, unit.getAttackPower());
        assertEquals(Swordwrath.SPEED, unit.getMoveSpeed());
    }

    @Test
    void createArchidonis() {
        Unit unit = factory.createUnit("ARCHIDONIS", testPlayer);
        
        assertNotNull(unit);
        assertTrue(unit instanceof Archidonis);
        assertEquals(testPlayer, unit.getOwner());
        assertEquals(Archidonis.HP, unit.getMaxHp());
        assertEquals(Archidonis.ATTACK, unit.getAttackPower());
        assertEquals(Archidonis.SPEED, unit.getMoveSpeed());
    }

    @Test
    void createSpeartonWorks() {
        Unit unit = factory.createUnit("SPEARTON", testPlayer);
        
        assertNotNull(unit);
        assertTrue(unit instanceof Spearton);
        assertEquals(testPlayer, unit.getOwner());
        assertEquals(Spearton.HP, unit.getMaxHp());
        assertEquals(Spearton.ATTACK, unit.getAttackPower());
        assertEquals(Spearton.SPEED, unit.getMoveSpeed());
    }

    @Test
    void createUnitIsCaseInsensitive() {
        Unit miner1 = factory.createUnit("MINER", testPlayer);
        Unit miner2 = factory.createUnit("miner", testPlayer);
        Unit miner3 = factory.createUnit("Miner", testPlayer);
        
        assertTrue(miner1 instanceof Miner);
        assertTrue(miner2 instanceof Miner);
        assertTrue(miner3 instanceof Miner);
    }

    @Test
    void createUnknownUnitThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> factory.createUnit("UNKNOWN", testPlayer));
        
        assertThrows(IllegalArgumentException.class, 
            () -> factory.createUnit("WIZARD", testPlayer));
        
        assertThrows(IllegalArgumentException.class, 
            () -> factory.createUnit("", testPlayer));
    }

    @Test
    void createUnitWithNullInputsThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> factory.createUnit(null, testPlayer));
        
        assertThrows(IllegalArgumentException.class, 
            () -> factory.createUnit("MINER", null));
        
        assertThrows(IllegalArgumentException.class, 
            () -> factory.createUnit(null, null));
    }

    @Test
    void getUnitCostWorks() {
        assertEquals(Miner.COST, factory.getUnitCost("MINER"));
        assertEquals(Swordwrath.COST, factory.getUnitCost("SWORDWRATH"));
        assertEquals(Archidonis.COST, factory.getUnitCost("ARCHIDONIS"));
        assertEquals(Spearton.COST, factory.getUnitCost("SPEARTON"));
    }

    @Test
    void getUnitCostIsCaseInsensitive() {
        assertEquals(Miner.COST, factory.getUnitCost("miner"));
        assertEquals(Swordwrath.COST, factory.getUnitCost("Swordwrath"));
        assertEquals(Archidonis.COST, factory.getUnitCost("ARCHIDONIS"));
    }

    @Test
    void getUnitCostForUnknownTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> factory.getUnitCost("UNKNOWN"));
        
        assertThrows(IllegalArgumentException.class, 
            () -> factory.getUnitCost(""));
        
        assertThrows(IllegalArgumentException.class, 
            () -> factory.getUnitCost(null));
    }

    @Test
    void unitCostsMatchHierarchy() {
        // Verify cost hierarchy: Miner < Swordwrath < Archidonis < Spearton
        int minerCost = factory.getUnitCost("MINER");
        int swordwrathCost = factory.getUnitCost("SWORDWRATH");
        int archidonisCost = factory.getUnitCost("ARCHIDONIS");
        int speartonCost = factory.getUnitCost("SPEARTON");
        
        assertTrue(minerCost < swordwrathCost, 
            "Miner cost (" + minerCost + ") should be less than Swordwrath (" + swordwrathCost + ")");
        assertTrue(swordwrathCost < archidonisCost, 
            "Swordwrath cost (" + swordwrathCost + ") should be less than Archidonis (" + archidonisCost + ")");
        assertTrue(archidonisCost < speartonCost, 
            "Archidonis cost (" + archidonisCost + ") should be less than Spearton (" + speartonCost + ")");
        
        // Verify exact costs match requirements
        assertEquals(150, minerCost);
        assertEquals(200, swordwrathCost);
        assertEquals(350, archidonisCost);
        assertEquals(500, speartonCost);
    }

    @Test
    void getAllUnitCostsReturnsCorrectMap() {
        Map<String, Integer> costs = factory.getAllUnitCosts();
        
        assertEquals(4, costs.size());
        assertEquals(Integer.valueOf(150), costs.get("MINER"));
        assertEquals(Integer.valueOf(200), costs.get("SWORDWRATH"));
        assertEquals(Integer.valueOf(350), costs.get("ARCHIDONIS"));
        assertEquals(Integer.valueOf(500), costs.get("SPEARTON"));
        
        // Verify it's a copy (modifications don't affect factory)
        costs.put("TEST", 999);
        assertFalse(factory.getAllUnitCosts().containsKey("TEST"));
    }

    @Test
    void setUpgradeManagerWorks() {
        // Should not throw exception
        assertDoesNotThrow(() -> factory.setUpgradeManager(upgradeManager));
        assertDoesNotThrow(() -> factory.setUpgradeManager(null));
    }

    @Test
    void upgradesAreAppliedWhenUpgradeManagerSet() {
        // Set up upgrades
        upgradeManager.addUpgradePoints(8); // 3 + 3 + 2 = 8 points
        upgradeManager.upgradeUnitHp("MINER");      // +10% HP
        upgradeManager.upgradeUnitAttack("MINER");  // +10% Attack  
        upgradeManager.upgradeUnitSpeed("MINER");   // +5% Speed
        
        // Set upgrade manager in factory
        factory.setUpgradeManager(upgradeManager);
        
        // Create unit - should have upgrades applied
        Miner miner = (Miner) factory.createUnit("MINER", testPlayer);
        
        // Calculate expected values
        int expectedHp = Miner.HP + (int)(Miner.HP * 0.10); // +10%
        int expectedAttack = Miner.ATTACK + (int)(Miner.ATTACK * 0.10); // +10%
        float expectedSpeed = Miner.SPEED + (Miner.SPEED * 0.05f); // +5%
        
        assertEquals(expectedHp, miner.getMaxHp(), "HP upgrade should be applied");
        assertEquals(expectedHp, miner.getHp(), "Current HP should match max HP");
        assertEquals(expectedAttack, miner.getAttackPower(), "Attack upgrade should be applied");
        assertEquals(expectedSpeed, miner.getMoveSpeed(), 0.001f, "Speed upgrade should be applied");
    }

    @Test
    void upgradesAreNotAppliedWhenNoUpgradeManager() {
        // Don't set upgrade manager
        Miner miner = (Miner) factory.createUnit("MINER", testPlayer);
        
        // Should have base stats
        assertEquals(Miner.HP, miner.getMaxHp());
        assertEquals(Miner.ATTACK, miner.getAttackPower());
        assertEquals(Miner.SPEED, miner.getMoveSpeed());
    }

    @Test
    void differentUnitTypesGetCorrectUpgrades() {
        // Set up different upgrades for different unit types
        upgradeManager.addUpgradePoints(16); // 6 + 6 + 4 = 16 points
        
        // Upgrade Swordwrath
        upgradeManager.upgradeUnitHp("SWORDWRATH");
        upgradeManager.upgradeUnitHp("SWORDWRATH");      // 2 levels = +20% HP
        upgradeManager.upgradeUnitAttack("SWORDWRATH");
        upgradeManager.upgradeUnitAttack("SWORDWRATH");  // 2 levels = +20% Attack
        upgradeManager.upgradeUnitSpeed("SWORDWRATH");
        upgradeManager.upgradeUnitSpeed("SWORDWRATH");   // 2 levels = +10% Speed
        
        factory.setUpgradeManager(upgradeManager);
        
        // Create different unit types
        Miner miner = (Miner) factory.createUnit("MINER", testPlayer);
        Swordwrath swordwrath = (Swordwrath) factory.createUnit("SWORDWRATH", testPlayer);
        
        // Miner should have base stats (no upgrades)
        assertEquals(Miner.HP, miner.getMaxHp());
        assertEquals(Miner.ATTACK, miner.getAttackPower());
        assertEquals(Miner.SPEED, miner.getMoveSpeed());
        
        // Swordwrath should have upgraded stats
        int expectedHp = Swordwrath.HP + (int)(Swordwrath.HP * 0.20); // +20%
        int expectedAttack = Swordwrath.ATTACK + (int)(Swordwrath.ATTACK * 0.20); // +20%
        float expectedSpeed = Swordwrath.SPEED + (Swordwrath.SPEED * 0.10f); // +10%
        
        assertEquals(expectedHp, swordwrath.getMaxHp());
        assertEquals(expectedAttack, swordwrath.getAttackPower());
        assertEquals(expectedSpeed, swordwrath.getMoveSpeed(), 0.001f);
    }

    @Test
    void factoryWorksAfterUpgradeManagerReset() {
        // Set up upgrades
        upgradeManager.addUpgradePoints(3);
        upgradeManager.upgradeUnitHp("MINER");
        factory.setUpgradeManager(upgradeManager);
        
        // Create unit with upgrades
        Miner minerWithUpgrades = (Miner) factory.createUnit("MINER", testPlayer);
        assertTrue(minerWithUpgrades.getMaxHp() > Miner.HP);
        
        // Reset upgrades
        upgradeManager.reset();
        
        // Create new unit - should have base stats
        Miner minerAfterReset = (Miner) factory.createUnit("MINER", testPlayer);
        assertEquals(Miner.HP, minerAfterReset.getMaxHp());
    }
}