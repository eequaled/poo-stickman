package com.stickwar.legacy.systems;

import com.stickwar.legacy.entities.Entity;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.Spearton;

import java.util.Random;

/**
 * Combat system with random damage variance.
 * Requirement: PDF 4.5 - prise en compte d'aléatoire
 */
public class CombatSystem {
    private Random random;

    public CombatSystem() {
        this.random = new Random();
    }

    /**
     * Constructor with seed for testing.
     */
    public CombatSystem(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Calculates damage with random variance.
     * Damage = base + random(0-5)
     *
     * @param attacker The attacking unit
     * @param target   The target entity
     * @return Calculated damage with variance
     */
    public int calculateDamage(Unit attacker, Entity target) {
        int baseDamage = attacker.getEffectiveAttackPower();
        // Add random variance: base + random(0-5)
        int damage = baseDamage + random.nextInt(6);

        // Apply Spearton shield defense (30% reduction)
        if (target instanceof Spearton) {
            Spearton spearton = (Spearton) target;
            damage = (int) (damage * spearton.getDefenseMultiplier()); // 0.7f
        }

        return damage;
    }

    /**
     * Calculates raw damage without target-specific modifiers.
     * Used for testing damage variance property.
     *
     * @param baseDamage The base damage value
     * @return Damage with random variance [baseDamage, baseDamage + 5]
     */
    public int calculateRawDamage(int baseDamage) {
        return baseDamage + random.nextInt(6);
    }

    /**
     * Processes an attack from attacker to target.
     *
     * @param attacker The attacking unit
     * @param target   The target entity
     */
    public void processAttack(Unit attacker, Entity target) {
        if (attacker == null || target == null) {
            return;
        }
        int damage = calculateDamage(attacker, target);
        target.takeDamage(damage);
    }

    /**
     * Checks if attacker is in range of target.
     */
    public boolean isInRange(Unit attacker, Entity target) {
        float dx = target.getX() - attacker.getX();
        float dy = target.getY() - attacker.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= attacker.getAttackRange();
    }
}
