package com.stickwar.legacy.entities;

import com.stickwar.legacy.core.Player;

/**
 * Abstract base class for all game entities (units and buildings).
 * Contains common properties like position, health, and ownership.
 */
public abstract class Entity {
    private static int nextId = 0;
    
    protected int id;
    protected float x;
    protected float y;
    protected int hp;
    protected int maxHp;
    protected Player owner;
    protected boolean markedForRemoval;

    protected Entity(float x, float y, int maxHp, Player owner) {
        this.id = nextId++;
        this.x = x;
        this.y = y;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.owner = owner;
        this.markedForRemoval = false;
    }

    /**
     * Reduces entity HP by the damage amount.
     * If HP reaches 0 or below, triggers death behavior.
     *
     * @param damage The amount of damage to apply
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            onDeath();
            markedForRemoval = true;
        }
    }

    /**
     * Called when the entity dies (HP <= 0).
     * Subclasses can override for custom death behavior.
     */
    protected void onDeath() {
        // Default implementation - can be overridden
    }

    /**
     * Updates the entity state each frame.
     *
     * @param deltaTime Time since last update in seconds
     */
    public abstract void update(float deltaTime);

    // Getters and setters
    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }
}
