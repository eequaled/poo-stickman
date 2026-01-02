package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.stickwar.legacy.core.GameMain;
import com.stickwar.legacy.systems.UpgradeManager;

/**
 * Campaign completion screen shown after beating Level 3.
 * Displays congratulations message and final stats.
 */
public class CampaignCompleteScreen implements Screen {
    private final GameMain game;
    private final UpgradeManager upgradeManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    private Rectangle continueButton;
    private float displayTimer = 0f;

    public CampaignCompleteScreen(GameMain game, UpgradeManager upgradeManager) {
        this.game = game;
        this.upgradeManager = upgradeManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        
        continueButton = new Rectangle(1280/2f - 100, 150, 200, 50);
    }

    @Override
    public void render(float delta) {
        displayTimer += delta;
        
        // Clear screen with victory colors
        Gdx.gl.glClearColor(0.1f, 0.3f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input (after 2 seconds)
        if (displayTimer > 2.0f && Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 720 - Gdx.input.getY(); // Flip Y coordinate
            
            if (continueButton.contains(mouseX, mouseY)) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

        // Draw continue button (only after 2 seconds)
        if (displayTimer > 2.0f) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1);
            shapeRenderer.rect(continueButton.x, continueButton.y, continueButton.width, continueButton.height);
            shapeRenderer.end();
        }

        // Draw text
        batch.begin();
        
        // Main congratulations message
        font.draw(batch, "CAMPAIGN COMPLETE!", 1280/2f - 100, 550);
        font.draw(batch, "Congratulations, Commander!", 1280/2f - 120, 500);
        
        // Victory message
        font.draw(batch, "You have successfully conquered all territories", 1280/2f - 180, 450);
        font.draw(batch, "and defeated the enemy forces across all 3 levels!", 1280/2f - 180, 420);
        
        // Final stats
        font.draw(batch, "Final Campaign Stats:", 1280/2f - 100, 350);
        font.draw(batch, "Total Gems Earned: " + upgradeManager.getGems(), 1280/2f - 100, 320);
        font.draw(batch, "Total Upgrade Points: " + upgradeManager.getUpgradePoints(), 1280/2f - 100, 290);
        
        // Upgrade summary
        font.draw(batch, "Building Upgrades:", 1280/2f - 100, 250);
        font.draw(batch, "  Statue Level: " + upgradeManager.getStatueUpgradeLevel(), 1280/2f - 80, 220);
        font.draw(batch, "  Barracks Level: " + upgradeManager.getBarracksUpgradeLevel(), 1280/2f - 80, 190);
        
        // Continue button text (only after 2 seconds)
        if (displayTimer > 2.0f) {
            font.draw(batch, "Continue to Main Menu", continueButton.x + 30, continueButton.y + 30);
        }
        
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}