package net.runelite.api;

public interface Texture extends Node
{
    int[] getPixels();

    int getAnimationDirection();

    int getAnimationSpeed();

    boolean isLoaded();

    float getU();
    void setU(float u);

    float getV();
    void setV(float v);
}
