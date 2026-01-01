package com.stickwar.legacy.systems;

import com.stickwar.legacy.entities.Entity;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.Spearton;

import java.util.Random;

/**
 * Combat system with random damage variance.
 * Requirement: PDF 4.5 - prise en compte d'aléatoire
 * 
 * Handles damage calculation, range checking, and special unit abilities.
 */
public class CombatSystem {
    private final Random random;

    public CombatSystem() {
        this.random = new Random();
    }

    /**
     * Constructor with seed for testing.
     * @param seed Random seed for reproducible tests
     */
    public CombatSystem(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Calculates raw damage with variance (for testing).
     * Base damage + random(0-5) without target modifiers.
     * 
     * @param baseDamage Base damage amount
     * @return Damage with variance applied
     */
    public int calculateRawDamage(int baseDamage) {
        return baseDamage + random.nextInt(6);
    }

    /**
     * Calculates damage with random variance.
     * Base damage + random(0-5) with target-specific modifiers.
     * 
     * @param attacker The attacking unit
     * @param target The target entity
     * @return Calculated damage with variance and modifiers
     */
    public int calculateDamage(Unit attacker, Entity target) {
        int baseDamage = attacker.getEffectiveAttackPower();
        
        // Add random variance: base + random(0-5)
        int damage = baseDamage + random.nextInt(6);
        
        // Apply Spearton shield defense (30% reduction)
        if (target instanceof Spearton) {
            Spearton spearton = (Spearton) target;
            damage = (int)(damage * spearton.getDefenseMultiplier()); // 0.7f
        }
        
        return Math.max(0, damage); // Ensure non-negative damage
    }

    /**
     * Processes a complete attack between attacker and target.
     * Calculates damage and applies it to the target.
     * 
     * @param attacker The attacking unit
     * @param target The target entity
     */
    public void processAttack(Unit attacker, Entity target) {
        if (attacker == null || target == null) {
            return;
        }
        
        if (target.isMarkedForRemoval()) {
            return; // Don't attack dead targets
        }
        
        int damage = calculateDamage(attacker, target);
        target.takeDamage(damage);
    }

    /**
     * Checks if an attacker is within range of a target.
     * Uses Euclidean distance calculation.
     * 
     * @param attacker The attacking unit
     * @param target The target entity
     * @return true if target is within attack range
     */
    public boolean isInRange(Unit attacker, Entity target) {
        if (attacker == null || target == null) {
            return false;
        }
        
        float dx = target.getX() - attacker.getX();
        float dy = target.getY() - attacker.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        return distance <= attacker.getAttackRange();
    }

    /**
     * Gets the random instance (for testing).
     */
    public Random getRandom() {
        return random;
    }
}