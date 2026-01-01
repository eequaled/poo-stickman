package com.stickwar.legacy.core;

/**
 * Represents a player in the game (human or AI).
 * Contains player identification and type information.
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Player{id=" + id + ", name='" + name + "', isHuman=" + isHuman + "}";
    }
}