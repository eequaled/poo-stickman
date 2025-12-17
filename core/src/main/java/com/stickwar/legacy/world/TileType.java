package com.stickwar.legacy.world;

/**
 * Represents the type of terrain for a tile.
 */
public enum TileType {
    GRASS,      // Normal terrain, passable
    MOUNTAIN,   // Impassable terrain
    WATER       // Impassable (unless bridge owned)
}
