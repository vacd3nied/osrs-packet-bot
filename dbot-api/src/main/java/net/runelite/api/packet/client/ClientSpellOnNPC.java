package net.runelite.api.packet.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.api.packet.PacketInfo;

@Data
@PacketInfo("CLIENT_SPELL_ON_NPC")
@EqualsAndHashCode(callSuper = true)
public class ClientSpellOnNPC extends ClientPacket {

    private int spellWidget;
    private int npcIndex;
    private int param;
    private int ctrl;

    public ClientSpellOnNPC(int spellWidget, int npcIndex, int param, int ctrl) {
        this.spellWidget = spellWidget;
        this.npcIndex = npcIndex;
        this.param = param;
        this.ctrl = ctrl;
    }
}
