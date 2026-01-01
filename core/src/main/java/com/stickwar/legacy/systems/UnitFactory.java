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
 */
public class UnitFactory {
    private final Map<String, Integer> unitCosts;

    public UnitFactory() {
        unitCosts = new HashMap<>();
        unitCosts.put("MINER", Miner.COST);
        unitCosts.put("SWORDWRATH", Swordwrath.COST);
        unitCosts.put("ARCHIDONIS", Archidonis.COST);
        unitCosts.put("SPEARTON", Spearton.COST);
    }

    /**
     * Creates a unit of the specified type.
     * 
     * @param type Unit type string (MINER, SWORDWRATH, ARCHIDONIS, SPEARTON)
     * @param owner The player who owns this unit
     * @return New unit instance
     * @throws IllegalArgumentException if unit type is unknown
     */
    public Unit createUnit(String type, Player owner) {
        if (type == null || owner == null) {
            throw new IllegalArgumentException("Type and owner cannot be null");
        }

        switch (type.toUpperCase()) {
            case "MINER":
                return new Miner(owner);
            case "SWORDWRATH":
                return new Swordwrath(owner);
            case "ARCHIDONIS":
                return new Archidonis(owner);
            case "SPEARTON":
                return new Spearton(owner);
            default:
                throw new IllegalArgumentException("Unknown unit type: " + type);
        }
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