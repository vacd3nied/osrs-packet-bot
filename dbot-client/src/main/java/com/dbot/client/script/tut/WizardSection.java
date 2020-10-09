package com.dbot.client.script.tut;

import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.TileArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.ClientDialog;
import net.runelite.api.packet.client.ClientSpellOnNPC;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;

import java.util.Optional;

public class WizardSection extends TutorialSection {

    private final TileArea CHICKEN_AREA = new TileArea(3139, 3091, 3142, 3089);

    public WizardSection() {
        guide = "Magic Instructor";
    }

    @Override
    public void poll() {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        if (pendingContinue()) {
            selectContinue();
            return;
        }

        int progress = getProgress();

        if (progress == 620) {
            if (!getInstructor().isPresent()) {
                final ClientWalkPacket walkPacket = new ClientWalkPacket(3134, 3087, 0);
                dispatch(walkPacket);
                sleep(2000);
            } else {
                talkToGuide();
            }
        } else if (progress == 630) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(10747963, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 640) {
            talkToGuide();
        } else if (progress == 650) {
            final WorldPoint location = client.getLocalPlayer().getWorldLocation();
            if (!CHICKEN_AREA.contains(location) && client.getLocalDestinationLocation() == null) {
                dispatch(CHICKEN_AREA.randomTile().walkTo());
                sleep(1000);
            } else {
                final Optional<NPC> chicken = new NPCQuery().nameEquals("Chicken").isInteractingWith(null).closest(client);

                if (chicken.isPresent()) {
                    final ClientSpellOnNPC spellOnNPC = new ClientSpellOnNPC(14286854, chicken.get().getIndex(), -1, 0);
                    dispatch(spellOnNPC);
                    sleep(1000);
                }
            }
        } else if (progress == 670) {
            if (!selectChatOptions("No, I'm not planning to do that.", "Yes.", "I'm fine, thanks.")) {
                talkToGuide();
            }
        }
    }

    private boolean selectChatOptions(final String... options) {
        final Widget widget = client.getWidget(WidgetID.DIALOG_OPTION_GROUP_ID, 1);
        if (widget != null && !widget.isHidden()) {
            final Widget[] children = widget.getDynamicChildren();
            for (int i = 0; i < children.length; i++) {
                for (String option : options) {
                    if (option.equals(children[i].getText())) {
                        final ClientDialog dialog = new ClientDialog(widget.getId(), i);
                        dispatch(dialog);
                        sleep(1000);
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
