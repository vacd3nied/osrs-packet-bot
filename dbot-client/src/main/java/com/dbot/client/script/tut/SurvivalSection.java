package com.dbot.client.script.tut;

import net.runelite.api.*;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.queries.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import java.util.List;
import java.util.Optional;

public class SurvivalSection extends TutorialSection {

    public SurvivalSection() {
        guide = "Survival Expert";
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

        if (progress == 20) {
            talkToGuide();
        } else if (progress == 30) {
            final Widget widget = client.getWidget(WidgetInfo.DIALOG_SPRITE);

            if (widget != null && !widget.isHidden()) {
                ClientWalkPacket clientWalkPacket = new ClientWalkPacket(3102, 3096, 0);
                dispatch(clientWalkPacket);
                sleep(500);
                return;
            }

            final ClientWidgetAction1 action = new ClientWidgetAction1(10747960, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 40) {
            fish();
        } else if (progress == 50) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(10747958, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 60) {
            talkToGuide();
        } else if (progress == 70) {
            chopTree();
        } else if (progress >= 80 && progress <= 110) {
            List<WidgetItem> shrimps = inventory.getWidgetItems(new int[]{ItemID.RAW_SHRIMPS, ItemID.RAW_SHRIMPS_2514});

            if (shrimps.isEmpty()) {
                fish();
            } else if (findFire() == null) {
                List<WidgetItem> logs = inventory.getWidgetItems(new int[]{ItemID.LOGS, ItemID.LOGS_2511});
                if (logs.isEmpty()) {
                    chopTree();
                } else {
                    List<WidgetItem> tinder = inventory.getWidgetItems(new int[]{ItemID.TINDERBOX, ItemID.TINDERBOX_7156});

                    if (!tinder.isEmpty()) {
                        dispatch(logs.get(0).useOnItem(tinder.get(0)));
                        sleep(1000);
                    }
                }
            } else {
                GameObject fire = findFire();

                if (fire != null) {
                    dispatch(shrimps.get(0).useOnObject(client, fire));
                    sleep(2000);
                }
            }
        } else if (progress == 120) {
            final ClientObjectAction1 objectAction = new ClientObjectAction1(3089, 3092, 9470, 0);
            dispatch(objectAction);
            sleep(2000);
        }
    }

    private void chopTree() {
        final Optional<GameObject> tree = new GameObjectQuery().containsName("Tree").closest(client);
        tree.ifPresent(t -> dispatch(t.action1(client)));
        tree.ifPresent(t -> System.out.println(t.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())));
        sleep(2000);
    }

    private boolean isStandingOnFire() {
        final GameObject fire = findFire();
        return fire != null && fire.getWorldLocation().equals(client.getLocalPlayer().getWorldLocation());
    }

    private GameObject findFire() {
        Optional<GameObject> fire = new GameObjectQuery().containsName("fire").closest(client);

        if (fire.isPresent()) {
            final GameObject gameObject = fire.get();

            if (client.getObjectDefinition(gameObject.getId()).getName().contains("place")) {
                return null;
            }
            return gameObject;
        }

        return null;
    }

    private void fish() {
        final Optional<NPC> fishing_spot = new NPCQuery().nameEquals("Fishing spot").closest(client);
        fishing_spot.ifPresent(npc -> dispatch(npc.action1()));
        sleep(500);
    }
}
