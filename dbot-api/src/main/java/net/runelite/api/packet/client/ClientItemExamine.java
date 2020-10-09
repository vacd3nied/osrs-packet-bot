package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_ITEM_EXAMINE")
@EqualsAndHashCode(callSuper = true)
public class ClientItemExamine extends ClientPacket {

    private final int itemId;

}
