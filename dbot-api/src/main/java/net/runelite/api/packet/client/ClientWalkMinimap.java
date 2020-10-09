package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_WALK_MINIMAP")
@EqualsAndHashCode(callSuper = false)
public class ClientWalkMinimap extends ClientPacket {

    private final int const1 = 18;
    private final int const2 = 57;
    private final int const3 = 0;
    private final int const4 = 0;
    private final int const5 = 89;
    private final int const6 = 63;

    private final int x;
    private final int y;
    private final int localX;
    private final int localY;
    private final int mapAngle;
    private final int ctrl_shift;

    public static ClientWalkMinimap fromDestination(final Client client, final WorldPoint destination) {
        final LocalPoint local = client.getLocalPlayer().getLocalLocation();

        return new ClientWalkMinimap(destination.getX(), destination.getY(), local.getX(), local.getY(), client.getMapAngle(), 0);
    }

}
