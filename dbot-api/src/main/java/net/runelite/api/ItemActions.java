package net.runelite.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.packet.GamePacketInfo;

@AllArgsConstructor
public enum ItemActions {

    USE("Use", null);
//    DROP("Drop", GamePacketInfo.DROP_ITEM),
//    ITEM_ACTION1("Action 1", GamePacketInfo.ITEM_ACTION1);

    @Getter
    private final String option;

    @Getter
    private final GamePacketInfo packetInfo;

}
