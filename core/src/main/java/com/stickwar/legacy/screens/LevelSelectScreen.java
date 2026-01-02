package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.stickwar.legacy.campaign.LevelConfig;
import com.stickwar.legacy.core.GameMain;
import com.stickwar.legacy.systems.UpgradeManager;

/**
 * Level selection screen for the 3-level campaign.
 * Shows available levels with difficulty names and locks unavailable levels.
 */
public class LevelSelectScreen implements Screen {
    private final GameMain game;
    private final UpgradeManager upgradeManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    // Button rectangles
    private Rectangle level1Button;
    private Rectangle level2Button;
    private Rectangle level3Button;
    private Rectangle backButton;
    
    // Level unlock tracking (for demo, all levels unlocked)
    private int maxUnlockedLevel = 3;

    public LevelSelectScreen(GameMain game, UpgradeManager upgradeManager) {
        this.game = game;
        this.upgradeManager = upgradeManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        
        // Create button rectangles
        float buttonWidth = 250;
        float buttonHeight = 80;
        float centerX = 1280 / 2f - buttonWidth / 2f;
        
        level1Button = new Rectangle(centerX, 450, buttonWidth, buttonHeight);
        level2Button = new Rectangle(centerX, 350, buttonWidth, buttonHeight);
        level3Button = new Rectangle(centerX, 250, buttonWidth, buttonHeight);
        backButton = new Rectangle(50, 50, 100, 40);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 720 - Gdx.input.getY(); // Flip Y coordinate
            
            if (level1Button.contains(mouseX, mouseY) && 1 <= maxUnlockedLevel) {
                startLevel(1);
            } else if (level2Button.contains(mouseX, mouseY) && 2 <= maxUnlockedLevel) {
                startLevel(2);
            } else if (level3Button.contains(mouseX, mouseY) && 3 <= maxUnlockedLevel) {
                startLevel(3);
            } else if (backButton.contains(mouseX, mouseY)) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

        // Draw buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Level 1 button (always available)
        shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1);
        shapeRenderer.rect(level1Button.x, level1Button.y, level1Button.width, level1Button.height);
        
        // Level 2 button
        if (2 <= maxUnlockedLevel) {
            shapeRenderer.setColor(0.8f, 0.6f, 0.2f, 1);
        } else {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1); // Locked
        }
        shapeRenderer.rect(level2Button.x, level2Button.y, level2Button.width, level2Button.height);
        
        // Level 3 button
        if (3 <= maxUnlockedLevel) {
            shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1);
        } else {
            shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1); // Locked
        }
        shapeRenderer.rect(level3Button.x, level3Button.y, level3Button.width, level3Button.height);
        
        // Back button
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        
        shapeRenderer.end();

        // Draw text
        batch.begin();
        
        // Title
        font.draw(batch, "Select Campaign Level", 1280/2f - 100, 600);
        
        // Currency display
        font.draw(batch, "Gems: " + upgradeManager.getGems(), 50, 680);
        font.draw(batch, "Upgrade Points: " + upgradeManager.getUpgradePoints(), 50, 650);
        
        // Level buttons
        font.draw(batch, "Level 1 - Easy", level1Button.x + 80, level1Button.y + 50);
        font.draw(batch, "6.0s spawn, 30% combat", level1Button.x + 50, level1Button.y + 25);
        
        if (2 <= maxUnlockedLevel) {
            font.draw(batch, "Level 2 - Medium", level2Button.x + 70, level2Button.y + 50);
            font.draw(batch, "4.5s spawn, 60% combat", level2Button.x + 50, level2Button.y + 25);
        } else {
            font.draw(batch, "Level 2 - LOCKED", level2Button.x + 70, level2Button.y + 40);
        }
        
        if (3 <= maxUnlockedLevel) {
            font.draw(batch, "Level 3 - Hard", level3Button.x + 80, level3Button.y + 50);
            font.draw(batch, "3.0s spawn, 90% combat", level3Button.x + 50, level3Button.y + 25);
        } else {
            font.draw(batch, "Level 3 - LOCKED", level3Button.x + 70, level3Button.y + 40);
        }
        
        // Back button
        font.draw(batch, "Back", backButton.x + 30, backButton.y + 25);
        
        batch.end();
    }
    
    private void startLevel(int levelNumber) {
        LevelConfig levelConfig = LevelConfig.getLevel(levelNumber);
        game.setScreen(new GameScreen(game, levelConfig, upgradeManager));
    }
    
    public void setMaxUnlockedLevel(int level) {
        this.maxUnlockedLevel = Math.min(level, LevelConfig.getTotalLevels());
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