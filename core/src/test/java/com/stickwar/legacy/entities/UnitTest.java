package com.stickwar.legacy.entities;

import com.stickwar.legacy.core.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Unit movement and User Control modifiers.
 */
class UnitTest {

    /**
     * Concrete test unit for testing abstract Unit class.
     */
    static class TestUnit extends Unit {
        public TestUnit(float x, float y, int maxHp, int attackPower, float moveSpeed, int cost, Player owner) {
            super(x, y, maxHp, attackPower, moveSpeed, cost, owner);
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
        }
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 3: Unit Movement Calculation**
     * For any Unit with moveSpeed > 0, any direction (-1 or 1), and any deltaTime > 0,
     * calling move(deltaTime, direction) should change x position by exactly
     * (direction × moveSpeed × deltaTime) when not user-controlled, or
     * (direction × moveSpeed × 1.2 × deltaTime) when user-controlled.
     * **Validates: Requirements 3.2, 3.5**
     */
    @RepeatedTest(100)
    void unitMovementCalculation() {
        Random random = new Random();
        float initialX = random.nextFloat() * 1000;
        float moveSpeed = random.nextFloat() * 100 + 1; // 1-101
        float deltaTime = random.nextFloat() * 0.1f + 0.01f; // 0.01-0.11
        int direction = random.nextBoolean() ? 1 : -1;
        boolean isUserControlled = random.nextBoolean();

        TestUnit unit = new TestUnit(initialX, 0, 100, 10, moveSpeed, 100, null);
        unit.setUserControlled(isUserControlled);

        float expectedSpeed = isUserControlled ? moveSpeed * 1.2f : moveSpeed;
        float expectedX = initialX + (direction * expectedSpeed * deltaTime);

        unit.move(deltaTime, direction);

        assertEquals(expectedX, unit.getX(), 0.001f,
            "Unit position should change by (direction × effective_speed × deltaTime)");
        assertEquals(UnitState.MOVING, unit.getState(),
            "Unit state should be MOVING after move() call");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 4: User Control Attack Modifier**
     * For any Unit with attackPower > 0, when isUserControlled is true,
     * getEffectiveAttackPower() should return exactly (attackPower × 2.0).
     * **Validates: Requirements 3.4**
     */
    @RepeatedTest(100)
    void userControlAttackModifier() {
        Random random = new Random();
        int attackPower = random.nextInt(100) + 1; // 1-100
        boolean isUserControlled = random.nextBoolean();

        TestUnit unit = new TestUnit(0, 0, 100, attackPower, 50, 100, null);
        unit.setUserControlled(isUserControlled);

        int expectedAttack = isUserControlled ? (int)(attackPower * 2.0f) : attackPower;
        int actualAttack = unit.getEffectiveAttackPower();

        assertEquals(expectedAttack, actualAttack,
            "Effective attack power should be doubled when user-controlled");
    }

    /**
     * Test that User Control speed modifier works correctly.
     */
    @RepeatedTest(100)
    void userControlSpeedModifier() {
        Random random = new Random();
        float moveSpeed = random.nextFloat() * 100 + 1; // 1-101
        boolean isUserControlled = random.nextBoolean();

        TestUnit unit = new TestUnit(0, 0, 100, 10, moveSpeed, 100, null);
        unit.setUserControlled(isUserControlled);

        float expectedSpeed = isUserControlled ? moveSpeed * 1.2f : moveSpeed;
        float actualSpeed = unit.getEffectiveMoveSpeed();

        assertEquals(expectedSpeed, actualSpeed, 0.001f,
            "Effective move speed should be 1.2x when user-controlled");
    }

    @Test
    void unitInitializesWithCorrectValues() {
        Player player = new Player(1, "Test", true);
        TestUnit unit = new TestUnit(10, 20, 100, 25, 80, 150, player);

        assertEquals(10, unit.getX());
        assertEquals(20, unit.getY());
        assertEquals(100, unit.getHp());
        assertEquals(100, unit.getMaxHp());
        assertEquals(25, unit.getAttackPower());
        assertEquals(80, unit.getMoveSpeed());
        assertEquals(150, unit.getCost());
        assertEquals(player, unit.getOwner());
        assertEquals(UnitState.IDLE, unit.getState());
        assertFalse(unit.isUserControlled());
    }

    @Test
    void unitAttackCooldownWorks() {
        TestUnit unit = new TestUnit(0, 0, 100, 10, 50, 100, null);
        
        assertTrue(unit.isAttackReady(), "Unit should be ready to attack initially");
        
        unit.resetAttackCooldown();
        assertFalse(unit.isAttackReady(), "Unit should not be ready after cooldown reset");
        assertEquals(UnitState.ATTACKING, unit.getState(), "State should be ATTACKING");
        
        // Simulate time passing
        unit.update(2.0f); // More than 1.0f cooldown
        assertTrue(unit.isAttackReady(), "Unit should be ready after cooldown expires");
    }
}