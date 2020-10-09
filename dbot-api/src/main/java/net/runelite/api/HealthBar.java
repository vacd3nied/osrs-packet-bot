package net.runelite.api;


public interface HealthBar
{
    SpritePixels getHealthBarFrontSprite();

    SpritePixels getHealthBarBackSprite();

    int getHealthBarFrontSpriteId();

    void setPadding(int padding);
}
