package com.dbot.client.script.tut;

import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import java.util.List;
import java.util.Optional;

public class MiningSection extends TutorialSection {

    private final int[] COPPER = {10079};
    private final int[] TIN = {10080};

    public MiningSection() {
        guide = "Mining Instructor";
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

        if (!getInstructor().isPresent()) {
            final ClientWalkPacket walkPacket = new ClientWalkPacket(3080, 9504, 0);
            dispatch(walkPacket);
            sleep(2000);
            return;
        }

        if (progress == 260) {
            talkToGuide();
        } else if (progress == 270) {
            mine(TIN);
        } else if (progress == 280) {
            mine(COPPER);
        } else if (progress == 290) {
            talkToGuide();
        } else if (progress == 300) {
            mine(TIN);
        } else if (progress == 310) {
            mine(COPPER);
        } else if (progress == 320) {
            smelt();
        } else if (progress == 330) {
            talkToGuide();
        } else if (progress == 340) {
            smith();
        } else if (progress == 350) {
            final Widget panel = client.getWidget(WidgetInfo.SMITHING_INVENTORY_ITEMS_CONTAINER);

            if (panel != null && !panel.isHidden()) {
                final ClientWidgetAction1 action1 = new ClientWidgetAction1(20447241, -1, -1);
                dispatch(action1);
                sleep(2500);
            } else {
                smith();
            }
        } else if (progress == 360) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3094, 9502, 9718, 0);
            dispatch(action1);
            sleep(2000);
        }
    }

    private void smith() {
        final Optional<GameObject> anvil = new GameObjectQuery().containsName("Anvil").closest(client);

        if (anvil.isPresent()) {
            final List<WidgetItem> bar = inventory.getWidgetItems(new int[]{ItemID.BRONZE_BAR});

            if (!bar.isEmpty()) {
                dispatch(bar.get(0).useOnObject(client, anvil.get()));
                sleep(1500);
            }
        }
    }

    private void smelt() {
        final List<WidgetItem> tin = inventory.getWidgetItems(new int[]{ItemID.TIN_ORE});
        final List<WidgetItem> copper = inventory.getWidgetItems(new int[]{ItemID.COPPER_ORE});

        if (!tin.isEmpty() && !copper.isEmpty()) {
            final Optional<GameObject> furnace = new GameObjectQuery().containsName("Furnace").closest(client);
            furnace.ifPresent(gameObject -> dispatch(tin.get(0).useOnObject(client, gameObject)));
            sleep(1500);
        }
    }

    private void mine(int... objId) {
        Optional<GameObject> closest = new GameObjectQuery().idEquals(objId).closest(client);

        if (closest.isPresent() && client.getLocalPlayer().getAnimation() == -1) {
            dispatch(closest.get().action1(client));
            sleep(1000);
        }
    }
}
