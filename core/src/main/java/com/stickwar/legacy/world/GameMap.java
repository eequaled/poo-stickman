package com.stickwar.legacy.world;

/**
 * Represents the 2D game map as a grid of tiles.
 * Uses nested loop iteration for tile access.
 */
public class GameMap {
    private final Tile[][] tiles;
    private final int width;
    private final int height;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
        generateMap();
    }

    /**
     * Generates the initial map layout.
     * TerritoryManager must iterate 2D.
     */
    private void generateMap() {
        java.util.Random random = new java.util.Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 70% grass, 20% mountain, 10% water
                int roll = random.nextInt(100);
                TileType type;
                if (roll < 70) {
                    type = TileType.GRASS;
                } else if (roll < 90) {
                    type = TileType.MOUNTAIN;
                } else {
                    type = TileType.WATER;
                }
                tiles[x][y] = new Tile(x, y, type);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
