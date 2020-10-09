package net.runelite.api.packet.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.PacketBuffer;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayerUpdatePacket extends ServerPacket {

    public static PlayerUpdatePacket fromBuffer(final PacketBuffer buffer) {
        return null;
    }
}
