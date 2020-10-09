package net.runelite.api;

import net.runelite.api.packet.GamePacketInfo;

public interface NetWriter {

    void setPacketLength(int length);

    void setServerPacket(ServerPacket packet);

    ServerPacket getServerPacket();

    PacketBuffer getPacketBuffer();

    PacketNode getPacketNode(ClientPacket cp);

    void dispatchPacket(PacketNode node);

    ISAACCipher getISAACCipher();

    void filterOutbound(GamePacketInfo... packetTypes);

    void permitOutbound(GamePacketInfo... packetTypes);

}
