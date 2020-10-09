package net.runelite.api;

/**
 * Represents a doubly linked node cache.
 */
public interface NodeCache
{
    /**
     * Resets cache.
     */
    void reset();

    void setCapacity(int capacity);

    void setRemainingCapacity(int remainingCapacity);
}