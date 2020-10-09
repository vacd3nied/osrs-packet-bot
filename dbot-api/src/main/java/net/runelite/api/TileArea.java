package net.runelite.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.coords.WorldPoint;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Line2D;

@Data
@EqualsAndHashCode
public class TileArea {

    private final int x1, y1, x2, y2, plane;
    private final Rectangle bounds;

    public TileArea(int x1, int y1, int x2, int y2, int plane) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.plane = plane;

        bounds = new Line2D.Double(new Point(x1, y1), new Point(x2, y2)).getBounds();
    }

    public TileArea(int x1, int y1, int x2, int y2) {
        this(x1, y1, x2, y2, 0);
    }

    public boolean contains(final WorldPoint worldPoint) {
        if (worldPoint == null)
            return false;
        return bounds.contains(worldPoint.getX(), worldPoint.getY());
    }

    public WorldPoint randomTile() {
        int x = Math.round((float) Math.random() * bounds.width) + bounds.x;
        int y = Math.round((float) Math.random() * bounds.height) + bounds.y;

        return new WorldPoint(x, y, plane);
    }
}
