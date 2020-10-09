package net.runelite.api;

public interface IterableHashTable<T extends Node> extends Iterable<T>
{
    T get(long hash);
}

