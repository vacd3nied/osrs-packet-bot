package net.runelite.api;

public interface PacketNode {

    boolean isAuthentic();

    void setManufactured();

    void setAuthentic();

    PacketBuffer getPacketBuffer();

    void setClientPacket(ClientPacket packet);

    void setPacketBuffer(PacketBuffer buffer);

    void setPacketLength(int length);

    ClientPacket getClientPacket();

}
