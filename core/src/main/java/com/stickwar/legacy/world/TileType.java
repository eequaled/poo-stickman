package com.stickwar.legacy.world;

/**
 * Represents different terrain types on the game map.
 * Each type has different properties for gameplay.
 */
public enum TileType {
    GRASS,      // Normal terrain - accessible, no bonuses
    MOUNTAIN,   // Impassable terrain with defense bonus
    WATER       // Impassable terrain (unless bridge built)
}