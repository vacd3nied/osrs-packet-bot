package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_ITEM_ACTION_1")
@EqualsAndHashCode(callSuper = true)
public class ClientItemAction1 extends ClientPacket {

    private final int itemId;
    private final int index;
    private final int widgetId;

}
