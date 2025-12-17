package com.stickwar.legacy.world;

import com.stickwar.legacy.core.Player;

/**
 * Represents a single tile on the game map.
 * Contains position, terrain type, and ownership information.
 */
public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private Player owner;
    private boolean isAccessible;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.owner = null;
        this.isAccessible = (type == TileType.GRASS);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
        this.isAccessible = (type == TileType.GRASS);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        this.isAccessible = accessible;
    }
}
