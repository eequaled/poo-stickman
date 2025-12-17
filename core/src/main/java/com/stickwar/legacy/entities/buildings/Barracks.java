package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.*;
import com.stickwar.legacy.resources.ResourceManager;

/**
 * Barracks building that spawns units instantly when gold is available.
 */
public class Barracks extends Building {
    public static final int DEFAULT_HP = 500;

    private ResourceManager resources;
    private float spawnX;
    private float spawnY;

    public Barracks(Player owner, ResourceManager resources) {
        this(0, 0, owner, resources);
    }

    public Barracks(float x, float y, Player owner, ResourceManager resources) {
        super(x, y, DEFAULT_HP, owner);
        this.resources = resources;
        this.spawnX = x;
        this.spawnY = y;
    }

    /**
     * Spawns a unit instantly if resources are available.
     *
     * @param unitType Type of unit to spawn ("Miner", "Swordwrath", "Archidonis", "Spearton")
     * @return The spawned unit, or null if insufficient gold
     */
    public Unit spawnUnit(String unitType) {
        int cost = getUnitCost(unitType);
        if (cost > 0 && resources.spendGold(cost)) {
            Unit unit = createUnit(unitType);
            if (unit != null) {
                unit.setPosition(spawnX, spawnY);
            }
            return unit;
        }
        return null;
    }

    /**
     * Gets the cost of a unit type.
     */
    public int getUnitCost(String unitType) {
        switch (unitType.toUpperCase()) {
            case "MINER": return Miner.COST;
            case "SWORDWRATH": return Swordwrath.COST;
            case "ARCHIDONIS": return Archidonis.COST;
            case "SPEARTON": return Spearton.COST;
            default: return 0;
        }
    }

    /**
     * Creates a unit of the specified type.
     */
    private Unit createUnit(String unitType) {
        switch (unitType.toUpperCase()) {
            case "MINER": return new Miner(getOwner());
            case "SWORDWRATH": return new Swordwrath(getOwner());
            case "ARCHIDONIS": return new Archidonis(getOwner());
            case "SPEARTON": return new Spearton(getOwner());
            default: return null;
        }
    }

    public void setSpawnPosition(float x, float y) {
        this.spawnX = x;
        this.spawnY = y;
    }

    @Override
    public void update(float deltaTime) {
        // Barracks doesn't auto-produce - spawning is triggered by player
    }
}
