package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Heavy infantry with spear and shield.
 * HP=150, Attack=20, Speed=70, Cost=500
 * Shield reduces incoming damage by 30% (multiplier = 0.7f)
 */
public class Spearton extends Unit {
    public static final int HP = 150;
    public static final int ATTACK = 20;
    public static final int SPEED = 70;
    public static final int COST = 500;
    public static final float SPEAR_RANGE = 60f;
    public static final float DEFENSE_MULTIPLIER = 0.7f;

    private float defenseMultiplier;
    private boolean isBlocking;

    public Spearton(Player owner) {
        this(0, 0, owner);
    }

    public Spearton(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = SPEAR_RANGE;
        this.defenseMultiplier = DEFENSE_MULTIPLIER;
        this.isBlocking = true; // Shield is always active
    }

    /**
     * Overrides takeDamage to apply shield defense.
     * Reduces incoming damage by 30%.
     *
     * @param damage The incoming damage amount
     */
    @Override
    public void takeDamage(int damage) {
        int reducedDamage = (int) (damage * defenseMultiplier);
        super.takeDamage(reducedDamage);
    }

    public float getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // Combat logic handled by CombatSystem
    }
}
