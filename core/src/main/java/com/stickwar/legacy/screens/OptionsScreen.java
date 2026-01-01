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
 * Options screen with volume control and back button.
 * Currently provides volume slider stub for future audio implementation.
 */
public class OptionsScreen implements Screen {
    private final GameMain game;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    private Rectangle backButton;
    private Rectangle volumeSlider;
    private float volumeValue = 50f; // 0-100

    public OptionsScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        
        // Create UI rectangles
        float centerX = 1280 / 2f;
        backButton = new Rectangle(centerX - 100, 200, 200, 50);
        volumeSlider = new Rectangle(centerX - 100, 350, 200, 20);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 720 - Gdx.input.getY(); // Flip Y coordinate
            
            if (backButton.contains(mouseX, mouseY)) {
                game.returnToMenu();
                game.setScreen(new MainMenuScreen(game));
            }
        }

        // Draw UI elements
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Back button
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        
        // Volume slider background
        shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1);
        shapeRenderer.rect(volumeSlider.x, volumeSlider.y, volumeSlider.width, volumeSlider.height);
        
        // Volume slider fill
        shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1);
        float fillWidth = (volumeValue / 100f) * volumeSlider.width;
        shapeRenderer.rect(volumeSlider.x, volumeSlider.y, fillWidth, volumeSlider.height);
        
        shapeRenderer.end();

        // Draw text
        batch.begin();
        font.draw(batch, "Options", 1280/2f - 30, 500);
        font.draw(batch, "Volume: " + (int)volumeValue, volumeSlider.x, volumeSlider.y + 40);
        font.draw(batch, "Back", backButton.x + 85, backButton.y + 30);
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