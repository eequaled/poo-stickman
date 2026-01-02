package com.stickwar.legacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.stickwar.legacy.ai.AIController;
import com.stickwar.legacy.campaign.LevelConfig;
import com.stickwar.legacy.core.GameMain;
import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.buildings.CommandCenter;
import com.stickwar.legacy.entities.buildings.Barracks;
import com.stickwar.legacy.entities.Entity;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.entities.units.Miner;
import com.stickwar.legacy.entities.units.Archidonis;
import com.stickwar.legacy.entities.units.Spearton;
import com.stickwar.legacy.resources.ResourceManager;
import com.stickwar.legacy.systems.CombatSystem;
import com.stickwar.legacy.systems.UnitFactory;
import com.stickwar.legacy.systems.UpgradeManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Main gameplay screen - Stick War Legacy style.
 * Features:
 * - Units fight when they meet (no phasing through)
 * - Click to control individual units with 2x damage
 * - Archidonis stay at range and shoot
 * - Spearton special: throw spear that penetrates all units
 * - Gold piles only for player, enemies spawn by interval
 */
public class GameScreen implements Screen {
    private final GameMain game;
    private final LevelConfig levelConfig;
    private final UpgradeManager upgradeManager;
    
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    
    // Game constants
    private static final float GROUND_Y = 150f;
    private static final float SCREEN_WIDTH = 1280f;
    private static final float SCREEN_HEIGHT = 720f;
    private static final float PLAYER_BASE_X = 50f;
    private static final float ENEMY_BASE_X = 1180f;
    private static final float ARCHIDONIS_RANGE = 200f;
    private static final float UNIT_COLLISION_DIST = 40f;
    
    // Army states
    public enum ArmyState { DEFEND, ATTACK, RETREAT }
    private ArmyState playerArmyState = ArmyState.DEFEND;
    private float defendLineX = 300f;
    
    // Players
    private Player player;
    private Player enemy;
    
    // Resources
    private ResourceManager playerResources;
    
    // Buildings
    private CommandCenter playerBase;
    private CommandCenter enemyBase;
    private Barracks playerBarracks;
    private Barracks enemyBarracks;
    
    // Units
    private final List<Unit> playerUnits = new ArrayList<>();
    private final List<Unit> enemyUnits = new ArrayList<>();
    
    // Gold piles (player only)
    private final List<GoldPile> goldPiles = new ArrayList<>();
    
    // Dummy gold piles for enemy (visual only)
    private final List<GoldPile> enemyGoldPiles = new ArrayList<>();
    
    // Projectiles (arrows, spears)
    private final List<Projectile> projectiles = new ArrayList<>();
    
    // User controlled unit
    private Unit controlledUnit = null;
    
    // Systems
    private CombatSystem combatSystem;
    private UnitFactory unitFactory;
    private AIController aiController;
    
    // UI Buttons
    private Rectangle minerButton, swordwrathButton, archidonisButton, speartonButton;
    private Rectangle attackButton, defendButton, retreatButton;

    
    // Inner class for gold piles
    public static class GoldPile {
        public float x, y;
        public int goldAmount;
        public static final int MAX_GOLD = 500;
        
        public GoldPile(float x, float y) {
            this.x = x;
            this.y = y;
            this.goldAmount = MAX_GOLD;
        }
        
        public int collectGold(int amount) {
            int collected = Math.min(amount, goldAmount);
            goldAmount -= collected;
            return collected;
        }
        
        public boolean isEmpty() { return goldAmount <= 0; }
    }
    
    // Inner class for projectiles (arrows, spears)
    public static class Projectile {
        public float x, y;
        public float velocityX;
        public int damage;
        public boolean isSpear; // Spear penetrates, arrow doesn't
        public Player owner;
        public boolean hitSomething = false;
        public List<Entity> alreadyHit = new ArrayList<>();
        
        public Projectile(float x, float y, float velocityX, int damage, boolean isSpear, Player owner) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.damage = damage;
            this.isSpear = isSpear;
            this.owner = owner;
        }
        
        public void update(float delta) {
            x += velocityX * delta;
        }
        
