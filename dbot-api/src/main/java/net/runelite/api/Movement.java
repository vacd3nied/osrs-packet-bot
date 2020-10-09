package net.runelite.api;

import net.runelite.api.coords.WorldPoint;

import java.util.List;

public interface Movement {

    List<WorldPoint> getPath(WorldPoint start, WorldPoint end);

    net.runelite.api.script.Script walkTo(Client client, WorldPoint goal, int tolerance);

    boolean canReach(Client client, WorldPoint goal);
}
