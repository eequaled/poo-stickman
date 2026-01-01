package com.stickwar.legacy.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.stickwar.legacy.assets.StolenAssetManager;
import com.stickwar.legacy.entities.Unit;
import com.stickwar.legacy.core.Player;

/**
 * Renderer for stickman units using stolen assets.
 * Replaces placeholder graphics with actual stickman sprites.
 */
public class StickmanUnitRenderer {
    
    private StolenAssetManager assetManager;
    private float unitScale = 0.5f; // Scale factor for stickman sprites
    
    public StickmanUnitRenderer() {
        this.assetManager = StolenAssetManager.getInstance();
    }
    
    /**
     * Renders a unit as a stickman with appropriate coloring.
     */
    public void renderUnit(Unit unit, SpriteBatch batch) {
        // Get base stickman texture
        TextureRegion stickmanFrame = assetManager.getStickmanFrame(0);
        
        // Apply player color tint
        Color playerColor = getPlayerColor(unit.getOwner());
        batch.setColor(playerColor);
        
        // Calculate position and size
        float width = stickmanFrame.getRegionWidth() * unitScale;
        float height = stickmanFrame.getRegionHeight() * unitScale;
        float x = unit.getX() - width / 2;
        float y = unit.getY() - height / 2;
        
        // Draw the stickman
        batch.draw(stickmanFrame, x, y, width, height);
        
        // Reset color
        batch.setColor(Color.WHITE);
        
        // Draw unit-specific equipment
        renderUnitEquipment(unit, batch, x, y, width, height);
        
        // Draw health bar
        renderHealthBar(unit, batch, x, y, width);
    }
    
    /**
     * Renders unit-specific equipment based on unit type.
     */
    private void renderUnitEquipment(Unit unit, SpriteBatch batch, float x, float y, float width, float height) {
        String unitType = unit.getClass().getSimpleName();
        
        switch (unitType) {
            case "Swordwrath":
                renderSword(batch, x, y, width, height);
                break;
            case "Archidonis":
                renderBow(batch, x, y, width, height);
                break;
            case "Spearton":
                renderShieldAndSpear(batch, x, y, width, height);
                break;
            case "Miner":
                renderPickaxe(batch, x, y, width, height);
                break;
        }
    }
    
    /**
     * Renders a sword for Swordwrath units.
     */
    private void renderSword(SpriteBatch batch, float x, float y, float width, float height) {
        // Simple sword representation - will be replaced with actual sword sprite
        batch.setColor(Color.GRAY);
        float swordX = x + width * 0.8f;
        float swordY = y + height * 0.3f;
        float swordWidth = width * 0.3f;
        float swordHeight = height * 0.6f;
        batch.draw(assetManager.getStickmanFrame(0), swordX, swordY, swordWidth, swordHeight);
        batch.setColor(Color.WHITE);
    }
    
    /**
     * Renders a bow for Archidonis units.
     */
    private void renderBow(SpriteBatch batch, float x, float y, float width, float height) {
        batch.setColor(Color.BROWN);
        float bowX = x + width * 0.7f;
        float bowY = y + height * 0.2f;
        float bowWidth = width * 0.2f;
        float bowHeight = height * 0.8f;
        batch.draw(assetManager.getStickmanFrame(0), bowX, bowY, bowWidth, bowHeight);
        batch.setColor(Color.WHITE);
    }
    
    /**
     * Renders shield and spear for Spearton units.
     */
    private void renderShieldAndSpear(SpriteBatch batch, float x, float y, float width, float height) {
        // Shield
        batch.setColor(Color.GOLD);
        float shieldX = x + width * 0.1f;
        float shieldY = y + height * 0.3f;
        float shieldWidth = width * 0.15f;
        float shieldHeight = height * 0.4f;
        batch.draw(assetManager.getStickmanFrame(0), shieldX, shieldY, shieldWidth, shieldHeight);
        
        // Spear
        batch.setColor(Color.BROWN);
        float spearX = x + width * 0.8f;
        float spearY = y + height * 0.1f;
        float spearWidth = width * 0.1f;
        float spearHeight = height * 0.9f;
        batch.draw(assetManager.getStickmanFrame(0), spearX, spearY, spearWidth, spearHeight);
        batch.setColor(Color.WHITE);
    }
    
    /**
     * Renders a pickaxe for Miner units.
     */
    private void renderPickaxe(SpriteBatch batch, float x, float y, float width, float height) {
        batch.setColor(Color.GRAY);
        float pickaxeX = x + width * 0.7f;
        float pickaxeY = y + height * 0.2f;
        float pickaxeWidth = width * 0.25f;
        float pickaxeHeight = height * 0.7f;
        batch.draw(assetManager.getStickmanFrame(0), pickaxeX, pickaxeY, pickaxeWidth, pickaxeHeight);
        batch.setColor(Color.WHITE);
    }
    
    /**
     * Renders a health bar above the unit.
     */
    private void renderHealthBar(Unit unit, SpriteBatch batch, float x, float y, float width) {
        float barWidth = width;
        float barHeight = 4f;
        float barX = x;
        float barY = y - 10f;
        
        // Background (red)
        batch.setColor(Color.RED);
        batch.draw(assetManager.getStickmanFrame(0), barX, barY, barWidth, barHeight);
        
        // Health (green)
        batch.setColor(Color.GREEN);
        float healthPercent = (float) unit.getHp() / unit.getMaxHp();
        float healthWidth = barWidth * healthPercent;
        batch.draw(assetManager.getStickmanFrame(0), barX, barY, healthWidth, barHeight);
        
        batch.setColor(Color.WHITE);
    }
    
    /**
     * Renders a dead unit using the grave sprite.
     */
    public void renderDeadUnit(Unit unit, SpriteBatch batch) {
        TextureRegion graveFrame = assetManager.graveFrame;
        float width = graveFrame.getRegionWidth() * unitScale;
        float height = graveFrame.getRegionHeight() * unitScale;
        float x = unit.getX() - width / 2;
        float y = unit.getY() - height / 2;
        
        batch.draw(graveFrame, x, y, width, height);
    }
    
    /**
     * Gets player-specific color for unit differentiation.
     */
    private Color getPlayerColor(Player player) {
        if (player.isHuman()) {
            return new Color(0.2f, 0.4f, 1.0f, 1.0f); // Blue for player
        } else {
            return new Color(1.0f, 0.2f, 0.2f, 1.0f); // Red for enemy
        }
    }
    
    /**
     * Renders unit attack animation.
     */
    public void renderAttackAnimation(Unit unit, SpriteBatch batch) {
        // TODO: Implement attack animation frames
        // For now, just scale the unit slightly during attack
        float originalScale = unitScale;
        unitScale = 0.6f;
        renderUnit(unit, batch);
        unitScale = originalScale;
    }
    
    /**
     * Renders unit movement animation.
     */
    public void renderMovementAnimation(Unit unit, SpriteBatch batch, float deltaTime) {
        // TODO: Implement walking animation
        // For now, just add a slight bobbing effect
        float bobOffset = (float) Math.sin(System.currentTimeMillis() * 0.01) * 2;
        float originalY = unit.getY();
        unit.setY(originalY + bobOffset);
        renderUnit(unit, batch);
        unit.setY(originalY);
    }
}
