package com.stickwar.legacy.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.stickwar.legacy.assets.StolenAssetManager;

/**
 * Renderer for stickman-themed UI elements using stolen assets.
 * Replaces default UI with stickman game interface elements.
 */
public class StickmanUIRenderer {
    
    private StolenAssetManager assetManager;
    private BitmapFont font;
    
    public StickmanUIRenderer() {
        this.assetManager = StolenAssetManager.getInstance();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
    }
    
    /**
     * Renders a button using stickman game assets.
     */
    public void renderButton(SpriteBatch batch, float x, float y, float width, float height, String text, ButtonType type) {
        Texture buttonTexture = getButtonTexture(type);
        
        // Draw button background
        batch.draw(buttonTexture, x, y, width, height);
        
        // Draw button text
        float textX = x + width / 2 - font.getRegion().getRegionWidth() * text.length() / 4;
        float textY = y + height / 2 + font.getRegion().getRegionHeight() / 2;
        font.draw(batch, text, textX, textY);
    }
    
    /**
     * Renders the main menu background with stickman theme.
     */
    public void renderMainMenu(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Draw main background
        renderMenuBackground(batch, screenWidth, screenHeight);
        
        // Draw title
        font.getData().setScale(2.0f);
        String title = "STICK WAR: LEGACY";
        float titleX = screenWidth / 2 - font.getRegion().getRegionWidth() * title.length() / 4;
        float titleY = screenHeight * 0.8f;
        font.draw(batch, title, titleX, titleY);
        
        // Draw menu buttons
        float buttonWidth = 200f;
        float buttonHeight = 50f;
        float buttonX = screenWidth / 2 - buttonWidth / 2;
        float buttonSpacing = 70f;
        
        // New Game button
        renderButton(batch, buttonX, screenHeight * 0.5f, buttonWidth, buttonHeight, "NEW GAME", ButtonType.START);
        
        // Options button
        renderButton(batch, buttonX, screenHeight * 0.5f - buttonSpacing, buttonWidth, buttonHeight, "OPTIONS", ButtonType.SELECT);
        
        // Quit button
        renderButton(batch, buttonX, screenHeight * 0.5f - buttonSpacing * 2, buttonWidth, buttonHeight, "QUIT", ButtonType.BACK);
        
        font.getData().setScale(1.0f);
    }
    
    /**
     * Renders character selection screen.
     */
    public void renderCharacterSelection(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Draw background
        renderMenuBackground(batch, screenWidth, screenHeight);
        
        // Draw title
        font.getData().setScale(1.5f);
        String title = "SELECT CHARACTER";
        float titleX = screenWidth / 2 - font.getRegion().getRegionWidth() * title.length() / 4;
        float titleY = screenHeight * 0.9f;
        font.draw(batch, title, titleX, titleY);
        
        // Draw character selection interface
        float selectWidth = 300f;
        float selectHeight = 400f;
        float selectX = screenWidth / 2 - selectWidth / 2;
        float selectY = screenHeight * 0.4f;
        
        batch.draw(assetManager.selectCharacter, selectX, selectY, selectWidth, selectHeight);
        
        // Draw navigation arrows
        float arrowSize = 50f;
        float arrowY = selectY + selectHeight / 2 - arrowSize / 2;
        
        // Left arrow
        batch.draw(assetManager.leftArrow, selectX - arrowSize - 20, arrowY, arrowSize, arrowSize);
        
        // Right arrow
        batch.draw(assetManager.rightArrow, selectX + selectWidth + 20, arrowY, arrowSize, arrowSize);
        
        // Draw start button
        float startButtonWidth = 150f;
        float startButtonHeight = 40f;
        float startButtonX = screenWidth / 2 - startButtonWidth / 2;
        float startButtonY = screenHeight * 0.15f;
        
        renderButton(batch, startButtonX, startButtonY, startButtonWidth, startButtonHeight, "START", ButtonType.START);
        
        font.getData().setScale(1.0f);
    }
    
