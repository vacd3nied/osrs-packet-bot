package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_PLAYER_ACTION_1")
@EqualsAndHashCode(callSuper = true)
public class ClientPlayerAction1 extends ClientPacket {

    private final int playerIndex;
    private final int ctrl;

}
