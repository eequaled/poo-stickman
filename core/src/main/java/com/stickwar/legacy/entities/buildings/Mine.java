package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;
import com.stickwar.legacy.resources.ResourceManager;

/**
 * Mine building that produces Gold over time.
 * goldPerTick=10, tickInterval=2.0f seconds
 */
public class Mine extends Building {
    public static final int DEFAULT_HP = 500;
    public static final int GOLD_PER_TICK = 10;
    public static final float TICK_INTERVAL = 2.0f;

    private int goldPerTick;
    private float tickTimer;
    private float tickInterval;

    public Mine(Player owner) {
        this(0, 0, owner);
    }

    public Mine(float x, float y, Player owner) {
        super(x, y, DEFAULT_HP, owner);
        this.goldPerTick = GOLD_PER_TICK;
        this.tickInterval = TICK_INTERVAL;
        this.tickTimer = 0;
    }

    /**
     * Updates the mine, producing gold at regular intervals.
     *
     * @param deltaTime Time since last update in seconds
     * @param resources The ResourceManager to add gold to
     */
    public void update(float deltaTime, ResourceManager resources) {
        tickTimer += deltaTime;
        if (tickTimer >= tickInterval) {
            resources.addGold(goldPerTick);
            tickTimer = 0;
        }
    }

    @Override
    public void update(float deltaTime) {
        // Use update(deltaTime, resources) instead
    }

    public int getGoldPerTick() {
        return goldPerTick;
    }

    public float getTickInterval() {
        return tickInterval;
    }

    public float getTickTimer() {
        return tickTimer;
    }
}
