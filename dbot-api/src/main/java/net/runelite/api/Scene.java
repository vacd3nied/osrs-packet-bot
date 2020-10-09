package net.runelite.api;

public interface Scene
{
    /**
     * Gets the tiles in the scene
     *
     * @return the tiles in [plane][x][y]
     */
    Tile[][][] getTiles();

    int getDrawDistance();
    void setDrawDistance(int drawDistance);
}
