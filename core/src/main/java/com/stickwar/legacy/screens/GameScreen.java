package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stickwar.legacy.core.GameMain;
import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.buildings.CommandCenter;
import com.stickwar.legacy.entities.buildings.Barracks;
import com.stickwar.legacy.entities.buildings.Mine;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.resources.ResourceManager;
import com.stickwar.legacy.systems.CombatSystem;
import com.stickwar.legacy.systems.UnitFactory;
import com.stickwar.legacy.ui.GameHUD;
import com.stickwar.legacy.world.GameMap;
import com.stickwar.legacy.world.TerritoryManager;
import com.stickwar.legacy.world.Tile;
import com.stickwar.legacy.world.TileType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private CombatSystem combatSystem;
    private UnitFactory unitFactory;
    private GameHUD hud;
    
    // Players
    private Player player;
    private Player enemy;
    
    // Buildings
    private CommandCenter playerBase;
    private CommandCenter enemyBase;
    private Barracks playerBarracks;
    private Barracks enemyBarracks;
    private Mine playerMine;
    private Mine enemyMine;

    // Units
    private final List<Unit> playerUnits = new ArrayList<>();
    private final List<Unit> enemyUnits = new ArrayList<>();

    // AI spawn timer
    private float enemySpawnTimer = 0f;
    private static final float ENEMY_SPAWN_INTERVAL = 4.5f;

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
        combatSystem = new CombatSystem();
        unitFactory = new UnitFactory();
        
        // Initialize buildings
        playerBase = new CommandCenter(50, 360, player);
        enemyBase = new CommandCenter(1230, 360, enemy);
        playerBarracks = new Barracks(110, 360, player, playerResources);
        enemyBarracks = new Barracks(1170, 360, enemy, enemyResources);
        playerBarracks.setSpawnPosition(140, 360);
        enemyBarracks.setSpawnPosition(1140, 360);
        playerMine = new Mine(80, 280, player);
        enemyMine = new Mine(1200, 280, enemy);
        
        // Initialize HUD
        hud = new GameHUD(playerResources);

        // Starting units/economy
        playerResources.reset();
        enemyResources.reset();
        // Optionally start with a miner each
        Unit pMiner = unitFactory.createUnit("MINER", player);
        pMiner.setPosition(140, 360);
        playerUnits.add(pMiner);
        Unit eMiner = unitFactory.createUnit("MINER", enemy);
        eMiner.setPosition(1140, 360);
        enemyUnits.add(eMiner);
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
        handlePlayerInput();

        // Economy: mines generate gold
        playerMine.update(delta, playerResources);
        enemyMine.update(delta, enemyResources);

        // AI spawning
        enemySpawnTimer -= delta;
        if (enemySpawnTimer <= 0f) {
            spawnEnemyUnit();
            enemySpawnTimer = ENEMY_SPAWN_INTERVAL;
        }

        // Update units (cooldowns)
        for (Unit u : playerUnits) {
            u.update(delta);
        }
        for (Unit u : enemyUnits) {
            u.update(delta);
        }

        // Movement and combat
        processCombatAndMovement(playerUnits, enemyUnits, enemyBase, 1);
        processCombatAndMovement(enemyUnits, playerUnits, playerBase, -1);

        // Cleanup dead units
        removeDeadUnits(playerUnits);
        removeDeadUnits(enemyUnits);

        // Territory/frontline update
        territoryManager.updateFrontline(player, playerUnits);
        int frontlineTile = territoryManager.calculateFrontline(player, playerUnits);
        float percent = 0f;
        if (gameMap.getWidth() > 1) {
            percent = (frontlineTile / (float) (gameMap.getWidth() - 1)) * 100f;
        }
        hud.setFrontlinePercent(percent);
        hud.update(delta);
    }

    private void handlePlayerInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            spawnPlayerUnit("MINER");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            spawnPlayerUnit("SWORDWRATH");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            spawnPlayerUnit("ARCHIDONIS");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            spawnPlayerUnit("SPEARTON");
        }
    }

    private void spawnPlayerUnit(String type) {
        Unit unit = playerBarracks.spawnUnit(type);
        if (unit != null) {
            playerUnits.add(unit);
        }
    }

    private void spawnEnemyUnit() {
        // Simple AI: prefer swordwrath/archidonis mix if affordable, else miner
        String[] order = new String[] {"SWORDWRATH", "ARCHIDONIS", "SPEARTON", "MINER"};
        for (String type : order) {
            int cost = enemyBarracks.getUnitCost(type);
            if (enemyResources.canAfford(cost)) {
                Unit unit = enemyBarracks.spawnUnit(type);
                if (unit != null) {
                    enemyUnits.add(unit);
                    return;
                }
            }
        }
    }

    private void processCombatAndMovement(List<Unit> allies, List<Unit> enemies, CommandCenter enemyBase, int direction) {
        for (Unit unit : allies) {
            // Choose target
            com.stickwar.legacy.entities.Entity target = findNearestTarget(unit, enemies, enemyBase);

            if (target != null) {
                float dx = target.getX() - unit.getX();

                if (combatSystem.isInRange(unit, target) && unit.isAttackReady()) {
                    combatSystem.processAttack(unit, target);
                    unit.resetAttackCooldown();
                } else {
                    // Move toward target
                    int moveDir = dx == 0 ? direction : (dx > 0 ? 1 : -1);
                    unit.move(Gdx.graphics.getDeltaTime(), moveDir);
                }
            } else {
                // No target, keep marching
                unit.move(Gdx.graphics.getDeltaTime(), direction);
            }

            clampToBounds(unit);
        }
    }

    private com.stickwar.legacy.entities.Entity findNearestTarget(Unit seeker, List<Unit> enemies, CommandCenter enemyBase) {
        com.stickwar.legacy.entities.Entity best = null;
        float bestDist2 = Float.MAX_VALUE;

        for (Unit enemyUnit : enemies) {
            float dx = enemyUnit.getX() - seeker.getX();
            float dy = enemyUnit.getY() - seeker.getY();
            float d2 = dx * dx + dy * dy;
            if (d2 < bestDist2) {
                bestDist2 = d2;
                best = enemyUnit;
            }
        }

        // Consider base as target
        if (enemyBase != null) {
            float dx = enemyBase.getX() - seeker.getX();
            float dy = enemyBase.getY() - seeker.getY();
            float d2 = dx * dx + dy * dy;
            if (d2 < bestDist2) {
                best = enemyBase;
            }
        }

        return best;
    }

    private void clampToBounds(Unit unit) {
        float x = unit.getX();
        if (x < 0) x = 0;
        float maxX = gameMap.getWidth() * 20f; // tileWidth from render
        if (x > maxX) x = maxX;
        unit.setX(x);
    }

    private void removeDeadUnits(List<Unit> units) {
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u.isMarkedForRemoval()) {
                it.remove();
            }
        }
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

                // Tint by ownership (light blue for player, light red for enemy)
                if (tile.getOwner() == player) {
                    shapeRenderer.setColor(0.2f, 0.5f, 0.9f, 1);
                } else if (tile.getOwner() == enemy) {
                    shapeRenderer.setColor(0.9f, 0.4f, 0.4f, 1);
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
        shapeRenderer.rect(playerBarracks.getX(), playerBarracks.getY(), 50, 60);
        shapeRenderer.rect(playerMine.getX(), playerMine.getY(), 40, 40);
        
        // Enemy base (red)
        shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 1);
        shapeRenderer.rect(enemyBase.getX(), enemyBase.getY(), 60, 100);
        shapeRenderer.rect(enemyBarracks.getX(), enemyBarracks.getY(), 50, 60);
        shapeRenderer.rect(enemyMine.getX(), enemyMine.getY(), 40, 40);
        
        shapeRenderer.end();

        // Units
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Unit u : playerUnits) {
            shapeRenderer.setColor(0.2f, 0.6f, 1f, 1);
            shapeRenderer.circle(u.getX(), u.getY(), 10);
        }
        for (Unit u : enemyUnits) {
            shapeRenderer.setColor(1f, 0.4f, 0.4f, 1);
            shapeRenderer.circle(u.getX(), u.getY(), 10);
        }
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
