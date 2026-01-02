package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    // Button rectangles
    private Rectangle campaignButton;
    private Rectangle upgradesButton;
    private Rectangle controlsButton;
    private Rectangle optionsButton;
    private Rectangle quitButton;

    // Button textures
    private Texture campaignButtonUp;
    private Texture campaignButtonDown;
    private Texture genericButtonUp;
    private Texture genericButtonDown;


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

        // Load textures
        campaignButtonUp = new Texture("stolen/ui/startButton1.png");
        campaignButtonDown = new Texture("stolen/ui/startButton2.png");
        genericButtonUp = new Texture("stolen/ui/backMenuButton1.png");
        genericButtonDown = new Texture("stolen/ui/backMenuButton2.png");

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

        // Get mouse coordinates for hover detection
        float mouseX = Gdx.input.getX();
        float mouseY = 720 - Gdx.input.getY(); // Flip Y coordinate

        // Handle input (clicks)
        if (Gdx.input.justTouched()) {
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

        batch.begin();

        // Draw Campaign button with hover effect
        if (campaignButton.contains(mouseX, mouseY)) {
            batch.draw(campaignButtonDown, campaignButton.x, campaignButton.y, campaignButton.width, campaignButton.height);
        } else {
            batch.draw(campaignButtonUp, campaignButton.x, campaignButton.y, campaignButton.width, campaignButton.height);
        }

        // Draw Upgrades button with hover effect
        if (upgradesButton.contains(mouseX, mouseY)) {
            batch.draw(genericButtonDown, upgradesButton.x, upgradesButton.y, upgradesButton.width, upgradesButton.height);
        } else {
            batch.draw(genericButtonUp, upgradesButton.x, upgradesButton.y, upgradesButton.width, upgradesButton.height);
        }

        // Draw Controls button with hover effect
        if (controlsButton.contains(mouseX, mouseY)) {
            batch.draw(genericButtonDown, controlsButton.x, controlsButton.y, controlsButton.width, controlsButton.height);
        } else {
            batch.draw(genericButtonUp, controlsButton.x, controlsButton.y, controlsButton.width, controlsButton.height);
        }

        // Draw Options button with hover effect
        if (optionsButton.contains(mouseX, mouseY)) {
            batch.draw(genericButtonDown, optionsButton.x, optionsButton.y, optionsButton.width, optionsButton.height);
        } else {
            batch.draw(genericButtonUp, optionsButton.x, optionsButton.y, optionsButton.width, optionsButton.height);
        }

        // Draw Quit button with hover effect
        if (quitButton.contains(mouseX, mouseY)) {
            batch.draw(genericButtonDown, quitButton.x, quitButton.y, quitButton.width, quitButton.height);
        } else {
            batch.draw(genericButtonUp, quitButton.x, quitButton.y, quitButton.width, quitButton.height);
        }

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
        campaignButtonUp.dispose();
        campaignButtonDown.dispose();
        genericButtonUp.dispose();
        genericButtonDown.dispose();
    }
}