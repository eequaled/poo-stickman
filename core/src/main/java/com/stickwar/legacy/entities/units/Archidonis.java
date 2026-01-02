package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Ranged archer unit with bow and arrows.
 * HP=80, Attack=15, Speed=90, Cost=350
 * Attacks from distance with projectiles.
 * Has cooldown for arrow shooting to prevent spam.
 */
public class Archidonis extends Unit {
    public static final int HP = 80;
    public static final int ATTACK = 15;
    public static final int SPEED = 90;
    public static final int COST = 350;
    public static final float BOW_RANGE = 200f; // Increased range for better positioning
    public static final float ARROW_COOLDOWN = 1.5f; // 1.5 second cooldown between arrows

    private float arrowCooldown;

    public Archidonis(Player owner) {
        this(0, 0, owner);
    }

    public Archidonis(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = BOW_RANGE; // Longer range than melee
        this.arrowCooldown = 0f;
        this.attackCooldown = ARROW_COOLDOWN; // Override base cooldown
    }

    /**
     * Shoots an arrow if cooldown is ready.
     */
    public boolean shootArrow() {
        if (arrowCooldown <= 0) {
            arrowCooldown = ARROW_COOLDOWN;
            return true;
        }
        return false;
    }

    /**
     * Checks if arrow shooting is available.
     */
    public boolean canShootArrow() {
        return arrowCooldown <= 0;
    }

    /**
     * Archidonis can shoot over water tiles and obstacles.
     */
    public boolean canShootOverObstacles() {
        return true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Update arrow cooldown
        if (arrowCooldown > 0) {
            arrowCooldown -= deltaTime;
        }
    }

    public float getArrowCooldown() {
        return arrowCooldown;
    }
}