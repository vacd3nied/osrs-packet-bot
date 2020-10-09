package com.dbot.client.script;

import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.script.PollingScript;

import java.util.Arrays;

public class RandomDismiss extends PollingScript {

    private final int[] RANDOM_EVENT_NPCs = {
            NpcID.BEE_KEEPER,
            NpcID.BEE_KEEPER_6747, // not sure which one is for rdm evt
            NpcID.CAPT_ARNAV,
            NpcID.NILES,
            NpcID.NILES_5439,
            NpcID.GILES,
            NpcID.GILES_5441,
            NpcID.SERGEANT_DAMIEN,
            NpcID.SERGEANT_DAMIEN_6743,
            NpcID.DRUNKEN_DWARF,
            NpcID.FREAKY_FORESTER,
            NpcID.EVIL_BOB,
            NpcID.POSTIE_PETE,
            NpcID.POSTIE_PETE_6738
    };

    private long lastDismiss = 0;

    public RandomDismiss() {
        super(10);
    }

    @Override
    public void poll() {
        new NPCQuery().isInteractingWith(client.getLocalPlayer()).filter(this::containsAction).eval(client).forEach(npc -> {
            if (lastDismiss + 2000 < System.currentTimeMillis()) {
                lastDismiss = System.currentTimeMillis();
                dispatch(npc.action4());
                System.err.println("Random event dismissed, npcID=" + npc.getId());
            }
        });
    }

    private boolean containsAction(final NPC npc) {
        return Arrays.stream(npc.getComposition().getActions())
                .anyMatch(action -> action != null && action.toLowerCase().contains("dismiss"));
    }
}
