package com.stickwar.legacy.world;

import com.stickwar.legacy.core.Player;
import com.stickwar.legacy.entities.Unit;

import java.util.List;

/**
 * Manages territory ownership based on unit positions.
 * Calculates frontline and updates tile ownership.
 */
public class TerritoryManager {
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
        int maxX = 0;
        for (Unit unit : units) {
            if (unit.getOwner() == player) {
                maxX = Math.max(maxX, (int) unit.getX());
            }
        }
        
        // Update ownership for all tiles up to frontline
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Tile tile = map.getTile(x, y);
                if (x <= maxX) {
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
        int maxX = 0;
        for (Unit unit : units) {
            if (unit.getOwner() == player) {
                maxX = Math.max(maxX, (int) unit.getX());
            }
        }
        return maxX;
    }

    public GameMap getMap() {
        return map;
    }
}
