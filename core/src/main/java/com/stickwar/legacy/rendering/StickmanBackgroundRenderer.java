package com.stickwar.legacy.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.stickwar.legacy.assets.StolenAssetManager;

/**
 * Renderer for game backgrounds using stolen stickman game assets.
 * Provides dynamic background switching and parallax effects.
 */
public class StickmanBackgroundRenderer {
    
    private StolenAssetManager assetManager;
    private int currentBackgroundIndex = 0;
    private float parallaxSpeed = 0.1f;
    private float backgroundOffset = 0f;
    
    public StickmanBackgroundRenderer() {
        this.assetManager = StolenAssetManager.getInstance();
    }
    
    /**
     * Renders the current background with parallax scrolling effect.
     */
    public void renderBackground(SpriteBatch batch, float screenWidth, float screenHeight) {
        Texture background = assetManager.currentBackground;
        
        if (background == null) {
            return; // No background loaded
        }
        
        // Calculate parallax offset based on game state
        updateParallaxOffset();
        
        // Render background with parallax
        float bgWidth = background.getWidth();
        float bgHeight = background.getHeight();
        
        // Scale background to fit screen
        float scaleX = screenWidth / bgWidth;
        float scaleY = screenHeight / bgHeight;
        float scale = Math.max(scaleX, scaleY); // Use larger scale to cover screen
        
        float scaledWidth = bgWidth * scale;
        float scaledHeight = bgHeight * scale;
        
        // Center the background
        float x = (screenWidth - scaledWidth) / 2 + backgroundOffset;
        float y = (screenHeight - scaledHeight) / 2;
        
        batch.draw(background, x, y, scaledWidth, scaledHeight);
        
        // Draw background twice for seamless scrolling
        if (Math.abs(backgroundOffset) > 0) {
            float secondX = x + (backgroundOffset > 0 ? -scaledWidth : scaledWidth);
            batch.draw(background, secondX, y, scaledWidth, scaledHeight);
        }
    }
    
    /**
     * Updates parallax offset based on frontline position.
     */
    private void updateParallaxOffset() {
        // TODO: Get frontline percentage from TerritoryManager
        // For now, use a simple time-based animation
        backgroundOffset = (float) Math.sin(System.currentTimeMillis() * 0.0001) * 50f;
    }
    
    /**
     * Sets the background by index.
     */
    public void setBackground(int index) {
        if (index >= 0 && index < assetManager.backgrounds.length) {
            currentBackgroundIndex = index;
            assetManager.setBackground(index);
        }
    }
    
    /**
     * Cycles to the next background.
     */
    public void nextBackground() {
        currentBackgroundIndex = (currentBackgroundIndex + 1) % assetManager.backgrounds.length;
        setBackground(currentBackgroundIndex);
    }
    
    /**
     * Cycles to the previous background.
     */
    public void previousBackground() {
        currentBackgroundIndex = (currentBackgroundIndex - 1 + assetManager.backgrounds.length) % assetManager.backgrounds.length;
        setBackground(currentBackgroundIndex);
    }
    
    /**
     * Sets parallax speed for background movement.
     */
    public void setParallaxSpeed(float speed) {
        this.parallaxSpeed = speed;
    }
    
    /**
     * Gets the current background index.
     */
    public int getCurrentBackgroundIndex() {
        return currentBackgroundIndex;
    }
    
    /**
     * Renders a background specifically for the main menu.
     */
    public void renderMenuBackground(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Use a specific background for menu (e.g., Background002.png)
        assetManager.setBackground(1); // Background002.png
        renderBackground(batch, screenWidth, screenHeight);
    }
    
    /**
     * Renders a background specifically for gameplay.
     */
    public void renderGameBackground(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Use a different background for gameplay (e.g., background.png)
        assetManager.setBackground(2); // background.png
        renderBackground(batch, screenWidth, screenHeight);
    }
    
    /**
     * Renders a background for victory screen.
     */
    public void renderVictoryBackground(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Use a bright background for victory
        assetManager.setBackground(0); // Background001.png
        renderBackground(batch, screenWidth, screenHeight);
    }
    
    /**
     * Renders a background for defeat screen.
     */
    public void renderDefeatBackground(SpriteBatch batch, float screenWidth, float screenHeight) {
        // Use a darker background for defeat
        assetManager.setBackground(4); // background_image1.png
        renderBackground(batch, screenWidth, screenHeight);
    }
    
    /**
     * Updates background animation based on game state.
     */
    public void update(float deltaTime, float frontlinePercent) {
        // Update parallax based on frontline position
        backgroundOffset = (frontlinePercent - 50f) * parallaxSpeed * 2f;
        
        // Keep background offset within reasonable bounds
        float maxOffset = 100f;
        backgroundOffset = Math.max(-maxOffset, Math.min(maxOffset, backgroundOffset));
    }
}