    /**
     * Renders weapon selection interface.
     */
    public void renderWeaponSelection(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Draw background
        renderMenuBackground(batch, screenWidth, screenHeight);
        
        // Draw title
        font.getData().setScale(1.5f);
        String title = "SELECT WEAPON";
        float titleX = screenWidth / 2 - font.getRegion().getRegionWidth() * title.length() / 4;
        float titleY = screenHeight * 0.9f;
        font.draw(batch, title, titleX, titleY);
        
        // Draw weapon selection interface
        float selectWidth = 350f;
        float selectHeight = 300f;
        float selectX = screenWidth / 2 - selectWidth / 2;
        float selectY = screenHeight * 0.4f;
        
        batch.draw(assetManager.selectSword, selectX, selectY, selectWidth, selectHeight);
        
        // Draw navigation arrows
        float arrowSize = 50f;
        float arrowY = selectY + selectHeight / 2 - arrowSize / 2;
        
        batch.draw(assetManager.leftArrow, selectX - arrowSize - 20, arrowY, arrowSize, arrowSize);
        batch.draw(assetManager.rightArrow, selectX + selectWidth + 20, arrowY, arrowSize, arrowSize);
        
        // Draw back button
        renderButton(batch, 50, screenHeight - 100, 100, 40, "BACK", ButtonType.BACK);
        
        font.getData().setScale(1.0f);
    }
    
    /**
     * Renders in-game UI with stickman theme.
     */
    public void renderGameUI(SpriteBatch batch, float screenWidth, float screenHeight, int gold, float territoryPercent) {
        // Render gold counter
        renderGoldCounter(batch, gold, screenWidth);
        
        // Render territory bar
        renderTerritoryBar(batch, territoryPercent, screenWidth, screenHeight);
        
        // Render unit spawn buttons
        renderUnitButtons(batch, screenWidth, screenHeight);
    }
    
    /**
     * Renders gold counter display.
     */
    private void renderGoldCounter(SpriteBatch batch, int gold, float screenWidth) {
        String goldText = "Gold: " + gold;
        font.draw(batch, goldText, 20, screenWidth - 20);
    }
    
    /**
     * Renders territory control bar.
     */
    private void renderTerritoryBar(SpriteBatch batch, float territoryPercent, float screenWidth, float screenHeight) {
        float barWidth = 400f;
        float barHeight = 20f;
        float barX = screenWidth / 2 - barWidth / 2;
        float barY = screenHeight - 50f;
        
        // Background
        batch.setColor(Color.GRAY);
        batch.draw(assetManager.getStickmanFrame(0), barX, barY, barWidth, barHeight);
        
        // Player territory (blue)
        batch.setColor(Color.BLUE);
        float playerWidth = (territoryPercent / 100f) * barWidth;
        batch.draw(assetManager.getStickmanFrame(0), barX, barY, playerWidth, barHeight);
        
        // Enemy territory (red)
        batch.setColor(Color.RED);
        float enemyWidth = barWidth - playerWidth;
        if (enemyWidth > 0) {
            batch.draw(assetManager.getStickmanFrame(0), barX + playerWidth, barY, enemyWidth, barHeight);
        }
        
        batch.setColor(Color.WHITE);
        
        // Territory percentage text
        String territoryText = String.format("Territory: %.1f%%", territoryPercent);
        font.draw(batch, territoryText, barX, barY - 5);
    }
    
    /**
     * Renders unit spawn buttons.
     */
    private void renderUnitButtons(SpriteBatch batch, float screenWidth, float screenHeight) {
        float buttonWidth = 120f;
        float buttonHeight = 40f;
        float buttonX = 20f;
        float buttonSpacing = 50f;
        float buttonY = screenHeight - 150f;
        
        // Miner button
        renderButton(batch, buttonX, buttonY, buttonWidth, buttonHeight, "MINER", ButtonType.SELECT);
        
        // Swordwrath button
        renderButton(batch, buttonX, buttonY - buttonSpacing, buttonWidth, buttonHeight, "SWORD", ButtonType.SELECT);
        
        // Archidonis button
        renderButton(batch, buttonX, buttonY - buttonSpacing * 2, buttonWidth, buttonHeight, "ARCHER", ButtonType.SELECT);
        
        // Spearton button
        renderButton(batch, buttonX, buttonY - buttonSpacing * 3, buttonWidth, buttonHeight, "SPEAR", ButtonType.SELECT);
    }
    
    /**
     * Gets the appropriate texture for a button type.
     */
    private Texture getButtonTexture(ButtonType type) {
        switch (type) {
            case START:
                return assetManager.startButton;
            case BACK:
                return assetManager.backButton;
            case SELECT:
                return assetManager.selectCharacter;
            default:
                return assetManager.startButton;
        }
    }
    
    /**
     * Renders menu background.
     */
    private void renderMenuBackground(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Use a specific background for menus
        assetManager.setBackground(1); // Background002.png
        Texture background = assetManager.currentBackground;
        
        if (background != null) {
            batch.draw(background, 0, 0, screenWidth, screenHeight);
        }
    }
    
    /**
     * Disposes of resources.
     */
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
    }
    
    /**
     * Button types for UI rendering.
     */
    public enum ButtonType {
        START,
        BACK,
        SELECT
    }
}
