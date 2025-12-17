package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;

/**
 * Main base building (Statue) for each player.
 * Win/lose condition is tied to this building's HP.
 * No production capability.
 */
public class CommandCenter extends Building {
    public static final int DEFAULT_HP = 1000;

    public CommandCenter(Player owner) {
        this(0, 0, owner);
    }

    public CommandCenter(float x, float y, Player owner) {
        super(x, y, DEFAULT_HP, owner);
    }

    public CommandCenter(float x, float y, int maxHp, Player owner) {
        super(x, y, maxHp, owner);
    }

    /**
     * Checks if this CommandCenter is destroyed (HP <= 0).
     * Used for win/lose condition checks.
     */
    public boolean isDestroyed() {
        return getHp() <= 0;
    }

    @Override
    public void update(float deltaTime) {
        // CommandCenter has no production - just exists as win condition
    }
}
