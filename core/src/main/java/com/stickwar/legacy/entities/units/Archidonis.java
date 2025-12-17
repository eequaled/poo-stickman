package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Ranged archer unit that fires arrow projectiles.
 * HP=80, Attack=15, Speed=90, Cost=350
 */
public class Archidonis extends Unit {
    public static final int HP = 80;
    public static final int ATTACK = 15;
    public static final int SPEED = 90;
    public static final int COST = 350;
    public static final float RANGED_ATTACK_RANGE = 200f;

    public Archidonis(Player owner) {
        this(0, 0, owner);
    }

    public Archidonis(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = RANGED_ATTACK_RANGE;
    }

    /**
     * Archidonis can shoot over water tiles.
     */
    public boolean canShootOverWater() {
        return true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Ranged combat logic handled by CombatSystem
    }
}
