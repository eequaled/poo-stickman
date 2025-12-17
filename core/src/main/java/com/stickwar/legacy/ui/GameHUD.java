package com.stickwar.legacy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stickwar.legacy.resources.ResourceManager;
import com.stickwar.legacy.world.TerritoryManager;

/**
 * Game HUD displaying resources, frontline bar, and notifications.
 */
public class GameHUD {
    private final ResourceManager resources;
    private final TerritoryManager territoryManager;
    
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    private int screenWidth;
    private int screenHeight;
    
    // Frontline percentage (0-100)
    private float frontlinePercent;

    public GameHUD(ResourceManager resources, TerritoryManager territoryManager) {
        this.resources = resources;
        this.territoryManager = territoryManager;
        
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        shapeRenderer = new ShapeRenderer();
        
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        frontlinePercent = 50f;
    }

    public void update(float delta) {
        // Update frontline percentage based on territory
        // For now, use a placeholder value
        // In full implementation, calculate from TerritoryManager
    }

    public void render(float delta) {
        // Render gold amount at top-left
        batch.begin();
        font.draw(batch, "Gold: " + resources.getGold(), 20, screenHeight - 20);
        batch.end();
        
        // Render frontline bar at top-center
        renderFrontlineBar();
        
        // Render notifications panel at bottom (placeholder)
        renderNotifications();
    }

    private void renderFrontlineBar() {
        float barWidth = 300;
        float barHeight = 20;
        float barX = (screenWidth - barWidth) / 2;
        float barY = screenHeight - 40;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Background
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        
        // Player territory (blue)
        shapeRenderer.setColor(0.2f, 0.4f, 0.8f, 1);
        shapeRenderer.rect(barX, barY, barWidth * (frontlinePercent / 100f), barHeight);
        
        // Enemy territory (red) - remaining portion
        shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1);
        shapeRenderer.rect(barX + barWidth * (frontlinePercent / 100f), barY, 
                          barWidth * (1 - frontlinePercent / 100f), barHeight);
        
        shapeRenderer.end();
        
        // Label
        batch.begin();
        font.draw(batch, "Territory", barX + barWidth / 2 - 30, barY + barHeight + 15);
        batch.end();
    }

    private void renderNotifications() {
        // Placeholder for notifications panel at bottom
        batch.begin();
        font.draw(batch, "Notifications: Ready", 20, 30);
        batch.end();
    }

    public void setFrontlinePercent(float percent) {
        this.frontlinePercent = Math.max(0, Math.min(100, percent));
    }

    public void resize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
