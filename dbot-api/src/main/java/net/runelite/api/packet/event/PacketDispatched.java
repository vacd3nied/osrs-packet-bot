package net.runelite.api.packet.event;

import lombok.Data;
import net.runelite.api.packet.client.ClientPacket;
import net.runelite.api.packet.GamePacketInfo;

@Data
public class PacketDispatched {

    private final int id;
    private final GamePacketInfo type;
    private final ClientPacket packet;

}
