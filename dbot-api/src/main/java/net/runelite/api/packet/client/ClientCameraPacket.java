package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_CAMERA_PACKET")
@EqualsAndHashCode(callSuper = true)
public class ClientCameraPacket extends ClientPacket {

    private final int mapAngle;
    private final int cameraPitchTarget;

}
