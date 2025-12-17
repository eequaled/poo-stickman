package com.stickwar.legacy.systems;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.*;

/**
 * Factory class for creating unit instances.
 */
public class UnitFactory {

    /**
     * Creates a unit of the specified type.
     *
     * @param type  The type of unit ("Miner", "Swordwrath", "Archidonis", "Spearton")
     * @param owner The player who owns the unit
     * @return The created unit
     * @throws IllegalArgumentException if unit type is unknown
     */
    public Unit createUnit(String type, Player owner) {
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
     * @param type The type of unit
     * @return The gold cost
     */
    public int getUnitCost(String type) {
        switch (type.toUpperCase()) {
            case "MINER":
                return Miner.COST;
            case "SWORDWRATH":
                return Swordwrath.COST;
            case "ARCHIDONIS":
                return Archidonis.COST;
            case "SPEARTON":
                return Spearton.COST;
            default:
                return 0;
        }
    }
}
