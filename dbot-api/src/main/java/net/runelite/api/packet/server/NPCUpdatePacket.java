package net.runelite.api.packet.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.PacketBuffer;

@Data
@EqualsAndHashCode(callSuper = true)
public class NPCUpdatePacket extends ServerPacket {


    public static NPCUpdatePacket fromBuffer(final PacketBuffer buffer) {
        return null;
    }
}
