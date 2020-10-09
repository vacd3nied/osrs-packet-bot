package net.runelite.api.packet;

import java.util.function.Function;

import net.runelite.api.ClientPacket;
import net.runelite.api.GamePacket;
import net.runelite.api.NetWriter;
import net.runelite.api.PacketBuffer;
import net.runelite.api.packet.client.*;
import net.runelite.api.packet.server.NPCUpdatePacket;
import net.runelite.api.packet.server.PlayerUpdatePacket;

import static net.runelite.api.packet.PacketType.*;

public enum GamePacketInfo {

//    /**
//     * Input Events
//     */
//    MOUSE_RECORDER(CLIENT, NetWriter::getMouseRecorderClickedPacket, MouseRecorderPacket::fromBuffer, MouseRecorderPacket.class),
//    CLICK_EVENT(CLIENT, NetWriter::getMouseClickedClientPacket, MouseClickPacket::fromBuffer, MouseClickPacket.class),
//    WINDOW_FOCUS(CLIENT, NetWriter::getWindowFocusPacket, WindowFocusPacket::fromBuffer, WindowFocusPacket.class),
//
//    WIDGET_ACTION1(CLIENT, NetWriter::getWidgetMenuClickedPacket, WidgetAction1Packet::fromBuffer, WidgetAction1Packet.class),
//    WIDGET_CLOSED(CLIENT, NetWriter::getWidgetClosedPacket, WidgetClosedPacket::fromBuffer, WidgetClosedPacket.class),
//
//    DROP_ITEM(CLIENT, NetWriter::getDropItemPacket, DropItemPacket::fromBuffer, DropItemPacket.class),
//
//    DIALOG(CLIENT, NetWriter::getDialogPacket, DialogPacket::fromBuffer, DialogPacket.class),
//
//    CAMERA_PACKET(CLIENT, NetWriter::getCameraAnglePacket, CameraAnglePacket::fromBuffer, CameraAnglePacket.class),
//
//    /**
//     * Object actions
//     */
//
//    OBJECT_ACTION1(CLIENT, NetWriter::getObjectAction1Packet, ObjectAction1Packet::fromBuffer, ObjectAction1Packet.class),
//    OBJECT_ACTION2(CLIENT, NetWriter::getObjectAction2Packet, ObjectAction2Packet::fromBuffer, ObjectAction2Packet.class),
//    OBJECT_ACTION3(CLIENT, NetWriter::getObjectAction3Packet, ObjectAction3Packet::fromBuffer, ObjectAction3Packet.class),
//
//    WALK_TILE(CLIENT, NetWriter::getWalkClientPacket, WalkTilePacket::fromBuffer, WalkTilePacket.class),
//    WALK_MINIMAP(CLIENT, NetWriter::getWalkMinimapPacket, WalkMinimapPacket::fromBuffer, WalkMinimapPacket.class),
//
//    ITEM_ACTION1(CLIENT, NetWriter::getItemAction1Packet, ItemAction1Packet::fromBuffer, ItemAction1Packet.class),
//    ITEM_ON_ITEM(CLIENT, NetWriter::getItemOnItemPacket, ItemOnItemPacket::fromBuffer, ItemOnItemPacket.class),
//    ITEM_ON_GAME_OBJECT(CLIENT, NetWriter::getItemOnGameObjectPacket, ItemOnGameObjectPacket::fromBuffer, ItemOnGameObjectPacket.class),
//    ITEM_ON_PLAYER(CLIENT, NetWriter::getItemOnPlayerPacket, ItemOnPlayerPacket::fromBuffer, ItemOnPlayerPacket.class),
//    ITEM_ON_NPC(CLIENT, NetWriter::getItemOnNPCPacket, ItemOnNPCPacket::fromBuffer, ItemOnNPCPacket.class),
//    ITEM_ON_GROUND_ITEM(CLIENT, NetWriter::getItemOnGroundItemPacket, ItemOnGroundItemPacket::fromBuffer, ItemOnGroundItemPacket.class),
//
//    ITEM_EXAMINE(CLIENT, NetWriter::getItemExaminePacket, ItemExaminePacket::fromBuffer, ItemExaminePacket.class),
//    NPC_EXAMINE(CLIENT, NetWriter::getNPCExaminePacket, NPCExaminePacket::fromBuffer, NPCExaminePacket.class),
//    OBJECT_EXAMINE(CLIENT, NetWriter::getObjectExaminePacket, ObjectExaminePacket::fromBuffer, ObjectExaminePacket.class),
//
//    /**
//     * NPC actions
//     */
//    NPC_ATTACK(CLIENT, NetWriter::getNPCAttackPacket, NPCAttackPacket::fromBuffer, NPCAttackPacket.class),
//    NPC_ACTION1(CLIENT, NetWriter::getNPCAction1Packet, NPCAction1Packet::fromBuffer, NPCAction1Packet.class),
//    NPC_ACTION2(CLIENT, NetWriter::getNPCAction2Packet, NPCAction2Packet::fromBuffer, NPCAction2Packet.class),
//    NPC_ACTION3(CLIENT, NetWriter::getNPCAction3Packet, NPCAction3Packet::fromBuffer, NPCAction3Packet.class),
//    NPC_ACTION4(CLIENT, NetWriter::getNPCAction4Packet, NPCAction4Packet::fromBuffer, NPCAction4Packet.class),
//
//    PLAYER_ACTION1(CLIENT, NetWriter::getPlayerAction1Packet, PlayerAction1Packet::fromBuffer, PlayerAction1Packet.class),
//    PLAYER_ACTION2(CLIENT, NetWriter::getPlayerAction2Packet, PlayerAction2Packet::fromBuffer, PlayerAction2Packet.class),
//
//    FLUSH_PACKET(CLIENT, NetWriter::getFlushPacket, FlushPacket::fromBuffer, FlushPacket.class),
//    HEART_BEAT(CLIENT, NetWriter::getHeartBeatPacket, HeartBeatPacket::fromBuffer, HeartBeatPacket.class),
//
//    /**
//     * Server Packets
//     */
//
//    UPDATE_NPC(SERVER, NetWriter::getUpdateNPCPacket, NPCUpdatePacket::fromBuffer, NPCUpdatePacket.class),
//    UPDATE_PLAYER(SERVER, NetWriter::getUpdatePlayersPacket, PlayerUpdatePacket::fromBuffer, PlayerUpdatePacket.class);

