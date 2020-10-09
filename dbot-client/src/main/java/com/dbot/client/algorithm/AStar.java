package com.dbot.client.algorithm;

import net.runelite.api.CollisionDataFlag;
import net.runelite.api.coords.WorldPoint;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.List;

public class AStar {

    private List<Map<Integer, int[][]>> collisionMap;

    public AStar(final List<Map<Integer, int[][]>> map) {
        this.collisionMap = map;
    }

    public static void main(String[] args) {
        try {
            final ObjectInputStream cfOis = new ObjectInputStream(new FileInputStream("colData.dat"));
            List<Map<Integer, int[][]>> collisionMap = (List<Map<Integer, int[][]>>) cfOis.readObject();

            final AStar aStar = new AStar(collisionMap);

            // 3105 3272 3091 3245
            final WorldPoint begin = new WorldPoint(3105, 3272, 0);
            final WorldPoint end = new WorldPoint(3091, 3245, 0);

            System.out.println(aStar.aStar(begin, end));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double heuristic(WorldPoint start, WorldPoint goal) {
        return Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY());
    }

    public LinkedList<WorldPoint> aStar(WorldPoint start, WorldPoint goal) {
        WorldPoint tmp = start;
        start = goal;
        goal = tmp;

        HashSet<WorldPoint> closedSet = new HashSet<>();
        HashSet<WorldPoint> openSet = new HashSet<>();
        openSet.add(start);

        HashMap<WorldPoint, WorldPoint> cameFrom = new HashMap<>();

        HashMap<WorldPoint, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        HashMap<WorldPoint, Double> fScore = new HashMap<>();
        fScore.put(start, heuristic(start, goal));

        while (!openSet.isEmpty()) {
            WorldPoint current = null;
            double lfScore = Double.POSITIVE_INFINITY;
            for (WorldPoint point : openSet) {
                double v = fScore.getOrDefault(point, Double.POSITIVE_INFINITY);
                if (v < lfScore) {
                    lfScore = v;
                    current = point;
                }
            }

            if (current == null) {
                throw new NullPointerException("Current point is null");
            }

            if (current.equals(goal)) {
                final LinkedList<WorldPoint> result = reconstructPath(cameFrom, current);
                Collections.reverse(result);
                return result;
            }

            openSet.remove(current);
            closedSet.add(current);

            for (WorldPoint neighbor : getNeighbors(goal, current)) {
                if (closedSet.contains(neighbor))
                    continue;
                openSet.add(neighbor);
                double score = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + heuristic(current, neighbor);

                if (score >= gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY))
                    continue;

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, score);
                fScore.put(neighbor, score + heuristic(neighbor, goal));
            }
        }

        return null;
    }

    private LinkedList<WorldPoint> reconstructPath(final Map<WorldPoint, WorldPoint> cameFrom, WorldPoint current) {
        LinkedList<WorldPoint> path = new LinkedList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.addFirst(current);
        }

        return path;
    }

    private List<WorldPoint> getNeighbors(final WorldPoint goal, final WorldPoint point) {
        final List<WorldPoint> possibilities = Arrays.asList(north(point), south(point), east(point), west(point));

        if (point.equals(goal)) {
            return possibilities;
        }

        final List<WorldPoint> neighbors = new LinkedList<>();

        for (final WorldPoint possibility : possibilities) {
            if (canWalkTo(point, possibility)) {
                neighbors.add(possibility);
            }
        }

        return neighbors;
    }

    private boolean canWalkTo(final WorldPoint start, final WorldPoint neighbor) {
        Integer collisionData = getCollisionData(start);
        Integer neighborData = getCollisionData(neighbor);

        if (collisionData == null || neighborData == null || (neighborData & CollisionDataFlag.BLOCK_MOVEMENT_FULL) != 0
                || collisionData == -1 || neighborData == -1) {
            return false;
        }

        if (north(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_NORTH) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH) == 0);
        } else if (south(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_NORTH) == 0);
        } else if (east(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_EAST) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_WEST) == 0);
        } else if (west(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_WEST) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_EAST) == 0);
        } else if (northEast(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_NORTH_EAST) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH_WEST) == 0);
        } else if (northWest(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_NORTH_WEST) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH_EAST) == 0);
        } else if (southEast(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH_EAST) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_NORTH_WEST) == 0);
        } else if (southWest(start).equals(neighbor)) {
            return ((collisionData & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH_WEST) == 0) &&
                    ((neighborData & CollisionDataFlag.BLOCK_MOVEMENT_NORTH_EAST) == 0);
        }

        return false;
    }

    private Integer getCollisionData(WorldPoint worldPoint) {
        try {
            int regX = worldPoint.getX() - ((worldPoint.getRegionID() >> 8) << 6);
            int regY = worldPoint.getY() - ((worldPoint.getRegionID() & 0xFF) << 6);
            return collisionMap.get(worldPoint.getPlane()).get(worldPoint.getRegionID())[regX][regY];
        } catch (Exception e) {
            return null;
        }
    }

    private WorldPoint north(WorldPoint init) {
        return new WorldPoint(init.getX(), init.getY() + 1, init.getPlane());
    }

    private WorldPoint south(WorldPoint init) {
        return new WorldPoint(init.getX(), init.getY() - 1, init.getPlane());
    }

    private WorldPoint east(WorldPoint init) {
        return new WorldPoint(init.getX() + 1, init.getY(), init.getPlane());
    }

    private WorldPoint west(WorldPoint init) {
        return new WorldPoint(init.getX() - 1, init.getY(), init.getPlane());
    }

    private WorldPoint northEast(WorldPoint init) {
        return new WorldPoint(init.getX() + 1, init.getY() + 1, init.getPlane());
    }

    private WorldPoint northWest(WorldPoint init) {
        return new WorldPoint(init.getX() - 1, init.getY() + 1, init.getPlane());
    }

    private WorldPoint southEast(WorldPoint init) {
        return new WorldPoint(init.getX() + 1, init.getY() - 1, init.getPlane());
    }

    private WorldPoint southWest(WorldPoint init) {
        return new WorldPoint(init.getX() - 1, init.getY() - 1, init.getPlane());
    }
}
