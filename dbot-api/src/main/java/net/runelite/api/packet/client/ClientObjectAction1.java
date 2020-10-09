package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_OBJECT_ACTION_1")
@EqualsAndHashCode(callSuper = true)
public class ClientObjectAction1 extends ClientPacket {

    private final int x;
    private final int y;
    private final int objectId;
    private final int ctrl;


    public static ClientObjectAction1 fromObject(final Client client, final GameObject obj) {
        final WorldPoint worldLocation = obj.getWorldLocation();
        final Point regionMin = obj.getRegionMinLocation();

        final WorldPoint w = new WorldPoint(client.getBaseX() + regionMin.getX(), client.getBaseY() + regionMin.getY(), worldLocation.getPlane());
        // some objects span multiple tiles

        return new ClientObjectAction1(obj.getId(), w.getX(), w.getY(), 0);
    }

}
