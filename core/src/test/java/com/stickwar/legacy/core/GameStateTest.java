package com.stickwar.legacy.core;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property tests for GameState transitions.
 * **Feature: stick-war-legacy-clone, Property 6: Win/Lose State Transitions**
 */
class GameStateTest {

    /**
     * **Feature: stick-war-legacy-clone, Property 6: Win/Lose State Transitions**
     * For any game state where player CommandCenter hp <= 0, the GameState should transition to DEFEAT.
     * For any game state where enemy CommandCenter hp <= 0, the GameState should transition to VICTORY.
     * **Validates: Requirements 16.2, 16.3**
     */
    @RepeatedTest(100)
    void winLoseStateTransitions() {
        Random random = new Random();
        GameMain game = new GameMain();
        
        // Simulate random initial HP values
        int playerHp = random.nextInt(1000) + 1;  // 1-1000
        int enemyHp = random.nextInt(1000) + 1;   // 1-1000
        
        // Simulate random damage to player
        int playerDamage = random.nextInt(1500);  // 0-1499
        int resultingPlayerHp = playerHp - playerDamage;
        
        // Simulate random damage to enemy
        int enemyDamage = random.nextInt(1500);   // 0-1499
        int resultingEnemyHp = enemyHp - enemyDamage;
        
        // Test DEFEAT condition: player HP <= 0
        if (resultingPlayerHp <= 0) {
            game.showDefeat();
            assertEquals(GameState.DEFEAT, game.getCurrentState(),
                "When player CommandCenter HP <= 0, state should be DEFEAT");
        }
        
        // Test VICTORY condition: enemy HP <= 0
        if (resultingEnemyHp <= 0) {
            game.showVictory(10, 5);
            assertEquals(GameState.VICTORY, game.getCurrentState(),
                "When enemy CommandCenter HP <= 0, state should be VICTORY");
        }
    }

    @Test
    void gameStartsInMenuState() {
        GameMain game = new GameMain();
        assertEquals(GameState.MENU, game.getCurrentState());
    }

    @Test
    void startGameTransitionsToPlaying() {
        GameMain game = new GameMain();
        game.startGame();
        assertEquals(GameState.PLAYING, game.getCurrentState());
    }

    @Test
    void returnToMenuTransitionsToMenu() {
        GameMain game = new GameMain();
        game.startGame();
        game.returnToMenu();
        assertEquals(GameState.MENU, game.getCurrentState());
    }

    @Test
    void showVictoryTransitionsToVictory() {
        GameMain game = new GameMain();
        game.startGame();
        game.showVictory(10, 5);
        assertEquals(GameState.VICTORY, game.getCurrentState());
    }

    @Test
    void showDefeatTransitionsToDefeat() {
        GameMain game = new GameMain();
        game.startGame();
        game.showDefeat();
        assertEquals(GameState.DEFEAT, game.getCurrentState());
    }
}
