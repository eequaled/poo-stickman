package com.stickwar.legacy.entities;

import com.stickwar.legacy.core.Player;

/**
 * Abstract base class for all building types.
 * Buildings are stationary entities that cannot move.
 */
public abstract class Building extends Entity {

    protected Building(float x, float y, int maxHp, Player owner) {
        super(x, y, maxHp, owner);
    }

    @Override
    public void update(float deltaTime) {
        // Buildings are stationary - subclasses can override for production logic
    }

    /**
     * Buildings cannot move - position is fixed.
     */
    @Override
    public void setX(float x) {
        // No-op: buildings are stationary
    }

    /**
     * Buildings cannot move - position is fixed.
     */
    @Override
    public void setY(float y) {
        // No-op: buildings are stationary
    }
}
