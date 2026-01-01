package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Ranged archer unit with bow and arrows.
 * HP=80, Attack=15, Speed=90, Cost=350
 * Attacks from distance with projectiles.
 */
public class Archidonis extends Unit {
    public static final int HP = 80;
    public static final int ATTACK = 15;
    public static final int SPEED = 90;
    public static final int COST = 350;
    public static final float BOW_RANGE = 120f;

    public Archidonis(Player owner) {
        this(0, 0, owner);
    }

    public Archidonis(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = BOW_RANGE; // Longer range than melee
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Combat logic handled by CombatSystem
    }
}