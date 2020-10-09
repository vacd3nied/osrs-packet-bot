package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_NPC_ACTION_3")
@EqualsAndHashCode(callSuper = true)
public class ClientNpcAction3 extends ClientPacket {

    private final int npcIndex;
    private final int ctrl;

}
