package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_NPC_ATTACK")
@EqualsAndHashCode(callSuper = true)
public class ClientNpcAttack extends ClientPacket {

    private final int npcIndex;
    private final int ctrl;

}
