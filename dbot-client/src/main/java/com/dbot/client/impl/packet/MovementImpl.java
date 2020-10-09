package com.dbot.client.impl.packet;

import com.dbot.client.algorithm.AStar;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Movement;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.script.PollingScript;
import net.runelite.api.script.Script;
import net.runelite.api.script.ScriptState;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

public class MovementImpl implements Movement {

    private AStar aStar;

    private void load() {
        if (aStar == null) {
            try {
                final ObjectInputStream cfOis = new ObjectInputStream(new FileInputStream("colData.dat"));
                List<Map<Integer, int[][]>> collisionMap = (List<Map<Integer, int[][]>>) cfOis.readObject();

                aStar = new AStar(collisionMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<WorldPoint> getPath(final WorldPoint start, final WorldPoint end) {
        load();

        return aStar.aStar(start, end);
    }

    @Override
    public Script walkTo(final Client client, final WorldPoint goal, final int tolerance) {
        return new PollingScript(0) {
            @Override
            public void poll() {
                if (client.getGameState() != GameState.LOGGED_IN) {
                    setState(ScriptState.STOP);
                    return;
                }

                final WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

                if (playerLocation.distanceTo(goal) <= tolerance) {
                    setState(ScriptState.STOP);
                    return;
                }

                final LocalPoint destLocation = client.getLocalDestinationLocation();
                final List<WorldPoint> path = getPath(playerLocation, goal);

                if (path == null) {
                    setState(ScriptState.STOP);
                    return;
                }

                if (destLocation != null) {
                    final WorldPoint worldPoint = WorldPoint.fromLocal(client, destLocation);

                    if (path.contains(worldPoint) && playerLocation.distanceTo(worldPoint) > 10) {
                        return;
                    }
                }

                if (!path.isEmpty() && lastPacket + 1000 < System.currentTimeMillis()) {
                    final WorldPoint nextDest = getNextDest(playerLocation, path);

                    final ClientWalkPacket walkMinimap = ClientWalkPacket.fromDestination(nextDest);
                    walkMinimap.writePacket(client.getNetWriter());
                    lastPacket = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public boolean canReach(Client client, WorldPoint goal) {

        return false;
    }

    private WorldPoint getNextDest(final WorldPoint playerLocation, final List<WorldPoint> path) {
        WorldPoint dest = path.get(path.size() - 1);
        long n = Math.round(15 + Math.random() * 15);

        for (int i = path.size() - 2; i >= 0; i--) {
            if (playerLocation.distanceTo(dest) < n) {
                return dest;
            }
            dest = path.get(i);
        }

        return dest;
    }
}
