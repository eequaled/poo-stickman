package com.stickwar.legacy.entities.buildings;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Building;
import com.stickwar.legacy.systems.UpgradeManager;

/**
 * Main base building - primary objective.
 * Destroying the enemy CommandCenter wins the game.
 * No production capability - purely defensive structure.
 * Can be upgraded with Gems to increase HP (+200 per level).
 */
public class CommandCenter extends Building {
    public static final int BASE_HP = 1000;

    public CommandCenter(float x, float y, Player owner) {
        this(x, y, owner, null);
    }

    public CommandCenter(float x, float y, Player owner, UpgradeManager upgradeManager) {
        super(x, y, calculateTotalHp(upgradeManager), owner);
        
        // CommandCenter has no construction cost (pre-built)
        cost.put("Gold", 0);
    }

    /**
     * Calculates total HP including upgrade bonuses.
     */
    private static int calculateTotalHp(UpgradeManager upgradeManager) {
        int totalHp = BASE_HP;
        if (upgradeManager != null) {
            totalHp += upgradeManager.getStatueBonusHp();
        }
        return totalHp;
    }

    /**
     * Updates the CommandCenter HP based on current upgrade level.
     * Should be called when upgrades are purchased.
     */
    public void applyUpgrades(UpgradeManager upgradeManager) {
        if (upgradeManager != null) {
            int newMaxHp = BASE_HP + upgradeManager.getStatueBonusHp();
            int hpDifference = newMaxHp - this.maxHp;
            
            this.maxHp = newMaxHp;
            this.hp += hpDifference; // Increase current HP by the upgrade amount
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // CommandCenter has no special abilities - just exists as target
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        // Game should check for CommandCenter destruction to trigger win/lose
    }
}