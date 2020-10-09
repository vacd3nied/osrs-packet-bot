package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_WALK_PACKET")
@EqualsAndHashCode(callSuper = true)
public class ClientWalkPacket extends ClientPacket {

    private final int constant = 5;
    private final int x;
    private final int y;
    private final int ctrl;

    public static ClientWalkPacket fromDestination(final WorldPoint destination) {
        return new ClientWalkPacket(destination.getX(), destination.getY(), 0);
    }

    public static ClientWalkPacket fromDestination(final Client client, final LocalPoint destination) {
        return fromDestination(WorldPoint.fromLocal(client, destination));
    }
}
