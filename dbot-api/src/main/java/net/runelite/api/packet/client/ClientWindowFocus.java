package net.runelite.api.packet.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.runelite.api.packet.PacketInfo;

@Data
@AllArgsConstructor
@PacketInfo("CLIENT_WINDOW_FOCUS")
@EqualsAndHashCode(callSuper = true)
public class ClientWindowFocus extends ClientPacket {

    private int focused = 1;

}
