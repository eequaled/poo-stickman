package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Basic melee combat unit with sword.
 * HP=100, Attack=20, Speed=100, Cost=200
 * Fast and aggressive frontline fighter.
 */
public class Swordwrath extends Unit {
    public static final int HP = 100;
    public static final int ATTACK = 20;
    public static final int SPEED = 100;
    public static final int COST = 200;

    public Swordwrath(Player owner) {
        this(0, 0, owner);
    }

    public Swordwrath(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = 50f; // Melee range
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Combat logic handled by CombatSystem
    }
}