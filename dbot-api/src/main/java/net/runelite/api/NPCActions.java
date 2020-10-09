package net.runelite.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.packet.GamePacketInfo;

@AllArgsConstructor
public enum NPCActions {

//    ATTACK("Attack", GamePacketInfo.NPC_ATTACK),
//    TALK_TO("Talk-to", GamePacketInfo.NPC_ACTION1),
//    PICK_POCKET("Pickpocket", GamePacketInfo.NPC_ACTION2),
//    BANK("Bank", GamePacketInfo.NPC_ACTION2),
//    NPC_ACTION1("Action 1", GamePacketInfo.NPC_ACTION1),
//    NPC_ACTION2("Action 2", GamePacketInfo.NPC_ACTION2),
//    NPC_ACTION3("Action 3", GamePacketInfo.NPC_ACTION3);

    ;

    @Getter
    private final String option;

    @Getter
    private final GamePacketInfo packetInfo;

}
