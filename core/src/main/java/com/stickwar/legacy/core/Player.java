package com.stickwar.legacy.core;

/**
 * Represents a player in the game (human or AI).
 */
public class Player {
    private final int id;
    private final String name;
    private final boolean isHuman;

    public Player(int id, String name, boolean isHuman) {
        this.id = id;
        this.name = name;
        this.isHuman = isHuman;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return isHuman;
    }
}
