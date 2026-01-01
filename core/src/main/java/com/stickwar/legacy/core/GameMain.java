package com.stickwar.legacy.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.stickwar.legacy.screens.MainMenuScreen;

/**
 * Main game class extending LibGDX's Game class.
 * Entry point for the Stick War Legacy clone.
 * Manages screen transitions and global game state.
 */
public class GameMain extends Game {
    private GameState currentState = GameState.MENU;
    
    // Reward tracking
    private int totalGems = 0;
    private int totalUpgradePoints = 0;

    @Override
    public void create() {
        // Initial screen
        setScreen(new MainMenuScreen(this));
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    public void startGame() {
        currentState = GameState.PLAYING;
    }

    public void returnToMenu() {
        currentState = GameState.MENU;
    }

    public void showVictory(int gemsEarned, int upgradePointsEarned) {
        currentState = GameState.VICTORY;
        totalGems += gemsEarned;
        totalUpgradePoints += upgradePointsEarned;
    }

    public void showDefeat() {
        currentState = GameState.DEFEAT;
    }
    
    public int getTotalGems() {
        return totalGems;
    }
    
    public int getTotalUpgradePoints() {
        return totalUpgradePoints;
    }
}
