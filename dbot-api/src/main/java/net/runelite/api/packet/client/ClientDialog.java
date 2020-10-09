package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_DIALOG")
@EqualsAndHashCode(callSuper = true)
public class ClientDialog extends ClientPacket {

    private final int widgetId;
    private final int chatOption;

}
