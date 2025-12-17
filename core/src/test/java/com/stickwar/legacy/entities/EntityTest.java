package com.stickwar.legacy.entities;

import com.stickwar.legacy.core.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for Entity damage and death behavior.
 * **Feature: stick-war-legacy-clone, Property 1: Entity Damage and Death Consistency**
 */
class EntityTest {

    /**
     * Concrete test entity for testing abstract Entity class.
     */
    static class TestEntity extends Entity {
        public TestEntity(int maxHp) {
            super(0, 0, maxHp, null);
        }

        public TestEntity(float x, float y, int maxHp, Player owner) {
            super(x, y, maxHp, owner);
        }

        @Override
        public void update(float deltaTime) {
            // No-op for testing
        }
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 1: Entity Damage and Death Consistency**
     * For any Entity with initial hp > 0 and any damage amount, calling takeDamage(damage)
     * should reduce hp by exactly the damage amount, and if the resulting hp <= 0,
     * the entity should be marked for removal.
     * **Validates: Requirements 6.2, 6.3**
     */
    @RepeatedTest(100)
    void entityDamageReducesHpCorrectly() {
        Random random = new Random();
        int initialHp = random.nextInt(1000) + 1;  // 1-1000
        int damage = random.nextInt(500);           // 0-499

        TestEntity entity = new TestEntity(initialHp);
        int expectedHp = initialHp - damage;
        
        entity.takeDamage(damage);

        assertEquals(expectedHp, entity.getHp(),
            "HP should be reduced by exactly the damage amount");
        
        if (expectedHp <= 0) {
            assertTrue(entity.isMarkedForRemoval(),
                "Entity should be marked for removal when HP <= 0");
        } else {
            assertFalse(entity.isMarkedForRemoval(),
                "Entity should not be marked for removal when HP > 0");
        }
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 1: Entity Damage and Death Consistency**
     * Edge case: Entity dies exactly when HP reaches 0.
     * **Validates: Requirements 6.2, 6.3**
     */
    @RepeatedTest(100)
    void entityDiesWhenHpReachesZero() {
        Random random = new Random();
        int initialHp = random.nextInt(1000) + 1;  // 1-1000

        TestEntity entity = new TestEntity(initialHp);
        
        // Deal exact damage to reach 0
        entity.takeDamage(initialHp);

        assertEquals(0, entity.getHp(), "HP should be exactly 0");
        assertTrue(entity.isMarkedForRemoval(),
            "Entity should be marked for removal when HP = 0");
    }

    /**
     * **Feature: stick-war-legacy-clone, Property 1: Entity Damage and Death Consistency**
     * Edge case: Multiple damage applications accumulate correctly.
     * **Validates: Requirements 6.2, 6.3**
     */
    @RepeatedTest(100)
    void multipleDamageApplicationsAccumulate() {
        Random random = new Random();
        int initialHp = random.nextInt(500) + 500;  // 500-999
        int damage1 = random.nextInt(100);           // 0-99
        int damage2 = random.nextInt(100);           // 0-99

        TestEntity entity = new TestEntity(initialHp);
        int expectedHp = initialHp - damage1 - damage2;
        
        entity.takeDamage(damage1);
        entity.takeDamage(damage2);

        assertEquals(expectedHp, entity.getHp(),
            "HP should reflect cumulative damage");
    }

    @Test
    void entityInitializesWithCorrectValues() {
        Player player = new Player(1, "Test", true);
        TestEntity entity = new TestEntity(10, 20, 100, player);

        assertEquals(10, entity.getX());
        assertEquals(20, entity.getY());
        assertEquals(100, entity.getHp());
        assertEquals(100, entity.getMaxHp());
        assertEquals(player, entity.getOwner());
        assertFalse(entity.isMarkedForRemoval());
    }
}
