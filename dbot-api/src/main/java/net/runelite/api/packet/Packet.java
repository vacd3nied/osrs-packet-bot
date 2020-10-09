package net.runelite.api.packet;

import lombok.Data;
import net.runelite.api.NetWriter;
import net.runelite.api.PacketBuffer;

import java.io.Serializable;

@Data
public abstract class Packet implements Serializable {

    public static final long serialVersionUID = 477214543423L;

    private long timeMillis = System.currentTimeMillis();

    public abstract void writePacket(final NetWriter netWriter);

}
