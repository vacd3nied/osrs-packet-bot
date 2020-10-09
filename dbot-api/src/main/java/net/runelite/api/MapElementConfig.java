package net.runelite.api;

/**
 * Represents an area in the world.
 */
public interface MapElementConfig
{
    /**
     * Gets the sprite icon to display on the world map.
     *
     * @param unused unused value
     * @return the sprite icon to display on the world map
     */
    SpritePixels getMapIcon(boolean unused);
}

