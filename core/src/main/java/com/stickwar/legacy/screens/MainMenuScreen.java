package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.stickwar.legacy.core.GameMain;

/**
 * Main menu screen with simple programmatic UI.
 * Provides navigation to game, options, and quit functionality.
 */
public class MainMenuScreen implements Screen {
    private final GameMain game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    // Button rectangles
    private Rectangle newGameButton;
    private Rectangle optionsButton;
    private Rectangle quitButton;

    public MainMenuScreen(GameMain game) {
        this.game = game;
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
        
        newGameButton = new Rectangle(centerX, 400, buttonWidth, buttonHeight);
        optionsButton = new Rectangle(centerX, 330, buttonWidth, buttonHeight);
        quitButton = new Rectangle(centerX, 260, buttonWidth, buttonHeight);
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
            
            if (newGameButton.contains(mouseX, mouseY)) {
                game.startGame();
                game.setScreen(new GameScreen(game));
            } else if (optionsButton.contains(mouseX, mouseY)) {
                game.setScreen(new OptionsScreen(game));
            } else if (quitButton.contains(mouseX, mouseY)) {
                Gdx.app.exit();
            }
        }

        // Draw buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // New Game button
        shapeRenderer.setColor(0.4f, 0.4f, 0.8f, 1);
        shapeRenderer.rect(newGameButton.x, newGameButton.y, newGameButton.width, newGameButton.height);
        
        // Options button
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        shapeRenderer.rect(optionsButton.x, optionsButton.y, optionsButton.width, optionsButton.height);
        
        // Quit button
        shapeRenderer.setColor(0.8f, 0.4f, 0.4f, 1);
        shapeRenderer.rect(quitButton.x, quitButton.y, quitButton.width, quitButton.height);
        
        shapeRenderer.end();

        // Draw text
        batch.begin();
        font.draw(batch, "Stick War Legacy", 1280/2f - 80, 500);
        font.draw(batch, "New Game", newGameButton.x + 70, newGameButton.y + 30);
        font.draw(batch, "Options", optionsButton.x + 80, optionsButton.y + 30);
        font.draw(batch, "Quit", quitButton.x + 90, quitButton.y + 30);
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