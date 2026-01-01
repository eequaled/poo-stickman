package com.stickwar.legacy.world;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

import java.util.List;

/**
 * Manages territory ownership based on unit positions.
 * Calculates frontline and updates tile ownership.
 */
public class TerritoryManager {
    // World units per tile (kept in sync with GameScreen rendering tile width)
    public static final int WORLD_UNITS_PER_TILE = 20;

    private final GameMap map;

    public TerritoryManager(GameMap map) {
        this.map = map;
    }

    /**
     * Updates tile ownership based on unit positions.
     * Iterates through 2D grid to calculate frontline.
     * All tiles with x <= max(unit.x) are owned by the player.
     *
     * @param player The player whose territory to update
     * @param units  List of all units to consider
     */
    public void updateFrontline(Player player, List<Unit> units) {
        int maxTileX = 0;
        for (Unit unit : units) {
            if (unit.getOwner() == player) {
                int tileX = (int) (unit.getX() / WORLD_UNITS_PER_TILE);
                maxTileX = Math.max(maxTileX, tileX);
            }
        }
        
        // Update ownership for all tiles up to frontline
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                if (x <= maxTileX) {
                    tile.setOwner(player);
                }
            }
        }
    }

    /**
     * Calculates the frontline position for a player.
     *
     * @param player The player to calculate frontline for
     * @param units  List of all units
     * @return The maximum x position of the player's units
     */
    public int calculateFrontline(Player player, List<Unit> units) {
        int maxTileX = 0;
        for (Unit unit : units) {
            if (unit.getOwner() == player) {
                int tileX = (int) (unit.getX() / WORLD_UNITS_PER_TILE);
                maxTileX = Math.max(maxTileX, tileX);
            }
        }
        return maxTileX;
    }

    public GameMap getMap() {
        return map;
    }
}
