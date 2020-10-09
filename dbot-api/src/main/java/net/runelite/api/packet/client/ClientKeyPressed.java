package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_KEY_PRESSED_PACKET")
@EqualsAndHashCode(callSuper = true)
public class ClientKeyPressed extends ClientPacket {

    private final int const0 = 0;
    private final int key;
    private final int time;
    private final int length = 4;

    public ClientKeyPressed(int key) {
        this.key = key;
        this.time = 0;
    }
}
