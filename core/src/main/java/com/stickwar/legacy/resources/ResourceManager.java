package com.stickwar.legacy.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages game resources using Map<String, Integer>.
 * Primary resource is "Gold".
 */
public class ResourceManager {
    public static final String GOLD = "Gold";
    public static final int DEFAULT_STARTING_GOLD = 200;

    private Map<String, Integer> resources;

    public ResourceManager() {
        this(DEFAULT_STARTING_GOLD);
    }

    public ResourceManager(int startingGold) {
        resources = new HashMap<>();
        resources.put(GOLD, startingGold);
    }

    /**
     * Adds gold to the player's balance.
     *
     * @param amount The amount of gold to add (must be non-negative)
     */
    public void addGold(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        resources.put(GOLD, resources.get(GOLD) + amount);
    }

    /**
     * Attempts to spend gold from the player's balance.
     *
     * @param amount The amount of gold to spend
     * @return true if successful, false if insufficient balance
     */
    public boolean spendGold(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        int current = resources.get(GOLD);
        if (current >= amount) {
            resources.put(GOLD, current - amount);
            return true;
        }
        return false;
    }

    /**
     * Gets the current gold balance.
     *
     * @return The current gold amount
     */
    public int getGold() {
        return resources.get(GOLD);
    }

    /**
     * Checks if the player can afford a cost.
     *
     * @param cost The cost to check
     * @return true if gold >= cost
     */
    public boolean canAfford(int cost) {
        return resources.get(GOLD) >= cost;
    }

    /**
     * Resets gold to starting amount.
     */
    public void reset() {
        resources.put(GOLD, DEFAULT_STARTING_GOLD);
    }

    /**
     * Sets gold to a specific amount (for testing/initialization).
     */
    public void setGold(int amount) {
        resources.put(GOLD, amount);
    }
}
