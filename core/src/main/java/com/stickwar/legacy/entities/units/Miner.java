package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.UnitState;
import com.stickwar.legacy.resources.ResourceManager;

/**
 * Miner unit that gathers gold from mines and deposits it at the statue.
 * HP=50, Attack=5, Speed=80, Cost=150
 */
public class Miner extends Unit {
    public static final int HP = 50;
    public static final int ATTACK = 5;
    public static final int SPEED = 80;
    public static final int COST = 150;
    public static final int GOLD_CAPACITY = 50;

    private int carriedGold;
    private int goldCapacity;

    public Miner(Player owner) {
        this(0, 0, owner);
    }

    public Miner(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.goldCapacity = GOLD_CAPACITY;
        this.carriedGold = 0;
    }

    /**
     * Collects gold from a mine up to carrying capacity.
     *
     * @param availableGold The amount of gold available at the mine
     * @return The amount of gold actually collected
     */
    public int collectGold(int availableGold) {
        int spaceAvailable = goldCapacity - carriedGold;
        int collected = Math.min(spaceAvailable, availableGold);
        carriedGold += collected;
        if (carriedGold > 0) {
            state = UnitState.RETURNING;
        }
        return collected;
    }

    /**
     * Deposits carried gold into the ResourceManager.
     *
     * @param resources The ResourceManager to deposit gold into
     * @return The amount of gold deposited
     */
    public int depositGold(ResourceManager resources) {
        int deposited = carriedGold;
        if (deposited > 0) {
            resources.addGold(deposited);
            carriedGold = 0;
            state = UnitState.MINING;
        }
        return deposited;
    }

    public int getCarriedGold() {
        return carriedGold;
    }

    public int getGoldCapacity() {
        return goldCapacity;
    }

    public boolean isCarryingGold() {
        return carriedGold > 0;
    }

    public boolean isAtCapacity() {
        return carriedGold >= goldCapacity;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Mining/returning logic handled by game systems
    }
}
