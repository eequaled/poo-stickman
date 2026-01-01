package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stickwar.legacy.core.GameMain;

/**
 * Main menu screen with Scene2D buttons.
 * Shows: New Game, Options, Quit
 */
public class MainMenuScreen implements Screen {
    private final GameMain game;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create a basic skin programmatically (no external assets required)
        skin = createBasicSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // New Game button
        TextButton newGameButton = new TextButton("New Game", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.startGame();
                game.setScreen(new GameScreen(game));
            }
        });

        // Options button
        TextButton optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionsScreen(game));
            }
        });

        // Quit button
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Layout
        table.add(newGameButton).width(200).height(50).pad(10);
        table.row();
        table.add(optionsButton).width(200).height(50).pad(10);
        table.row();
        table.add(quitButton).width(200).height(50).pad(10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    /**
     * Builds a minimal Scene2D skin with a white drawable and default font.
     */
    private Skin createBasicSkin() {
        Skin s = new Skin();

        // White 1x1 texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        s.add("white", texture);

        // Default font
        BitmapFont font = new BitmapFont();
        s.add("default-font", font);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        s.add("default", labelStyle);

        // TextButton style
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.up = new SpriteDrawable(new Sprite(texture));
        tbs.down = new SpriteDrawable(new Sprite(texture));
        tbs.over = new SpriteDrawable(new Sprite(texture));
        ((SpriteDrawable) tbs.over).getSprite().setColor(0.8f, 0.8f, 0.8f, 1f);
        tbs.font = font;
        tbs.fontColor = Color.BLACK;
        s.add("default", tbs);

        return s;
    }
}
