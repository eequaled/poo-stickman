package com.stickwar.legacy.systems;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.Archidonis;
import com.stickwar.legacy.entities.units.Miner;
import com.stickwar.legacy.entities.units.Spearton;
import com.stickwar.legacy.entities.units.Swordwrath;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating unit instances.
 * Uses Factory pattern to create units by type string.
 * Integrates with UpgradeManager to apply upgrade bonuses to newly created units.
 */
public class UnitFactory {
    private final Map<String, Integer> unitCosts;
    private UpgradeManager upgradeManager;

    public UnitFactory() {
        unitCosts = new HashMap<>();
        unitCosts.put("MINER", Miner.COST);
        unitCosts.put("SWORDWRATH", Swordwrath.COST);
        unitCosts.put("ARCHIDONIS", Archidonis.COST);
        unitCosts.put("SPEARTON", Spearton.COST);
    }

    /**
     * Sets the UpgradeManager to apply upgrade bonuses to created units.
     * 
     * @param upgradeManager The UpgradeManager instance
     */
    public void setUpgradeManager(UpgradeManager upgradeManager) {
        this.upgradeManager = upgradeManager;
    }

    /**
     * Creates a unit of the specified type.
     * Applies upgrade bonuses if UpgradeManager is set.
     * 
     * @param type Unit type string (MINER, SWORDWRATH, ARCHIDONIS, SPEARTON)
     * @param owner The player who owns this unit
     * @return New unit instance with upgrades applied
     * @throws IllegalArgumentException if unit type is unknown
     */
    public Unit createUnit(String type, Player owner) {
        if (type == null || owner == null) {
            throw new IllegalArgumentException("Type and owner cannot be null");
        }

        Unit unit;
        switch (type.toUpperCase()) {
            case "MINER":
                unit = new Miner(owner);
                break;
            case "SWORDWRATH":
                unit = new Swordwrath(owner);
                break;
            case "ARCHIDONIS":
                unit = new Archidonis(owner);
                break;
            case "SPEARTON":
                unit = new Spearton(owner);
                break;
            default:
                throw new IllegalArgumentException("Unknown unit type: " + type);
        }

        // Apply upgrade bonuses if UpgradeManager is set
        if (upgradeManager != null) {
            upgradeManager.applyUnitUpgrades(unit, type);
        }

        return unit;
    }

    /**
     * Gets the cost of a unit type.
     * 
     * @param type Unit type string
     * @return Gold cost of the unit
     * @throws IllegalArgumentException if unit type is unknown
     */
    public int getUnitCost(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        
        Integer cost = unitCosts.get(type.toUpperCase());
        if (cost == null) {
            throw new IllegalArgumentException("Unknown unit type: " + type);
        }
        return cost;
    }

    /**
     * Gets all available unit types and their costs.
     * 
     * @return Map of unit types to costs
     */
    public Map<String, Integer> getAllUnitCosts() {
        return new HashMap<>(unitCosts);
    }
}