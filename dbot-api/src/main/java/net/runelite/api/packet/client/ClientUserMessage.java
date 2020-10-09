package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_USER_MESSAGE")
@EqualsAndHashCode(callSuper = false)
public class ClientUserMessage extends ClientPacket {

    private final int length;
    private final String value;

    public ClientUserMessage(final String message) {
        length = message.length() + 1;
        value = message;
    }
}
