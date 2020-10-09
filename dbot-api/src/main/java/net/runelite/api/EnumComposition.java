package net.runelite.api;

public interface EnumComposition
{
    int[] getKeys();

    int[] getIntVals();

    String[] getStringVals();

    int getIntValue(int key);

    String getStringValue(int key);
}
