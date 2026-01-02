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
 * Upgrade shop screen for spending Gems and Upgrade Points.
 * Building upgrades cost Gems, unit upgrades cost Upgrade Points.
 */
public class UpgradeShopScreen implements Screen {
    private final GameMain game;
    private final UpgradeManager upgradeManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    
    // Building upgrade buttons (Gems)
    private Rectangle statueUpgradeButton;
    private Rectangle barracksUpgradeButton;
    
    // Unit upgrade buttons (Upgrade Points)
    private Rectangle hpUpgradeButton;
    private Rectangle attackUpgradeButton;
    private Rectangle speedUpgradeButton;
    
    // Navigation
    private Rectangle backButton;
    
    // Selected unit type for upgrades
    private String selectedUnitType = "MINER";
    private Rectangle minerButton, swordwrathButton, archidonisButton, speartonButton;

    public UpgradeShopScreen(GameMain game, UpgradeManager upgradeManager) {
        this.game = game;
        this.upgradeManager = upgradeManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        
        // Building upgrade buttons (left side)
        statueUpgradeButton = new Rectangle(100, 400, 200, 50);
        barracksUpgradeButton = new Rectangle(100, 330, 200, 50);
        
        // Unit type selection buttons (center)
        minerButton = new Rectangle(400, 500, 100, 40);
        swordwrathButton = new Rectangle(520, 500, 100, 40);
        archidonisButton = new Rectangle(640, 500, 100, 40);
        speartonButton = new Rectangle(760, 500, 100, 40);
        
        // Unit upgrade buttons (right side)
        hpUpgradeButton = new Rectangle(980, 400, 200, 50);
        attackUpgradeButton = new Rectangle(980, 330, 200, 50);
        speedUpgradeButton = new Rectangle(980, 260, 200, 50);
        
        // Back button
        backButton = new Rectangle(50, 50, 100, 40);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.2f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = 720 - Gdx.input.getY(); // Flip Y coordinate
            
            handleInput(mouseX, mouseY);
        }

        // Draw buttons
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Building upgrade buttons
        drawBuildingUpgradeButton(statueUpgradeButton, upgradeManager.getGems() >= UpgradeManager.STATUE_UPGRADE_COST);
        drawBuildingUpgradeButton(barracksUpgradeButton, upgradeManager.getGems() >= UpgradeManager.BARRACKS_UPGRADE_COST);
        
        // Unit type selection buttons
        drawUnitTypeButton(minerButton, "MINER".equals(selectedUnitType));
        drawUnitTypeButton(swordwrathButton, "SWORDWRATH".equals(selectedUnitType));
        drawUnitTypeButton(archidonisButton, "ARCHIDONIS".equals(selectedUnitType));
        drawUnitTypeButton(speartonButton, "SPEARTON".equals(selectedUnitType));
        
        // Unit upgrade buttons
        drawUnitUpgradeButton(hpUpgradeButton, upgradeManager.getUpgradePoints() >= UpgradeManager.HP_UPGRADE_COST);
        drawUnitUpgradeButton(attackUpgradeButton, upgradeManager.getUpgradePoints() >= UpgradeManager.ATTACK_UPGRADE_COST);
        drawUnitUpgradeButton(speedUpgradeButton, upgradeManager.getUpgradePoints() >= UpgradeManager.SPEED_UPGRADE_COST);
        
        // Back button
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        shapeRenderer.rect(backButton.x, backButton.y, backButton.width, backButton.height);
        
        shapeRenderer.end();

        // Draw text
        batch.begin();
        
        // Title
        font.draw(batch, "Upgrade Shop", 1280/2f - 60, 650);
        
        // Currency display
        font.draw(batch, "Gems: " + upgradeManager.getGems(), 50, 680);
        font.draw(batch, "Upgrade Points: " + upgradeManager.getUpgradePoints(), 250, 680);
        
        // Building upgrades section
        font.draw(batch, "Building Upgrades (Gems)", 100, 480);
        font.draw(batch, "Statue Upgrade (15 Gems)", statueUpgradeButton.x + 10, statueUpgradeButton.y + 35);
        font.draw(batch, "Level " + upgradeManager.getStatueUpgradeLevel() + " -> " + (upgradeManager.getStatueUpgradeLevel() + 1), 
                  statueUpgradeButton.x + 10, statueUpgradeButton.y + 15);
        
