package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;
import com.stickwar.legacy.resources.ResourceManager;

/**
 * Mine building that produces Gold over time.
 * Provides passive income for the player's economy.
 */
public class Mine extends Building {
    public static final int HP = 300;
    
    private int goldPerTick = 10;
    private float tickTimer = 0;
    private float tickInterval = 2.0f; // seconds

    public Mine(float x, float y, Player owner) {
        super(x, y, HP, owner);
        
        // Mine construction cost
        cost.put("Gold", 100);
    }

    /**
     * Updates the mine and produces gold at regular intervals.
     * 
     * @param deltaTime Time since last update
     * @param resources ResourceManager to add gold to
     */
    public void update(float deltaTime, ResourceManager resources) {
        super.update(deltaTime);
        
        if (isDestroyed()) {
            return; // Dead mines don't produce gold
        }
        
        tickTimer += deltaTime;
        if (tickTimer >= tickInterval) {
            resources.addGold(goldPerTick);
            tickTimer = 0;
        }
    }

    /**
     * Gets the gold production rate.
     * @return Gold per tick
     */
    public int getGoldPerTick() {
        return goldPerTick;
    }

    /**
     * Gets the production interval.
     * @return Seconds between gold production
     */
    public float getTickInterval() {
        return tickInterval;
    }

    /**
     * Sets the gold production rate (for upgrades/balancing).
     * @param goldPerTick New gold per tick amount
     */
    public void setGoldPerTick(int goldPerTick) {
        this.goldPerTick = Math.max(0, goldPerTick);
    }

    /**
     * Sets the production interval (for upgrades/balancing).
     * @param tickInterval New interval in seconds
     */
    public void setTickInterval(float tickInterval) {
        this.tickInterval = Math.max(0.1f, tickInterval);
    }
}