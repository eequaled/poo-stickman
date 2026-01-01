package com.stickwar.legacy.entities;

import com.stickwar.legacy.core.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all building types.
 * Extends Entity with construction costs and stationary behavior.
 */
public abstract class Building extends Entity {
    protected Map<String, Integer> cost;

    protected Building(float x, float y, int maxHp, Player owner) {
        super(x, y, maxHp, owner);
        this.cost = new HashMap<>();
    }

    /**
     * Gets the construction cost of this building.
     * @return Map of resource type to amount
     */
    public Map<String, Integer> getCost() {
        return new HashMap<>(cost);
    }

    /**
     * Checks if this building has been destroyed (HP <= 0).
     * @return true if destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return hp <= 0 || isMarkedForRemoval();
    }

    @Override
    public void update(float deltaTime) {
        // Buildings are stationary - no movement logic
        // Subclasses can override for production/special abilities
    }
}