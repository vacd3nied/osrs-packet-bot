package net.runelite.api.packet.server;

import net.runelite.api.NetWriter;
import net.runelite.api.PacketBuffer;
import net.runelite.api.packet.GamePacketInfo;
import net.runelite.api.packet.Packet;

public abstract class ServerPacket extends Packet {

    public final void writePacket(final NetWriter netWriter) {
        throw new RuntimeException("TODO");
    }

    public static ServerPacket fromPacketBuffer(final NetWriter netWriter,
                                                final net.runelite.api.ServerPacket type,
                                                final PacketBuffer buffer) {
        final GamePacketInfo gamePacketInfo = GamePacketInfo.fromGamePacket(netWriter, type);

        if (gamePacketInfo == null)
            return null;

        final int initialOffset = buffer.getOffset();
        buffer.setOffset(1);

        final Packet result = gamePacketInfo.getReader().apply(buffer);
        buffer.setOffset(initialOffset);

        return (ServerPacket) result;
    }

}
