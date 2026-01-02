package com.stickwar.legacy.entities;

import com.stickwar.legacy.core.Player;

/**
 * Abstract base class for all unit types.
 * Extends Entity with combat and movement capabilities.
 */
public abstract class Unit extends Entity {
    protected int attackPower;
    protected float moveSpeed;
    protected int cost;
    protected UnitState state;
    protected float attackRange;
    protected float attackCooldown;
    protected float currentCooldown;
    protected boolean isUserControlled;

    // User Control modifiers
    protected static final float UC_DAMAGE_MULTIPLIER = 2.0f;
    protected static final float UC_SPEED_MULTIPLIER = 1.2f;

    protected Unit(float x, float y, int maxHp, int attackPower, float moveSpeed, int cost, Player owner) {
        super(x, y, maxHp, owner);
        this.attackPower = attackPower;
        this.moveSpeed = moveSpeed;
        this.cost = cost;
        this.state = UnitState.IDLE;
        this.attackRange = 50f;
        this.attackCooldown = 1.0f;
        this.currentCooldown = 0f;
        this.isUserControlled = false;
    }

    /**
     * Moves the unit based on its speed and delta time.
     *
     * @param deltaTime Time since last update
     * @param direction Direction to move (1 = right, -1 = left)
     */
    public void move(float deltaTime, int direction) {
        float speed = getEffectiveMoveSpeed();
        x += direction * speed * deltaTime;
        state = UnitState.MOVING;
    }

    /**
     * Attacks a target entity.
     *
     * @param target The entity to attack
     */
    public void attack(Entity target) {
        if (currentCooldown <= 0 && target != null) {
            int damage = getEffectiveAttackPower();
            target.takeDamage(damage);
            currentCooldown = attackCooldown;
            state = UnitState.ATTACKING;
        }
    }

    @Override
    public void update(float deltaTime) {
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }
    }

    /**
     * Returns true if the unit can currently perform an attack (cooldown ready).
     */
    public boolean isAttackReady() {
        return currentCooldown <= 0f;
    }

    /**
     * Resets the attack cooldown after an attack has been processed externally.
     * Sets the state to ATTACKING for clarity.
     */
    public void resetAttackCooldown() {
        this.currentCooldown = attackCooldown;
        this.state = UnitState.ATTACKING;
    }

    public void setUserControlled(boolean controlled) {
        this.isUserControlled = controlled;
    }

    public boolean isUserControlled() {
        return isUserControlled;
    }

    /**
     * Gets attack power with User Control modifier if active.
     */
    public int getEffectiveAttackPower() {
        return isUserControlled ? (int) (attackPower * UC_DAMAGE_MULTIPLIER) : attackPower;
    }

    /**
     * Gets move speed with User Control modifier if active.
     */
    public float getEffectiveMoveSpeed() {
        return isUserControlled ? moveSpeed * UC_SPEED_MULTIPLIER : moveSpeed;
    }

    // Getters and setters
    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getCost() {
        return cost;
    }

    public UnitState getState() {
        return state;
    }

    public void setState(UnitState state) {
        this.state = state;
    }

    public float getAttackRange() {
        return attackRange;
    }
}
