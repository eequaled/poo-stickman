package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Basic melee combat unit with sword attacks.
 * HP=100, Attack=20, Speed=100, Cost=200
 */
public class Swordwrath extends Unit {
    public static final int HP = 100;
    public static final int ATTACK = 20;
    public static final int SPEED = 100;
    public static final int COST = 200;
    public static final float MELEE_RANGE = 50f;

    public Swordwrath(Player owner) {
        this(0, 0, owner);
    }

    public Swordwrath(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = MELEE_RANGE;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Combat logic handled by CombatSystem
    }
}
