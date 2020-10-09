package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_NPC_EXAMINE")
@EqualsAndHashCode(callSuper = true)
public class ClientNpcExamine extends ClientPacket {

    private final int npcId;

}
