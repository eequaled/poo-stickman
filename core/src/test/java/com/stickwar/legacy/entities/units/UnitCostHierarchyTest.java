package com.stickwar.legacy.entities.units;

import com.stickwar.legacy.core.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for unit cost hierarchy.
 * **Feature: stick-war-legacy-clone, Property 2: Unit Cost Hierarchy**
 */
class UnitCostHierarchyTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 2: Unit Cost Hierarchy**
     * For any two unit types A and B where A appears before B in the ordering
     * (Miner, Swordwrath, Archidonis, Spearton), the cost of A should be strictly less than the cost of B.
     * **Validates: Requirements 13.1**
     */
    @Test
    void unitCostHierarchyIsCorrect() {
        Player player = new Player(1, "Test", true);
        
        Miner miner = new Miner(player);
        Swordwrath swordwrath = new Swordwrath(player);
        Archidonis archidonis = new Archidonis(player);
        Spearton spearton = new Spearton(player);
        
        // Miner < Swordwrath
        assertTrue(miner.getCost() < swordwrath.getCost(),
            "Miner cost (" + miner.getCost() + ") should be less than Swordwrath cost (" + swordwrath.getCost() + ")");
        
        // Swordwrath < Archidonis
        assertTrue(swordwrath.getCost() < archidonis.getCost(),
            "Swordwrath cost (" + swordwrath.getCost() + ") should be less than Archidonis cost (" + archidonis.getCost() + ")");
        
        // Archidonis < Spearton
        assertTrue(archidonis.getCost() < spearton.getCost(),
            "Archidonis cost (" + archidonis.getCost() + ") should be less than Spearton cost (" + spearton.getCost() + ")");
    }

    @Test
    void minerIsLowestCostUnit() {
        assertEquals(150, Miner.COST, "Miner cost should be 150");
    }

    @Test
    void swordwrathIsLowestCombatUnitCost() {
        assertTrue(Swordwrath.COST < Archidonis.COST,
            "Swordwrath should be the lowest cost combat unit");
        assertTrue(Swordwrath.COST < Spearton.COST,
            "Swordwrath should be the lowest cost combat unit");
    }

    @Test
    void unitCostsMatchDesignSpec() {
        assertEquals(150, Miner.COST, "Miner cost should be 150");
        assertEquals(200, Swordwrath.COST, "Swordwrath cost should be 200");
        assertEquals(350, Archidonis.COST, "Archidonis cost should be 350");
        assertEquals(500, Spearton.COST, "Spearton cost should be 500");
    }
}
