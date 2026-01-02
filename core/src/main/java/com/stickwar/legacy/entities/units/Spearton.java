package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

/**
 * Heavy infantry with spear and shield.
 * HP=150, Attack=20, Speed=70, Cost=500
 * Shield reduces incoming damage by 30% (multiplier = 0.7f)
 * Special mechanics:
 * - Can throw spear once (penetrates all units, 20% statue damage)
 * - After throwing spear, switches to dagger with Swordwrath damage (20)
 * - Has block chance when upgraded
 */
public class Spearton extends Unit {
    public static final int HP = 150;
    public static final int ATTACK = 20;
    public static final int SPEED = 70;
    public static final int COST = 500;
    public static final float SPEAR_RANGE = 60f;
    public static final float DEFENSE_MULTIPLIER = 0.7f;
    public static final float SPEAR_THROW_COOLDOWN = 5.0f; // 5 second cooldown for spear throw
    public static final int DAGGER_DAMAGE = 20; // Same as Swordwrath

    private float defenseMultiplier;
    private boolean isBlocking;
    private boolean hasSpear; // True if still has spear, false if using dagger
    private float spearThrowCooldown;
    private float blockChance; // Chance to block attacks (0.0 to 1.0)

    public Spearton(Player owner) {
        this(0, 0, owner);
    }

    public Spearton(float x, float y, Player owner) {
        super(x, y, HP, ATTACK, SPEED, COST, owner);
        this.attackRange = SPEAR_RANGE;
        this.defenseMultiplier = DEFENSE_MULTIPLIER;
        this.isBlocking = true; // Shield is always active
        this.hasSpear = true; // Starts with spear
        this.spearThrowCooldown = 0f;
        this.blockChance = 0.0f; // No block chance initially, increased by upgrades
    }

    /**
     * Overrides takeDamage to apply shield defense and block chance.
     * Reduces incoming damage by 30% and has a chance to block completely.
     *
     * @param damage The incoming damage amount
     */
    @Override
    public void takeDamage(int damage) {
        // Check for block first
        if (blockChance > 0 && Math.random() < blockChance) {
            // Attack blocked completely
            return;
        }
        
        // Apply shield defense
        int reducedDamage = (int) (damage * defenseMultiplier);
        super.takeDamage(reducedDamage);
    }

    /**
     * Throws the spear if available and on cooldown.
     * After throwing, switches to dagger mode.
     */
    public boolean throwSpear() {
        if (hasSpear && spearThrowCooldown <= 0) {
            hasSpear = false; // Switch to dagger
            spearThrowCooldown = SPEAR_THROW_COOLDOWN;
            // Reduce attack power to dagger level
            this.attackPower = DAGGER_DAMAGE;
            return true;
        }
        return false;
    }

    /**
     * Gets the current weapon type.
     */
    public String getWeaponType() {
        return hasSpear ? "Spear" : "Dagger";
    }

    /**
     * Checks if spear throw is available.
     */
    public boolean canThrowSpear() {
        return hasSpear && spearThrowCooldown <= 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Update spear throw cooldown
        if (spearThrowCooldown > 0) {
            spearThrowCooldown -= deltaTime;
        }
    }

    // Getters and setters
    public float getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }

    public boolean hasSpear() {
        return hasSpear;
    }

    public float getSpearThrowCooldown() {
        return spearThrowCooldown;
    }

    public float getBlockChance() {
        return blockChance;
    }

    public void setBlockChance(float blockChance) {
        this.blockChance = Math.max(0f, Math.min(1f, blockChance)); // Clamp between 0 and 1
    }

    /**
     * Increases block chance by the specified amount (used by upgrade system).
     */
    public void increaseBlockChance(float increase) {
        setBlockChance(this.blockChance + increase);
    }
}
