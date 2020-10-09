package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_OBJECT_ACTION_2")
@EqualsAndHashCode(callSuper = true)
public class ClientObjectAction2 extends ClientPacket {

    private final int x;
    private final int y;
    private final int objectId;
    private final int ctrl;

}
