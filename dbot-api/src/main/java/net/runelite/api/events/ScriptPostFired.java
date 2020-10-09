package net.runelite.api.events;

import lombok.Value;

/**
 * An event that is fired after the designated script is ran
 */
@Value
public class ScriptPostFired
{
    /**
     * The script id of the invoked script
     */
    private final int scriptId;
}
