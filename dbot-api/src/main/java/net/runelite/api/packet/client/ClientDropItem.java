package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;
import net.runelite.api.widgets.WidgetInfo;

@Data
@PacketInfo("CLIENT_DROP_ITEM")
@EqualsAndHashCode(callSuper = true)
public class ClientDropItem extends ClientPacket {

    private final int itemId;
    private final int index;
    private final int widgetId;

    public ClientDropItem(int itemId, int index) {
        this.itemId = itemId;
        this.index = index;

        this.widgetId = WidgetInfo.INVENTORY.getId();
    }
}
