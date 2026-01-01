package com.stickwar.legacy.entities;

/**
 * Represents the current state of a unit.
 * Used for AI behavior and animation control.
 */
public enum UnitState {
    IDLE,       // Standing still, no action
    MOVING,     // Moving to a destination
    ATTACKING,  // Currently attacking a target
    MINING,     // Gathering resources (Miner only)
    RETURNING,  // Returning to base (after mining)
    DEAD        // Unit has been destroyed
}