        public boolean isOffScreen() {
            return x < -50 || x > SCREEN_WIDTH + 50;
        }
    }

    public GameScreen(GameMain game) {
        this(game, LevelConfig.LEVEL_1, new UpgradeManager());
    }
    
    public GameScreen(GameMain game, LevelConfig levelConfig, UpgradeManager upgradeManager) {
        this.game = game;
        this.levelConfig = levelConfig;
        this.upgradeManager = upgradeManager;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        
        // Initialize players
        player = new Player(1, "Player", true);
        enemy = new Player(2, "Enemy", false);
        
        // Initialize resources (only player needs gold)
        playerResources = new ResourceManager();
        playerResources.reset();
        
        // Initialize systems
        combatSystem = new CombatSystem();
        unitFactory = new UnitFactory();
        unitFactory.setUpgradeManager(upgradeManager);
        
        // Initialize buildings
        playerBase = new CommandCenter(PLAYER_BASE_X, GROUND_Y, player, upgradeManager);
        enemyBase = new CommandCenter(ENEMY_BASE_X, GROUND_Y, enemy);
        playerBarracks = new Barracks(PLAYER_BASE_X + 80, GROUND_Y, player, playerResources, upgradeManager);
        
        // Enemy barracks doesn't need resources (spawns by interval)
        ResourceManager dummyResources = new ResourceManager();
        dummyResources.addGold(999999); // Infinite gold for AI
        enemyBarracks = new Barracks(ENEMY_BASE_X - 80, GROUND_Y, enemy, dummyResources);
        
        playerBarracks.setSpawnPosition(PLAYER_BASE_X + 120, GROUND_Y);
        enemyBarracks.setSpawnPosition(ENEMY_BASE_X - 120, GROUND_Y);
        
        // Initialize AI
        aiController = new AIController(levelConfig, enemy, enemyBarracks, dummyResources);
        
        // Create gold piles for player (left side of map)
        createGoldPiles();
        
        // Create UI buttons
        createUIButtons();
        
        // Start with one miner
        spawnPlayerUnit("MINER");
    }
    
    private void createGoldPiles() {
        // Player gold piles (left-center of map)
        goldPiles.add(new GoldPile(200, GROUND_Y - 20));
        goldPiles.add(new GoldPile(300, GROUND_Y - 20));
        goldPiles.add(new GoldPile(400, GROUND_Y - 20));
        goldPiles.add(new GoldPile(500, GROUND_Y - 20));
        
        // Dummy enemy gold piles (visual only, right side)
        enemyGoldPiles.add(new GoldPile(780, GROUND_Y - 20));
        enemyGoldPiles.add(new GoldPile(880, GROUND_Y - 20));
        enemyGoldPiles.add(new GoldPile(980, GROUND_Y - 20));
        enemyGoldPiles.add(new GoldPile(1080, GROUND_Y - 20));
    }
    
    private void createUIButtons() {
        float buttonSize = 60;
        float buttonY = SCREEN_HEIGHT - 80;
        float startX = 50;
        float spacing = 70;
        
        minerButton = new Rectangle(startX, buttonY, buttonSize, buttonSize);
        swordwrathButton = new Rectangle(startX + spacing, buttonY, buttonSize, buttonSize);
        archidonisButton = new Rectangle(startX + spacing * 2, buttonY, buttonSize, buttonSize);
        speartonButton = new Rectangle(startX + spacing * 3, buttonY, buttonSize, buttonSize);
        
        float stateButtonX = SCREEN_WIDTH - 200;
        attackButton = new Rectangle(stateButtonX, buttonY, 50, 50);
        defendButton = new Rectangle(stateButtonX + 55, buttonY, 50, 50);
        retreatButton = new Rectangle(stateButtonX + 110, buttonY, 50, 50);
    }


    @Override
    public void render(float delta) {
        update(delta);
        
        Gdx.gl.glClearColor(0.4f, 0.6f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        
        renderBackground();
        renderGoldPiles();
        renderBuildings();
        renderUnits();
        renderProjectiles();
        renderUI();
        
        checkVictoryConditions();
    }
    
    private void update(float delta) {
        handleInput();
        
        // Update AI spawning
        aiController.update(delta);
        Unit newAIUnit = aiController.getAndClearLastSpawnedUnit();
        if (newAIUnit != null) {
            newAIUnit.setY(GROUND_Y);
            enemyUnits.add(newAIUnit);
        }
        
        // Update controlled unit
        updateControlledUnit(delta);
        
        // Update all units
        updateUnits(delta);
        
        // Process miner behavior
        updateMiners(delta);
        
        // Process combat (units stop and fight when they meet)
        processCombat(delta);
        
        // Update projectiles
        updateProjectiles(delta);
        
        // Remove dead units
        removeDeadUnits(playerUnits);
        removeDeadUnits(enemyUnits);
        
        // Remove empty gold piles
        goldPiles.removeIf(GoldPile::isEmpty);
    }
    
    private void handleInput() {
        // Keyboard shortcuts for spawning
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) spawnPlayerUnit("MINER");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) spawnPlayerUnit("SWORDWRATH");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) spawnPlayerUnit("ARCHIDONIS");
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) spawnPlayerUnit("SPEARTON");
        
        // Army state shortcuts
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) playerArmyState = ArmyState.ATTACK;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) playerArmyState = ArmyState.DEFEND;
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) playerArmyState = ArmyState.RETREAT;
        
        // Escape to deselect controlled unit
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (controlledUnit != null) {
                controlledUnit.setUserControlled(false);
                controlledUnit = null;
            }
        }
        
        // Controlled unit movement (LEFT/RIGHT arrows)
        if (controlledUnit != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                controlledUnit.move(Gdx.graphics.getDeltaTime(), -1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                controlledUnit.move(Gdx.graphics.getDeltaTime(), 1);
            }
            
            // Space to attack (special ability for Spearton/Archidonis)
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                performSpecialAttack();
            }
        }
        
        // Mouse clicks
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = SCREEN_HEIGHT - Gdx.input.getY();
            
            // Check UI buttons first
            if (minerButton.contains(mouseX, mouseY)) { spawnPlayerUnit("MINER"); return; }
            if (swordwrathButton.contains(mouseX, mouseY)) { spawnPlayerUnit("SWORDWRATH"); return; }
            if (archidonisButton.contains(mouseX, mouseY)) { spawnPlayerUnit("ARCHIDONIS"); return; }
            if (speartonButton.contains(mouseX, mouseY)) { spawnPlayerUnit("SPEARTON"); return; }
            if (attackButton.contains(mouseX, mouseY)) { playerArmyState = ArmyState.ATTACK; return; }
            if (defendButton.contains(mouseX, mouseY)) { playerArmyState = ArmyState.DEFEND; return; }
            if (retreatButton.contains(mouseX, mouseY)) { playerArmyState = ArmyState.RETREAT; return; }
            
            // Check if clicking on a player unit to control it
            for (Unit unit : playerUnits) {
                if (Math.abs(unit.getX() - mouseX) < 30 && Math.abs(unit.getY() - mouseY) < 50) {
                    // Deselect previous
                    if (controlledUnit != null) {
                        controlledUnit.setUserControlled(false);
                    }
                    // Select new
                    controlledUnit = unit;
                    controlledUnit.setUserControlled(true);
                    return;
                }
            }
            
            // Clicked elsewhere - deselect
            if (controlledUnit != null) {
                controlledUnit.setUserControlled(false);
                controlledUnit = null;
            }
        }
    }
    
    private void performSpecialAttack() {
        if (controlledUnit == null) return;
        
        if (controlledUnit instanceof Spearton) {
            // Spearton throws a spear that penetrates all enemies and hits statue for 20%
            Projectile spear = new Projectile(
                controlledUnit.getX() + 20, 
                controlledUnit.getY() + 25,
                400f, // velocity
                50,   // damage to units
                true, // is spear (penetrates)
                player
            );
            projectiles.add(spear);
        } else if (controlledUnit instanceof Archidonis) {
            // Archidonis shoots arrow at nearest enemy or statue
            Entity target = findNearestEnemy(controlledUnit, enemyUnits, enemyBase);
            if (target != null) {
                float direction = target.getX() > controlledUnit.getX() ? 1 : -1;
                Projectile arrow = new Projectile(
                    controlledUnit.getX() + 20 * direction,
                    controlledUnit.getY() + 25,
                    350f * direction,
                    controlledUnit.getEffectiveAttackPower(), // 2x damage when controlled
                    false, // not spear
                    player
                );
                projectiles.add(arrow);
            }
        }
    }
    
    private void spawnPlayerUnit(String type) {
        int cost = unitFactory.getUnitCost(type);
        if (playerResources.getGold() >= cost) {
            playerResources.spendGold(cost);
            Unit unit = unitFactory.createUnit(type, player);
            unit.setPosition(PLAYER_BASE_X + 120, GROUND_Y);
            playerUnits.add(unit);
        }
    }
    
    private void updateControlledUnit(float delta) {
        if (controlledUnit != null && controlledUnit.isMarkedForRemoval()) {
            controlledUnit = null;
        }
    }

    
    private void updateUnits(float delta) {
        // Update player units
        for (Unit unit : playerUnits) {
            unit.update(delta);
            
            // Skip miners and controlled units
            if (unit instanceof Miner) continue;
            if (unit == controlledUnit) continue;
            
            // Check if blocked by enemy
            boolean blocked = isBlockedByEnemy(unit, enemyUnits);
            
            // Archidonis behavior: stay at range
            if (unit instanceof Archidonis) {
                Entity nearestEnemy = findNearestEnemy(unit, enemyUnits, enemyBase);
                if (nearestEnemy != null) {
                    float dist = Math.abs(nearestEnemy.getX() - unit.getX());
                    if (dist < ARCHIDONIS_RANGE - 50) {
                        // Too close, back up
                        unit.move(delta, -1);
                    } else if (dist > ARCHIDONIS_RANGE + 50 && !blocked) {
                        // Too far, move closer (unless blocked)
                        unit.move(delta, 1);
                    }
                    // Shoot if in range
                    if (dist <= ARCHIDONIS_RANGE && unit.isAttackReady()) {
                        shootArrow(unit, nearestEnemy, player);
                        unit.resetAttackCooldown();
                    }
                }
                continue;
            }
            
            // Regular combat units
            if (!blocked) {
                float targetX;
                switch (playerArmyState) {
                    case ATTACK: targetX = ENEMY_BASE_X; break;
                    case RETREAT: targetX = PLAYER_BASE_X + 150; break;
                    default: targetX = defendLineX; break;
                }
                
                if (Math.abs(unit.getX() - targetX) > 10) {
                    int direction = unit.getX() < targetX ? 1 : -1;
                    unit.move(delta, direction);
                }
            }
        }
        
        // Update enemy units
        for (Unit unit : enemyUnits) {
            unit.update(delta);
            if (unit instanceof Miner) continue;
            
            boolean blocked = isBlockedByEnemy(unit, playerUnits);
            
            // Enemy Archidonis
            if (unit instanceof Archidonis) {
                Entity nearestPlayer = findNearestEnemy(unit, playerUnits, playerBase);
                if (nearestPlayer != null) {
                    float dist = Math.abs(nearestPlayer.getX() - unit.getX());
                    if (dist < ARCHIDONIS_RANGE - 50) {
                        unit.move(delta, 1); // Back up
                    } else if (dist > ARCHIDONIS_RANGE + 50 && !blocked) {
                        unit.move(delta, -1); // Move closer
                    }
                    if (dist <= ARCHIDONIS_RANGE && unit.isAttackReady()) {
                        shootArrow(unit, nearestPlayer, enemy);
                        unit.resetAttackCooldown();
                    }
                }
                continue;
            }
            
            // Regular enemy units march toward player base
            if (!blocked && unit.getX() > PLAYER_BASE_X + 50) {
                unit.move(delta, -1);
            }
        }
    }
    
    private boolean isBlockedByEnemy(Unit unit, List<Unit> enemies) {
        for (Unit enemy : enemies) {
            if (enemy instanceof Miner) continue;
            float dist = Math.abs(enemy.getX() - unit.getX());
            if (dist < UNIT_COLLISION_DIST) {
                return true;
            }
        }
        return false;
    }
    
    private void shootArrow(Unit archer, Entity target, Player owner) {
        float direction = target.getX() > archer.getX() ? 1 : -1;
        Projectile arrow = new Projectile(
            archer.getX() + 15 * direction,
            archer.getY() + 25,
            300f * direction,
            archer.getAttackPower(),
            false,
            owner
        );
        projectiles.add(arrow);
    }
    
    private void updateMiners(float delta) {
        // Player miners collect from gold piles
        for (Unit unit : playerUnits) {
            if (!(unit instanceof Miner)) continue;
            Miner miner = (Miner) unit;
            
            if (miner.getCarriedGold() >= miner.getGoldCapacity()) {
                // Return to base
                if (miner.getX() > PLAYER_BASE_X + 80) {
                    miner.move(delta, -1);
                } else {
                    int deposited = miner.depositGold();
                    playerResources.addGold(deposited);
                }
            } else {
                GoldPile nearest = findNearestGoldPile(miner.getX());
                if (nearest != null) {
                    if (Math.abs(miner.getX() - nearest.x) < 30) {
                        int collected = nearest.collectGold(10);
                        miner.collectGold(collected);
                    } else {
                        int direction = miner.getX() < nearest.x ? 1 : -1;
                        miner.move(delta, direction);
                    }
                }
            }
        }
        
        // Enemy miners just walk back and forth (visual only)
        for (Unit unit : enemyUnits) {
            if (!(unit instanceof Miner)) continue;
            Miner miner = (Miner) unit;
            
            // Simple back-and-forth behavior
            if (miner.getX() < ENEMY_BASE_X - 200) {
                miner.move(delta, 1);
            } else if (miner.getX() > ENEMY_BASE_X - 80) {
                miner.move(delta, -1);
            }
        }
    }
    
    private GoldPile findNearestGoldPile(float x) {
        GoldPile nearest = null;
        float nearestDist = Float.MAX_VALUE;
        for (GoldPile pile : goldPiles) {
            if (pile.isEmpty()) continue;
            float dist = Math.abs(pile.x - x);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = pile;
            }
        }
        return nearest;
    }

    
    private void processCombat(float delta) {
        // Player units attack enemies in range
        for (Unit attacker : playerUnits) {
            if (attacker instanceof Miner) continue;
            if (attacker instanceof Archidonis) continue; // Archidonis use arrows
            
            // Find enemy in melee range
            for (Unit target : enemyUnits) {
                if (target instanceof Miner) continue;
                float dist = Math.abs(target.getX() - attacker.getX());
                if (dist < UNIT_COLLISION_DIST && attacker.isAttackReady()) {
                    combatSystem.processAttack(attacker, target);
                    attacker.resetAttackCooldown();
                    break;
                }
            }
            
            // Attack enemy base if close
            float baseDist = Math.abs(enemyBase.getX() - attacker.getX());
            if (baseDist < 60 && attacker.isAttackReady()) {
                combatSystem.processAttack(attacker, enemyBase);
                attacker.resetAttackCooldown();
            }
        }
        
        // Enemy units attack player units
        for (Unit attacker : enemyUnits) {
            if (attacker instanceof Miner) continue;
            if (attacker instanceof Archidonis) continue;
            
            for (Unit target : playerUnits) {
                if (target instanceof Miner) continue;
                float dist = Math.abs(target.getX() - attacker.getX());
                if (dist < UNIT_COLLISION_DIST && attacker.isAttackReady()) {
                    combatSystem.processAttack(attacker, target);
                    attacker.resetAttackCooldown();
                    break;
                }
            }
            
            // Attack player base
            float baseDist = Math.abs(playerBase.getX() - attacker.getX());
            if (baseDist < 60 && attacker.isAttackReady()) {
                combatSystem.processAttack(attacker, playerBase);
                attacker.resetAttackCooldown();
            }
        }
    }
    
    private void updateProjectiles(float delta) {
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.update(delta);
            
            // Check collisions
            if (p.owner == player) {
                // Player projectile hits enemies
                for (Unit enemy : enemyUnits) {
                    if (p.alreadyHit.contains(enemy)) continue;
                    if (Math.abs(enemy.getX() - p.x) < 20 && Math.abs(enemy.getY() - p.y) < 30) {
                        enemy.takeDamage(p.damage);
                        p.alreadyHit.add(enemy);
                        if (!p.isSpear) {
                            p.hitSomething = true;
                        }
                    }
                }
                
                // Spear hits enemy base for 20% damage
                if (p.isSpear && Math.abs(enemyBase.getX() - p.x) < 40) {
                    int baseDamage = (int)(enemyBase.getMaxHp() * 0.20f);
                    enemyBase.takeDamage(baseDamage);
                    p.hitSomething = true;
                }
            } else {
                // Enemy projectile hits player units
                for (Unit playerUnit : playerUnits) {
                    if (p.alreadyHit.contains(playerUnit)) continue;
                    if (Math.abs(playerUnit.getX() - p.x) < 20 && Math.abs(playerUnit.getY() - p.y) < 30) {
                        playerUnit.takeDamage(p.damage);
                        p.alreadyHit.add(playerUnit);
                        if (!p.isSpear) {
                            p.hitSomething = true;
                        }
                    }
                }
                
                // Enemy spear hits player base
                if (p.isSpear && Math.abs(playerBase.getX() - p.x) < 40) {
                    int baseDamage = (int)(playerBase.getMaxHp() * 0.20f);
                    playerBase.takeDamage(baseDamage);
                    p.hitSomething = true;
                }
            }
            
            // Remove if hit something (non-spear) or off screen
            if (p.hitSomething || p.isOffScreen()) {
                it.remove();
            }
        }
    }
    
    private Entity findNearestEnemy(Unit seeker, List<Unit> enemies, CommandCenter base) {
        Entity nearest = null;
        float nearestDist = Float.MAX_VALUE;
        
        for (Unit enemy : enemies) {
            float dist = Math.abs(enemy.getX() - seeker.getX());
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = enemy;
            }
        }
        
        if (base != null) {
            float dist = Math.abs(base.getX() - seeker.getX());
            if (dist < nearestDist) {
                nearest = base;
            }
        }
        
        return nearest;
    }
    
    private void removeDeadUnits(List<Unit> units) {
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u.isMarkedForRemoval()) {
                if (u == controlledUnit) {
                    controlledUnit = null;
                }
                it.remove();
            }
        }
    }

    
    private void renderBackground() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Ground
        shapeRenderer.setColor(0.4f, 0.7f, 0.3f, 1);
        shapeRenderer.rect(0, 0, SCREEN_WIDTH, GROUND_Y + 50);
        
        // Darker ground line
        shapeRenderer.setColor(0.3f, 0.5f, 0.2f, 1);
        shapeRenderer.rect(0, GROUND_Y - 10, SCREEN_WIDTH, 20);
        
        shapeRenderer.end();
    }
    
    private void renderGoldPiles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Player gold piles (bright gold)
        shapeRenderer.setColor(1f, 0.85f, 0f, 1);
        for (GoldPile pile : goldPiles) {
            if (!pile.isEmpty()) {
                float size = 15 + (pile.goldAmount / 30f);
                shapeRenderer.circle(pile.x, pile.y + 10, size);
            }
        }
        
        // Enemy dummy gold piles (darker gold)
        shapeRenderer.setColor(0.8f, 0.65f, 0f, 1);
        for (GoldPile pile : enemyGoldPiles) {
            float size = 15 + (pile.goldAmount / 30f);
            shapeRenderer.circle(pile.x, pile.y + 10, size);
        }
        
        shapeRenderer.end();
    }
    
    private void renderBuildings() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Player base
        shapeRenderer.setColor(0.3f, 0.5f, 0.9f, 1);
        shapeRenderer.rect(playerBase.getX(), playerBase.getY(), 80, 120);
        
        // Player barracks
        shapeRenderer.setColor(0.4f, 0.6f, 0.8f, 1);
        shapeRenderer.rect(playerBarracks.getX(), playerBarracks.getY(), 60, 80);
        
        // Enemy base
        shapeRenderer.setColor(0.9f, 0.3f, 0.3f, 1);
        shapeRenderer.rect(enemyBase.getX(), enemyBase.getY(), 80, 120);
        
        // Enemy barracks
        shapeRenderer.setColor(0.8f, 0.4f, 0.4f, 1);
        shapeRenderer.rect(enemyBarracks.getX() - 60, enemyBarracks.getY(), 60, 80);
        
        shapeRenderer.end();
        
        // Health bars
        renderHealthBar(playerBase.getX() + 10, playerBase.getY() + 130, 60, playerBase.getHp(), playerBase.getMaxHp(), Color.BLUE);
        renderHealthBar(enemyBase.getX() + 10, enemyBase.getY() + 130, 60, enemyBase.getHp(), enemyBase.getMaxHp(), Color.RED);
    }
    
    private void renderUnits() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Player units
        for (Unit unit : playerUnits) {
            if (unit == controlledUnit) {
                shapeRenderer.setColor(0f, 1f, 0f, 1); // Green for controlled
            } else if (unit instanceof Miner) {
                shapeRenderer.setColor(0.6f, 0.8f, 1f, 1);
            } else if (unit instanceof Archidonis) {
                shapeRenderer.setColor(0.4f, 0.4f, 0.9f, 1);
            } else if (unit instanceof Spearton) {
                shapeRenderer.setColor(0.2f, 0.2f, 0.7f, 1);
            } else {
                shapeRenderer.setColor(0.2f, 0.4f, 0.9f, 1);
            }
            drawStickman(unit.getX(), unit.getY(), true, unit instanceof Archidonis, unit instanceof Spearton);
        }
        
        // Enemy units
        for (Unit unit : enemyUnits) {
            if (unit instanceof Miner) {
                shapeRenderer.setColor(1f, 0.6f, 0.6f, 1);
            } else if (unit instanceof Archidonis) {
                shapeRenderer.setColor(0.9f, 0.4f, 0.4f, 1);
            } else if (unit instanceof Spearton) {
                shapeRenderer.setColor(0.7f, 0.2f, 0.2f, 1);
            } else {
                shapeRenderer.setColor(0.9f, 0.2f, 0.2f, 1);
            }
            drawStickman(unit.getX(), unit.getY(), false, unit instanceof Archidonis, unit instanceof Spearton);
        }
        
        shapeRenderer.end();
        
        // Health bars for damaged units
        for (Unit unit : playerUnits) {
            if (unit.getHp() < unit.getMaxHp()) {
                renderHealthBar(unit.getX() - 15, unit.getY() + 55, 30, unit.getHp(), unit.getMaxHp(), Color.GREEN);
            }
        }
        for (Unit unit : enemyUnits) {
            if (unit.getHp() < unit.getMaxHp()) {
                renderHealthBar(unit.getX() - 15, unit.getY() + 55, 30, unit.getHp(), unit.getMaxHp(), Color.RED);
            }
        }
        
        // Selection indicator for controlled unit
        if (controlledUnit != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(controlledUnit.getX(), controlledUnit.getY() + 25, 25);
            shapeRenderer.end();
        }
    }
    
    private void drawStickman(float x, float y, boolean facingRight, boolean hasbow, boolean hasShield) {
        float headRadius = 8;
        float bodyHeight = 25;
        float legLength = 15;
        
        // Head
        shapeRenderer.circle(x, y + bodyHeight + headRadius, headRadius);
        
        // Body
        shapeRenderer.rect(x - 2, y + legLength, 4, bodyHeight);
        
        // Legs
        float legSpread = facingRight ? 5 : -5;
        shapeRenderer.rect(x - 4, y, 4, legLength);
        shapeRenderer.rect(x + legSpread, y, 4, legLength);
        
        // Arms/weapon
        float armDir = facingRight ? 1 : -1;
        if (hasbow) {
            // Bow shape
            shapeRenderer.rect(x + 8 * armDir, y + bodyHeight - 10, 3, 20);
        } else if (hasShield) {
            // Shield
            shapeRenderer.rect(x + 10 * armDir, y + legLength, 8, 20);
        } else {
            // Sword/arm
            shapeRenderer.rect(x, y + bodyHeight - 5, 15 * armDir, 4);
        }
    }
    
    private void renderProjectiles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for (Projectile p : projectiles) {
            if (p.isSpear) {
                shapeRenderer.setColor(0.6f, 0.4f, 0.2f, 1); // Brown spear
                shapeRenderer.rect(p.x - 15, p.y - 2, 30, 4);
            } else {
                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1); // Gray arrow
                shapeRenderer.rect(p.x - 8, p.y - 1, 16, 2);
            }
        }
        
        shapeRenderer.end();
    }
    
    private void renderHealthBar(float x, float y, float width, int hp, int maxHp, Color color) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, 6);
        float healthPercent = (float) hp / maxHp;
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width * healthPercent, 6);
        shapeRenderer.end();
    }

    
    private void renderUI() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Unit spawn buttons
        shapeRenderer.setColor(0.8f, 0.7f, 0.3f, 1);
        shapeRenderer.rect(minerButton.x, minerButton.y, minerButton.width, minerButton.height);
        shapeRenderer.rect(swordwrathButton.x, swordwrathButton.y, swordwrathButton.width, swordwrathButton.height);
        shapeRenderer.rect(archidonisButton.x, archidonisButton.y, archidonisButton.width, archidonisButton.height);
        shapeRenderer.rect(speartonButton.x, speartonButton.y, speartonButton.width, speartonButton.height);
        
        // Army state buttons
        shapeRenderer.setColor(playerArmyState == ArmyState.ATTACK ? Color.RED : Color.DARK_GRAY);
        shapeRenderer.rect(attackButton.x, attackButton.y, attackButton.width, attackButton.height);
        shapeRenderer.setColor(playerArmyState == ArmyState.DEFEND ? Color.BLUE : Color.DARK_GRAY);
        shapeRenderer.rect(defendButton.x, defendButton.y, defendButton.width, defendButton.height);
        shapeRenderer.setColor(playerArmyState == ArmyState.RETREAT ? Color.GREEN : Color.DARK_GRAY);
        shapeRenderer.rect(retreatButton.x, retreatButton.y, retreatButton.width, retreatButton.height);
        
        shapeRenderer.end();
        
        batch.begin();
        
        // Gold display
        font.setColor(Color.YELLOW);
        font.draw(batch, "Gold: " + playerResources.getGold(), 20, SCREEN_HEIGHT - 10);
        
        // Unit count
        font.setColor(Color.WHITE);
        font.draw(batch, "Units: " + playerUnits.size(), 150, SCREEN_HEIGHT - 10);
        
        // Button labels
        font.setColor(Color.BLACK);
        font.draw(batch, "1", minerButton.x + 25, minerButton.y + 45);
        font.draw(batch, "150", minerButton.x + 15, minerButton.y + 15);
        font.draw(batch, "2", swordwrathButton.x + 25, swordwrathButton.y + 45);
        font.draw(batch, "200", swordwrathButton.x + 15, swordwrathButton.y + 15);
        font.draw(batch, "3", archidonisButton.x + 25, archidonisButton.y + 45);
        font.draw(batch, "350", archidonisButton.x + 15, archidonisButton.y + 15);
        font.draw(batch, "4", speartonButton.x + 25, speartonButton.y + 45);
        font.draw(batch, "500", speartonButton.x + 15, speartonButton.y + 15);
        
        font.setColor(Color.WHITE);
        font.draw(batch, "ATK", attackButton.x + 8, attackButton.y + 30);
        font.draw(batch, "DEF", defendButton.x + 8, defendButton.y + 30);
        font.draw(batch, "RET", retreatButton.x + 8, retreatButton.y + 30);
        
        // Controls hint
        font.draw(batch, "A/D/R=Army | 1-4=Spawn | Click unit to control | SPACE=Special | ESC=Deselect", 350, SCREEN_HEIGHT - 10);
        
        // Controlled unit info
        if (controlledUnit != null) {
            font.setColor(Color.GREEN);
            String unitType = controlledUnit.getClass().getSimpleName();
            font.draw(batch, "Controlling: " + unitType + " (2x DMG) | Arrow keys to move", 350, 30);
            if (controlledUnit instanceof Spearton) {
                font.draw(batch, "SPACE = Throw Spear (20% base damage, penetrates all)", 350, 50);
            } else if (controlledUnit instanceof Archidonis) {
                font.draw(batch, "SPACE = Shoot Arrow at nearest enemy", 350, 50);
            }
        }
        
        batch.end();
    }
    
    private void checkVictoryConditions() {
        if (playerBase.isDestroyed()) {
            game.showDefeat();
            game.setScreen(new LevelSelectScreen(game, upgradeManager));
        } else if (enemyBase.isDestroyed()) {
            // Award rewards: 10 Gems and 5 Upgrade Points
            upgradeManager.addGems(10);
            upgradeManager.addUpgradePoints(5);
            game.showVictory(10, 5);
            
            if (levelConfig.isFinalLevel()) {
                game.setScreen(new CampaignCompleteScreen(game, upgradeManager));
            } else {
                game.setScreen(new LevelSelectScreen(game, upgradeManager));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
    
    // Getters for testing
    public CommandCenter getPlayerBase() { return playerBase; }
    public CommandCenter getEnemyBase() { return enemyBase; }
    public ResourceManager getPlayerResources() { return playerResources; }
    public LevelConfig getLevelConfig() { return levelConfig; }
    public AIController getAiController() { return aiController; }
    public ArmyState getPlayerArmyState() { return playerArmyState; }
    public Unit getControlledUnit() { return controlledUnit; }
}
