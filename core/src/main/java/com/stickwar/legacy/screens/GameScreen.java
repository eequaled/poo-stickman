package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stickwar.legacy.core.GameMain;
import com.stickwar.legacy.core.GameState;
import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.buildings.CommandCenter;
import com.stickwar.legacy.resources.ResourceManager;
import com.stickwar.legacy.ui.GameHUD;
import com.stickwar.legacy.world.GameMap;
import com.stickwar.legacy.world.TerritoryManager;
import com.stickwar.legacy.world.Tile;
import com.stickwar.legacy.world.TileType;

/**
 * Main gameplay screen.
 * Renders GameMap tiles, units, and buildings.
 */
public class GameScreen implements Screen {
    private final GameMain game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    
    // Game systems
    private GameMap gameMap;
    private TerritoryManager territoryManager;
    private ResourceManager playerResources;
    private ResourceManager enemyResources;
    private GameHUD hud;
    
    // Players
    private Player player;
    private Player enemy;
    
    // Buildings
    private CommandCenter playerBase;
    private CommandCenter enemyBase;

    public GameScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        shapeRenderer = new ShapeRenderer();
        
        // Initialize players
        player = new Player(1, "Player", true);
        enemy = new Player(2, "Enemy", false);
        
        // Initialize game systems
        gameMap = new GameMap(64, 8);
        territoryManager = new TerritoryManager(gameMap);
        playerResources = new ResourceManager();
        enemyResources = new ResourceManager();
        
        // Initialize buildings
        playerBase = new CommandCenter(50, 360, player);
        enemyBase = new CommandCenter(1230, 360, enemy);
        
        // Initialize HUD
        hud = new GameHUD(playerResources, territoryManager);
    }

    @Override
    public void render(float delta) {
        // Update
        update(delta);
        
        // Clear screen
        Gdx.gl.glClearColor(0.4f, 0.6f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        
        // Render map tiles
        renderMap();
        
        // Render buildings
        renderBuildings();
        
        // Render HUD
        hud.render(delta);
        
        // Check win/lose conditions
        checkVictoryConditions();
    }

    private void update(float delta) {
        // Update game logic here
        hud.update(delta);
    }

    private void renderMap() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        float tileWidth = 20;
        float tileHeight = 80;
        
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                Tile tile = gameMap.getTile(x, y);
                
                // Set color based on tile type
                if (tile.getType() == TileType.GRASS) {
                    shapeRenderer.setColor(0.3f, 0.7f, 0.3f, 1);
                } else if (tile.getType() == TileType.WATER) {
                    shapeRenderer.setColor(0.2f, 0.4f, 0.8f, 1);
                } else if (tile.getType() == TileType.MOUNTAIN) {
                    shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
                }
                
                shapeRenderer.rect(x * tileWidth, y * tileHeight + 100, tileWidth - 1, tileHeight - 1);
            }
        }
        
        shapeRenderer.end();
    }

    private void renderBuildings() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Player base (blue)
        shapeRenderer.setColor(0.2f, 0.4f, 0.8f, 1);
        shapeRenderer.rect(playerBase.getX(), playerBase.getY(), 60, 100);
        
        // Enemy base (red)
        shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1);
        shapeRenderer.rect(enemyBase.getX(), enemyBase.getY(), 60, 100);
        
        shapeRenderer.end();
    }

    private void checkVictoryConditions() {
        if (playerBase.isDestroyed()) {
            game.showDefeat();
            game.setScreen(new MainMenuScreen(game));
        } else if (enemyBase.isDestroyed()) {
            game.showVictory(10, 5);
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hud.dispose();
    }
    
    // Getters for testing
    public CommandCenter getPlayerBase() { return playerBase; }
    public CommandCenter getEnemyBase() { return enemyBase; }
    public ResourceManager getPlayerResources() { return playerResources; }
}
