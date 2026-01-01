package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.resources.ResourceManager;
import com.stickwar.legacy.systems.UnitFactory;

/**
 * Barracks building that spawns units instantly when gold is available.
 * Players can queue unit production by spending gold.
 */
public class Barracks extends Building {
    public static final int HP = 500;
    
    private final UnitFactory factory;
    private final ResourceManager resources;
    private float spawnX, spawnY;

    public Barracks(float x, float y, Player owner, ResourceManager resources) {
        super(x, y, HP, owner);
        this.factory = new UnitFactory();
        this.resources = resources;
        this.spawnX = x + 30; // Spawn units slightly offset from building
        this.spawnY = y;
        
        // Barracks construction cost
        cost.put("Gold", 200);
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