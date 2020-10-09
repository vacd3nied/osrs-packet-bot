package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;
import net.runelite.api.widgets.WidgetInfo;

@Data
@PacketInfo("CLIENT_ITEM_ON_ITEM")
@EqualsAndHashCode(callSuper = true)
public class ClientItemOnItem extends ClientPacket {

    private final int itemA;
    private final int itemB;
    private final int itemContainer;
    private final int itemContainerId;
    private final int targetItemIndex;
    private final int selectedItemIndex;

    public ClientItemOnItem(int itemA, int itemB, int itemAIdx, int itemBIdx, int itemContainer) {
        this.itemA = itemA;
        this.itemB = itemB;
        this.selectedItemIndex = itemAIdx;
        this.targetItemIndex = itemBIdx;
        this.itemContainer = itemContainer;
        this.itemContainerId = itemContainer;
    }

    public ClientItemOnItem(int itemA, int itemB, int itemAIdx, int itemBIdx) {
        this(itemA, itemB, itemAIdx, itemBIdx, WidgetInfo.INVENTORY.getId());
    }

}
