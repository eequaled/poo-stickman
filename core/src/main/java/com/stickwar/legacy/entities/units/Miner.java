package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Economic unit that gathers gold from mines.
 * HP=50, Attack=5, Speed=80, Cost=150
 * Can carry up to 50 gold and deposit it at base.
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
     * Collects gold from a mine or deposit.
     * @param amount Amount of gold to collect
     * @return Amount actually collected (limited by capacity)
     */
    public int collectGold(int amount) {
        int canCollect = Math.min(amount, goldCapacity - carriedGold);
        carriedGold += canCollect;
        return canCollect;
    }

    /**
     * Deposits all carried gold.
     * @return Amount of gold deposited
     */
    public int depositGold() {
        int deposited = carriedGold;
        carriedGold = 0;
        return deposited;
    }

    /**
     * Checks if the miner can carry more gold.
     */
    public boolean canCarryMore() {
        return carriedGold < goldCapacity;
    }

    /**
     * Checks if the miner is carrying any gold.
     */
    public boolean isCarryingGold() {
        return carriedGold > 0;
    }

    public int getCarriedGold() {
        return carriedGold;
    }

    public int getGoldCapacity() {
        return goldCapacity;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Mining logic handled by game systems
    }
}