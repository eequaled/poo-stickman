package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;

/**
 * Main base building - primary objective.
 * Destroying the enemy CommandCenter wins the game.
 * No production capability - purely defensive structure.
 */
public class CommandCenter extends Building {
    public static final int HP = 1000;

    public CommandCenter(float x, float y, Player owner) {
        super(x, y, HP, owner);
        
        // CommandCenter has no construction cost (pre-built)
        cost.put("Gold", 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // CommandCenter has no special abilities - just exists as target
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        // Game should check for CommandCenter destruction to trigger win/lose
    }
}