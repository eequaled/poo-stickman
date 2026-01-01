package com.stickwar.legacy.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.stickwar.legacy.resources.ResourceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Heads-up display showing resources, territory control, and notifications.
 * Renders real-time game information during gameplay.
 */
public class GameHUD implements Disposable {
    private final ResourceManager resources;
    
    private OrthographicCamera hudCamera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    
    private float frontlinePercent = 0f;
    private final List<String> notifications = new ArrayList<>();
    
    // HUD layout constants
    private static final int HUD_WIDTH = 1280;
    private static final int HUD_HEIGHT = 720;
    private static final int FRONTLINE_BAR_WIDTH = 400;
    private static final int FRONTLINE_BAR_HEIGHT = 20;

    public GameHUD(ResourceManager resources) {
        this.resources = resources;
        
        // Initialize rendering components
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, HUD_WIDTH, HUD_HEIGHT);
        
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(); // Default font
        font.setColor(Color.WHITE);
    }

    /**
     * Updates HUD state each frame.
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        hudCamera.update();
        
        // Clean up old notifications (keep last 5)
        while (notifications.size() > 5) {
            notifications.remove(0);
        }
    }

    /**
     * Renders the complete HUD.
     * @param deltaTime Time since last frame
     */
    public void render(float deltaTime) {
        batch.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        
        renderGoldCounter();
        renderFrontlineBar();
        renderNotifications();
    }

    /**
     * Renders the gold counter at top-left.
     */
    private void renderGoldCounter() {
        batch.begin();
        String goldText = "Gold: " + resources.getGold();
        font.draw(batch, goldText, 20, HUD_HEIGHT - 20);
        batch.end();
    }

    /**
     * Renders the territory frontline bar at top-center.
     */
    private void renderFrontlineBar() {
        float barX = (HUD_WIDTH - FRONTLINE_BAR_WIDTH) / 2f;
        float barY = HUD_HEIGHT - 50;
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Background (neutral)
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(barX, barY, FRONTLINE_BAR_WIDTH, FRONTLINE_BAR_HEIGHT);
        
        // Player territory (blue)
        float playerWidth = (frontlinePercent / 100f) * FRONTLINE_BAR_WIDTH;
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(barX, barY, playerWidth, FRONTLINE_BAR_HEIGHT);
        
        // Enemy territory (red)
        float enemyWidth = FRONTLINE_BAR_WIDTH - playerWidth;
        if (enemyWidth > 0) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(barX + playerWidth, barY, enemyWidth, FRONTLINE_BAR_HEIGHT);
        }
        
        shapeRenderer.end();
        
        // Frontline percentage text
        batch.begin();
        String frontlineText = String.format("Territory: %.1f%%", frontlinePercent);
        font.draw(batch, frontlineText, barX, barY - 5);
        batch.end();
    }

    /**
     * Renders notifications panel at bottom.
     */
    private void renderNotifications() {
        if (notifications.isEmpty()) {
            return;
        }
        
        batch.begin();
        float y = 100;
        for (int i = notifications.size() - 1; i >= 0; i--) {
            String notification = notifications.get(i);
            font.draw(batch, notification, 20, y);
            y += 20;
        }
        batch.end();
    }

    /**
     * Sets the frontline percentage for the territory bar.
     * @param percent Percentage of map controlled by player (0-100)
     */
    public void setFrontlinePercent(float percent) {
        this.frontlinePercent = Math.max(0, Math.min(100, percent));
    }

    /**
     * Adds a notification message.
     * @param message Notification text
     */
    public void addNotification(String message) {
        notifications.add(message);
    }

    /**
     * Handles screen resize events.
     * @param width New screen width
     * @param height New screen height
     */
    public void resize(int width, int height) {
        hudCamera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}