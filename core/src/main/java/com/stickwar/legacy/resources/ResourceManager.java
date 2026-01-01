package com.stickwar.legacy.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages player resources using Map<String, Integer>.
 * Handles gold economy for unit production and upgrades.
 */
public class ResourceManager {
    public static final int DEFAULT_STARTING_GOLD = 200;
    
    private final Map<String, Integer> resources;

    public ResourceManager() {
        this(DEFAULT_STARTING_GOLD);
    }

    public ResourceManager(int startingGold) {
        resources = new HashMap<>();
        resources.put("Gold", startingGold);
    }

    /**
     * Adds gold to the player's balance.
     * @param amount Amount of gold to add (must be non-negative)
     */
    public void addGold(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative gold: " + amount);
        }
        int current = resources.get("Gold");
        resources.put("Gold", current + amount);
    }

    /**
     * Attempts to spend gold from the player's balance.
     * @param amount Amount of gold to spend
     * @return true if successful, false if insufficient funds
     */
    public boolean spendGold(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot spend negative gold: " + amount);
        }
        int current = resources.get("Gold");
        if (current >= amount) {
            resources.put("Gold", current - amount);
            return true;
        }
        return false;
    }

    /**
     * Gets the current gold balance.
     * @return Current gold amount
     */
    public int getGold() {
        return resources.get("Gold");
    }

    /**
     * Checks if the player can afford a certain cost.
     * @param cost The cost to check
     * @return true if affordable, false otherwise
     */
    public boolean canAfford(int cost) {
        return getGold() >= cost;
    }

    /**
     * Sets the gold amount directly (for testing).
     * @param amount New gold amount
     */
    public void setGold(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Gold cannot be negative: " + amount);
        }
        resources.put("Gold", amount);
    }

    /**
     * Resets resources to default starting values.
     */
    public void reset() {
        resources.put("Gold", DEFAULT_STARTING_GOLD);
    }

    /**
     * Gets all resources (for debugging/display).
     * @return Copy of the resources map
     */
    public Map<String, Integer> getAllResources() {
        return new HashMap<>(resources);
    }
}