package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_ITEM_ON_OBJECT")
@EqualsAndHashCode(callSuper = true)
public class ClientItemOnObject1 extends ClientPacket {

    private final int itemId;
    private final int selectedItemIndex;
    private final int itemContainer;
    private final int x;
    private final int y;
    private final int objectId;
    private final int ctrl;

}
