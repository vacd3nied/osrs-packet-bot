package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_MOUSE_CLICKED_PACKET")
@EqualsAndHashCode(callSuper = true)
public class ClientMouseClickedPacket extends ClientPacket {

    private final int time_click_type;
    private final int x;
    private final int y;

}
