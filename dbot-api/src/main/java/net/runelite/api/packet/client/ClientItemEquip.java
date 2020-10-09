package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;
import net.runelite.api.widgets.WidgetInfo;

@Data
@PacketInfo("CLIENT_ITEM_EQUIP")
@EqualsAndHashCode(callSuper = true)
public class ClientItemEquip extends ClientPacket {

    private final int itemId;
    private final int index;
    private final int widgetId;

}