        font.draw(batch, "Barracks Upgrade (10 Gems)", barracksUpgradeButton.x + 10, barracksUpgradeButton.y + 35);
        font.draw(batch, "Level " + upgradeManager.getBarracksUpgradeLevel() + " -> " + (upgradeManager.getBarracksUpgradeLevel() + 1), 
                  barracksUpgradeButton.x + 10, barracksUpgradeButton.y + 15);
        
        // Unit type selection
        font.draw(batch, "Select Unit Type:", 400, 550);
        font.draw(batch, "Miner", minerButton.x + 25, minerButton.y + 25);
        font.draw(batch, "Sword", swordwrathButton.x + 20, swordwrathButton.y + 25);
        font.draw(batch, "Archer", archidonisButton.x + 20, archidonisButton.y + 25);
        font.draw(batch, "Spear", speartonButton.x + 20, speartonButton.y + 25);
        
        // Unit upgrades section
        font.draw(batch, "Unit Upgrades (Upgrade Points)", 980, 480);
        font.draw(batch, selectedUnitType + " HP Upgrade (3 Points)", hpUpgradeButton.x + 10, hpUpgradeButton.y + 35);
        font.draw(batch, "Level " + upgradeManager.getUnitHpLevel(selectedUnitType), hpUpgradeButton.x + 10, hpUpgradeButton.y + 15);
        
        font.draw(batch, selectedUnitType + " Attack Upgrade (3 Points)", attackUpgradeButton.x + 10, attackUpgradeButton.y + 35);
        font.draw(batch, "Level " + upgradeManager.getUnitAttackLevel(selectedUnitType), attackUpgradeButton.x + 10, attackUpgradeButton.y + 15);
        
        font.draw(batch, selectedUnitType + " Speed Upgrade (2 Points)", speedUpgradeButton.x + 10, speedUpgradeButton.y + 35);
        font.draw(batch, "Level " + upgradeManager.getUnitSpeedLevel(selectedUnitType), speedUpgradeButton.x + 10, speedUpgradeButton.y + 15);
        
        // Back button
        font.draw(batch, "Back", backButton.x + 30, backButton.y + 25);
        
        batch.end();
    }
    
    private void handleInput(float mouseX, float mouseY) {
        // Building upgrades
        if (statueUpgradeButton.contains(mouseX, mouseY)) {
            upgradeManager.upgradeStatue();
        } else if (barracksUpgradeButton.contains(mouseX, mouseY)) {
            upgradeManager.upgradeBarracks();
        }
        
        // Unit type selection
        else if (minerButton.contains(mouseX, mouseY)) {
            selectedUnitType = "MINER";
        } else if (swordwrathButton.contains(mouseX, mouseY)) {
            selectedUnitType = "SWORDWRATH";
        } else if (archidonisButton.contains(mouseX, mouseY)) {
            selectedUnitType = "ARCHIDONIS";
        } else if (speartonButton.contains(mouseX, mouseY)) {
            selectedUnitType = "SPEARTON";
        }
        
        // Unit upgrades
        else if (hpUpgradeButton.contains(mouseX, mouseY)) {
            upgradeManager.upgradeUnitHp(selectedUnitType);
        } else if (attackUpgradeButton.contains(mouseX, mouseY)) {
            upgradeManager.upgradeUnitAttack(selectedUnitType);
        } else if (speedUpgradeButton.contains(mouseX, mouseY)) {
            upgradeManager.upgradeUnitSpeed(selectedUnitType);
        }
        
        // Navigation
        else if (backButton.contains(mouseX, mouseY)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }
    
    private void drawBuildingUpgradeButton(Rectangle button, boolean canAfford) {
        if (canAfford) {
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1); // Green if affordable
        } else {
            shapeRenderer.setColor(0.6f, 0.3f, 0.3f, 1); // Red if not affordable
        }
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
    }
    
    private void drawUnitTypeButton(Rectangle button, boolean isSelected) {
        if (isSelected) {
            shapeRenderer.setColor(0.8f, 0.8f, 0.2f, 1); // Yellow if selected
        } else {
            shapeRenderer.setColor(0.4f, 0.4f, 0.6f, 1); // Blue if not selected
        }
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
    }
    
    private void drawUnitUpgradeButton(Rectangle button, boolean canAfford) {
        if (canAfford) {
            shapeRenderer.setColor(0.2f, 0.6f, 0.8f, 1); // Blue if affordable
        } else {
            shapeRenderer.setColor(0.6f, 0.3f, 0.3f, 1); // Red if not affordable
        }
        shapeRenderer.rect(button.x, button.y, button.width, button.height);
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