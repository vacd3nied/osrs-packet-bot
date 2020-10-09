package net.runelite.api;

/**
 * A bitmap Font in Jagex's format
 */
public interface FontTypeFace
{
    int getTextWidth(String text);

    int getBaseline();
}
