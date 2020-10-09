package net.runelite.api.packet.event;

import lombok.Data;
import net.runelite.api.packet.GamePacketInfo;
import net.runelite.api.packet.server.ServerPacket;

@Data
public class PacketReceived {

    private final int id;
    private final GamePacketInfo type;
    private final ServerPacket packet;

}
