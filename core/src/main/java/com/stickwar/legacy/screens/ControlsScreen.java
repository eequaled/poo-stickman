package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.stickwar.legacy.core.GameMain;

/**
 * Controls tutorial screen explaining how to play the game.
 */
public class ControlsScreen implements Screen {
    private final GameMain game;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ShapeRenderer shapeRenderer;
    private Rectangle backButton;

    public ControlsScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        titleFont = new BitmapFont();
        titleFont.getData().setScale(1.5f);
        shapeRenderer = new ShapeRenderer();
        
        backButton = new Rectangle(540, 50, 200, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 720 - Gdx.input.getY();
            
            if (backButton.contains(mouseX, mouseY)) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

        // Draw back button
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        shapeRenderer.end();

        batch.begin();
        
        // Title
        titleFont.setColor(Color.YELLOW);
        titleFont.draw(batch, "HOW TO PLAY", 520, 680);
        
        font.setColor(Color.WHITE);
        float y = 600;
        float lineHeight = 30;
        
        // Objective
        font.setColor(Color.CYAN);
        font.draw(batch, "OBJECTIVE:", 100, y);
        font.setColor(Color.WHITE);
        font.draw(batch, "Destroy the enemy Statue (base) to win!", 100, y - lineHeight);
        font.draw(batch, "Protect your own Statue from being destroyed.", 100, y - lineHeight * 2);
        
        y -= lineHeight * 4;
        
        // Unit Controls
        font.setColor(Color.CYAN);
        font.draw(batch, "SPAWN UNITS (Click buttons or press keys):", 100, y);
        font.setColor(Color.WHITE);
        font.draw(batch, "1 - Miner (150 gold) - Collects gold from gold piles", 100, y - lineHeight);
        font.draw(batch, "2 - Swordwrath (200 gold) - Fast melee fighter", 100, y - lineHeight * 2);
        font.draw(batch, "3 - Archidonis (350 gold) - Ranged archer (stays at distance)", 100, y - lineHeight * 3);
        font.draw(batch, "4 - Spearton (500 gold) - Heavy tank with shield (30% less damage)", 100, y - lineHeight * 4);
        
        y -= lineHeight * 6;
        
        // Army States
        font.setColor(Color.CYAN);
        font.draw(batch, "ARMY COMMANDS:", 100, y);
        font.setColor(Color.WHITE);
        font.draw(batch, "A - ATTACK: All combat units march toward enemy base", 100, y - lineHeight);
        font.draw(batch, "D - DEFEND: Units hold position at defense line", 100, y - lineHeight * 2);
        font.draw(batch, "R - RETREAT: Units fall back to your base", 100, y - lineHeight * 3);
        
        y -= lineHeight * 5;
        
        // Tips
        font.setColor(Color.CYAN);
        font.draw(batch, "UNIT CONTROL (Click on a unit to select):", 100, y);
        font.setColor(Color.WHITE);
        font.draw(batch, "- LEFT/RIGHT arrows to move the selected unit", 100, y - lineHeight);
        font.draw(batch, "- Controlled units deal 2x DAMAGE!", 100, y - lineHeight * 2);
        font.draw(batch, "- SPACE = Special attack (Archidonis shoots, Spearton throws spear)", 100, y - lineHeight * 3);
        font.draw(batch, "- Spearton spear penetrates ALL enemies and deals 20% to statue!", 100, y - lineHeight * 4);
        font.draw(batch, "- ESC to deselect unit", 100, y - lineHeight * 5);
        
        y -= lineHeight * 7;
        
        // More tips
        font.setColor(Color.CYAN);
        font.draw(batch, "TIPS:", 100, y);
        font.setColor(Color.WHITE);
        font.draw(batch, "- Units stop and fight when they meet enemies (no phasing through)", 100, y - lineHeight);
        font.draw(batch, "- Archidonis automatically stay at range and shoot arrows", 100, y - lineHeight * 2);
        font.draw(batch, "- Win a level to earn 10 Gems + 5 Upgrade Points", 100, y - lineHeight * 3);
        
        // Back button text
        font.setColor(Color.BLACK);
        font.draw(batch, "Back to Menu", backButton.x + 45, backButton.y + 30);
        
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
        titleFont.dispose();
        shapeRenderer.dispose();
    }
}