    ;

    private final PacketType packetType;
    private final Function<NetWriter, GamePacket> clientPacketSupplier;
    private final Function<PacketBuffer, Packet> reader;
    private final Class<? extends Packet> clazz;

    GamePacketInfo(final PacketType packetType,
                   final Function<NetWriter, GamePacket> clientPacketSupplier,
                   final Function<PacketBuffer, Packet> reader,
                   final Class<? extends Packet> clazz) {
        this.packetType = packetType;
        this.clientPacketSupplier = clientPacketSupplier;
        this.reader = reader;
        this.clazz = clazz;
    }

    public Function<PacketBuffer, Packet> getReader() {
        return reader;
    }

    public Class<? extends Packet> getHandler() {
        return clazz;
    }

    public GamePacket getPacketInfo(final NetWriter netWriter) {
        return clientPacketSupplier.apply(netWriter);
    }

    public static GamePacketInfo fromHandler(final Packet packet) {
        final Class<? extends Packet> packetClass = packet.getClass();

        for (final GamePacketInfo value : values()) {
            if (value.clazz.equals(packetClass)) {
                return value;
            }
        }

        return null;
    }

    public static GamePacketInfo fromGamePacket(final NetWriter netWriter, final GamePacket gamePacket) {
        boolean isClientPacket = gamePacket instanceof ClientPacket;

        for (final GamePacketInfo value : values()) {
            if (value.getPacketInfo(netWriter).getPacketId() == gamePacket.getPacketId() &&
                    ((isClientPacket && value.packetType == CLIENT) || (!isClientPacket && value.packetType == PacketType.SERVER)))
                return value;
        }

        return null;
    }
}
