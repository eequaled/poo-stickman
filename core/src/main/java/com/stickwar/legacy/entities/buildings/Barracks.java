package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.resources.ResourceManager;
import com.stickwar.legacy.systems.UnitFactory;
import com.stickwar.legacy.systems.UpgradeManager;

/**
 * Barracks building that spawns units instantly when gold is available.
 * Players can queue unit production by spending gold.
 * Can be upgraded with Gems to increase HP (+100 per level).
 */
public class Barracks extends Building {
    public static final int BASE_HP = 500;
    
    private final UnitFactory factory;
    private final ResourceManager resources;
    private float spawnX, spawnY;

    public Barracks(float x, float y, Player owner, ResourceManager resources) {
        this(x, y, owner, resources, null);
    }

    public Barracks(float x, float y, Player owner, ResourceManager resources, UpgradeManager upgradeManager) {
        super(x, y, calculateTotalHp(upgradeManager), owner);
        this.factory = new UnitFactory();
        this.resources = resources;
        this.spawnX = x + 30; // Spawn units slightly offset from building
        this.spawnY = y;
        
        // Set UpgradeManager in factory if provided
        if (upgradeManager != null) {
            factory.setUpgradeManager(upgradeManager);
        }
        
        // Barracks construction cost
        cost.put("Gold", 200);
    }

    /**
     * Calculates total HP including upgrade bonuses.
     */
    private static int calculateTotalHp(UpgradeManager upgradeManager) {
        int totalHp = BASE_HP;
        if (upgradeManager != null) {
            totalHp += upgradeManager.getBarracksBonusHp();
        }
        return totalHp;
    }

    /**
     * Updates the Barracks HP based on current upgrade level.
     * Should be called when upgrades are purchased.
     */
    public void applyUpgrades(UpgradeManager upgradeManager) {
        if (upgradeManager != null) {
            int newMaxHp = BASE_HP + upgradeManager.getBarracksBonusHp();
            int hpDifference = newMaxHp - this.maxHp;
            
            this.maxHp = newMaxHp;
            this.hp += hpDifference; // Increase current HP by the upgrade amount
            
            // Update factory's upgrade manager
            factory.setUpgradeManager(upgradeManager);
        }
    }

    /**
     * Spawns a unit instantly if resources are available.
     * 
     * @param unitType Type of unit to spawn (MINER, SWORDWRATH, etc.)
     * @return The spawned unit, or null if insufficient gold
     */
    public Unit spawnUnit(String unitType) {
        try {
            int cost = factory.getUnitCost(unitType);
            if (resources.spendGold(cost)) {
                Unit unit = factory.createUnit(unitType, this.getOwner());
                unit.setPosition(spawnX, spawnY);
                return unit;
            }
        } catch (IllegalArgumentException e) {
            // Unknown unit type - return null
        }
        return null;
    }

    /**
     * Gets the cost of a unit type without creating it.
     * 
     * @param unitType Type of unit to check
     * @return Gold cost, or 0 if unknown type
     */
    public int getUnitCost(String unitType) {
        try {
            return factory.getUnitCost(unitType);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    /**
     * Sets the spawn position for new units.
     * 
     * @param x Spawn X coordinate
     * @param y Spawn Y coordinate
     */
    public void setSpawnPosition(float x, float y) {
        this.spawnX = x;
        this.spawnY = y;
    }

    public float getSpawnX() {
        return spawnX;
    }

    public float getSpawnY() {
        return spawnY;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Barracks spawns units on demand - no automatic production
    }
}