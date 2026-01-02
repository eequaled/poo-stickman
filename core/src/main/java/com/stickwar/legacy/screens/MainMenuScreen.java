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
 * Main menu screen with campaign and upgrade options.
 * Displays current Gems and Upgrade Points.
 * Provides navigation to campaign, upgrades, options, and quit functionality.
 */
public class MainMenuScreen implements Screen {
    private final GameMain game;
    private final UpgradeManager upgradeManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    // Button rectangles
    private Rectangle campaignButton;
    private Rectangle upgradesButton;
    private Rectangle controlsButton;
    private Rectangle optionsButton;
    private Rectangle quitButton;

    public MainMenuScreen(GameMain game) {
        this.game = game;
        this.upgradeManager = new UpgradeManager(); // Global upgrade manager
        
        // Initialize with current totals from GameMain
        upgradeManager.addGems(game.getTotalGems());
        upgradeManager.addUpgradePoints(game.getTotalUpgradePoints());
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        
        // Create button rectangles
        float buttonWidth = 200;
        float buttonHeight = 50;
        float centerX = 1280 / 2f - buttonWidth / 2f;
        
        campaignButton = new Rectangle(centerX, 420, buttonWidth, buttonHeight);
        upgradesButton = new Rectangle(centerX, 350, buttonWidth, buttonHeight);
        controlsButton = new Rectangle(centerX, 280, buttonWidth, buttonHeight);
        optionsButton = new Rectangle(centerX, 210, buttonWidth, buttonHeight);
        quitButton = new Rectangle(centerX, 140, buttonWidth, buttonHeight);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 720 - Gdx.input.getY(); // Flip Y coordinate
            
            if (campaignButton.contains(mouseX, mouseY)) {
                game.setScreen(new LevelSelectScreen(game, upgradeManager));
            } else if (upgradesButton.contains(mouseX, mouseY)) {
                game.setScreen(new UpgradeShopScreen(game, upgradeManager));
            } else if (controlsButton.contains(mouseX, mouseY)) {
                game.setScreen(new ControlsScreen(game));
            } else if (optionsButton.contains(mouseX, mouseY)) {
                game.setScreen(new OptionsScreen(game));
            } else if (quitButton.contains(mouseX, mouseY)) {
                Gdx.app.exit();
            }
        }

        // Draw buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Campaign button
        shapeRenderer.setColor(0.4f, 0.8f, 0.4f, 1);
        shapeRenderer.rect(campaignButton.x, campaignButton.y, campaignButton.width, campaignButton.height);
        
        // Upgrades button
        shapeRenderer.setColor(0.8f, 0.6f, 0.2f, 1);
        shapeRenderer.rect(upgradesButton.x, upgradesButton.y, upgradesButton.width, upgradesButton.height);
        
        // Controls button
        shapeRenderer.setColor(0.3f, 0.6f, 0.9f, 1);
        shapeRenderer.rect(controlsButton.x, controlsButton.y, controlsButton.width, controlsButton.height);
        
        // Options button
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        shapeRenderer.rect(optionsButton.x, optionsButton.y, optionsButton.width, optionsButton.height);
        
        // Quit button
        shapeRenderer.setColor(0.8f, 0.4f, 0.4f, 1);
        shapeRenderer.rect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
        
        shapeRenderer.end();

        // Draw text
        batch.begin();
        
        // Title
        font.draw(batch, "Stick War Legacy", 1280/2f - 80, 550);
        
        // Currency display
        font.draw(batch, "Gems: " + upgradeManager.getGems(), 50, 680);
        font.draw(batch, "Upgrade Points: " + upgradeManager.getUpgradePoints(), 50, 650);
        
        // Button labels
        font.draw(batch, "Campaign", campaignButton.x + 70, campaignButton.y + 30);
        font.draw(batch, "Upgrades", upgradesButton.x + 70, upgradesButton.y + 30);
        font.draw(batch, "How to Play", controlsButton.x + 55, controlsButton.y + 30);
        font.draw(batch, "Options", optionsButton.x + 80, optionsButton.y + 30);
        font.draw(batch, "Quit", quitButton.x + 90, quitButton.y + 30);
        
        batch.end();
    }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
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