/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;

import java.awt.Polygon;

import net.runelite.api.coords.Angle;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientObjectAction2;

/**
 * @author Adam
 */
public interface GameObject extends TileObject {
    /**
     * Returns the min x,y for this game object
     *
     * @return
     */
    Point getRegionMinLocation();

    /**
     * Returns the max x,y for this game object. This is different from
     * {@link #getRegionMinLocation()} for objects larger than 1 tile.
     *
     * @return
     */
    Point getRegionMaxLocation();

    Polygon getConvexHull();

    Angle getOrientation();

    default ClientObjectAction1 action1(Client client) {
        final Point regMin = getRegionMinLocation();
        final WorldPoint worldLocation = getWorldLocation();
        final WorldPoint w = new WorldPoint(client.getBaseX() + regMin.getX(), client.getBaseY() + regMin.getY(), worldLocation.getPlane());

        return new ClientObjectAction1(w.getX(), w.getY(), getId(), 0);
    }

    default ClientObjectAction2 action2(Client client) {
        final Point regMin = getRegionMinLocation();
        final WorldPoint worldLocation = getWorldLocation();
        final WorldPoint w = new WorldPoint(client.getBaseX() + regMin.getX(), client.getBaseY() + regMin.getY(), worldLocation.getPlane());

        return new ClientObjectAction2(w.getX(), w.getY(), getId(), 0);
    }
}
