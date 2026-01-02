package com.stickwar.legacy.entities.units;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for unit cost hierarchy.
 */
class UnitCostHierarchyTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 11: Unit Cost Hierarchy**
     * For any two unit types A and B where A appears before B in the ordering
     * (Miner, Swordwrath, Archidonis, Spearton), the cost of A should be
     * strictly less than the cost of B.
     * **Validates: Requirements 22.1**
     */
    @Test
    void unitCostHierarchy() {
        // Verify the exact hierarchy: Miner < Swordwrath < Archidonis < Spearton
        assertTrue(Miner.COST < Swordwrath.COST,
            "Miner cost should be less than Swordwrath cost");
        assertTrue(Swordwrath.COST < Archidonis.COST,
            "Swordwrath cost should be less than Archidonis cost");
        assertTrue(Archidonis.COST < Spearton.COST,
            "Archidonis cost should be less than Spearton cost");
        
        // Verify transitive relationships
        assertTrue(Miner.COST < Archidonis.COST,
            "Miner cost should be less than Archidonis cost");
        assertTrue(Miner.COST < Spearton.COST,
            "Miner cost should be less than Spearton cost");
        assertTrue(Swordwrath.COST < Spearton.COST,
            "Swordwrath cost should be less than Spearton cost");
    }

    /**
     * Test that unit costs match the expected values from requirements.
     */
    @Test
    void unitCostsMatchRequirements() {
        assertEquals(150, Miner.COST, "Miner cost should be 150");
        assertEquals(200, Swordwrath.COST, "Swordwrath cost should be 200");
        assertEquals(350, Archidonis.COST, "Archidonis cost should be 350");
        assertEquals(500, Spearton.COST, "Spearton cost should be 500");
    }

    /**
     * Test that all unit costs are positive.
     */
    @Test
    void allUnitCostsArePositive() {
        assertTrue(Miner.COST > 0, "Miner cost should be positive");
        assertTrue(Swordwrath.COST > 0, "Swordwrath cost should be positive");
        assertTrue(Archidonis.COST > 0, "Archidonis cost should be positive");
        assertTrue(Spearton.COST > 0, "Spearton cost should be positive");
    }

    /**
     * Test that unit costs increase significantly between tiers.
     */
    @Test
    void unitCostIncreasesBetweenTiers() {
        // Each tier should be meaningfully more expensive
        int minerToSwordwrath = Swordwrath.COST - Miner.COST;
        int swordwrathToArchidonis = Archidonis.COST - Swordwrath.COST;
        int archidonisToSpearton = Spearton.COST - Archidonis.COST;
        
        assertTrue(minerToSwordwrath >= 50,
            "Swordwrath should cost at least 50 more than Miner");
        assertTrue(swordwrathToArchidonis >= 100,
            "Archidonis should cost at least 100 more than Swordwrath");
        assertTrue(archidonisToSpearton >= 100,
            "Spearton should cost at least 100 more than Archidonis");
    }
}