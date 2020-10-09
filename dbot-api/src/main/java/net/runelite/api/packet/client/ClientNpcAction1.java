package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_NPC_ACTION_1")
@EqualsAndHashCode(callSuper = true)
public class ClientNpcAction1 extends ClientPacket {

    private final int npcIndex;
    private final int ctrl;

}
