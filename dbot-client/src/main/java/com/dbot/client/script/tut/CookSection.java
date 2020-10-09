package com.dbot.client.script.tut;

import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.widgets.WidgetItem;

import java.util.List;
import java.util.Optional;

public class CookSection extends TutorialSection {

    public CookSection() {
        guide = "Master Chef";
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

        if (progress == 130) {
            final ClientObjectAction1 action = new ClientObjectAction1(3079, 3084, 9709, 0);
            dispatch(action);
            sleep(2000);
        } else if (progress == 140) {
            talkToGuide();
        } else if (progress == 150) {
            final List<WidgetItem> flour = inventory.getWidgetItems(new int[]{ItemID.POT_OF_FLOUR, ItemID.POT_OF_FLOUR_2516});
            final List<WidgetItem> dough = inventory.getWidgetItems(new int[]{ItemID.BUCKET_OF_WATER,
                    ItemID.BUCKET_OF_WATER_6712, ItemID.BUCKET_OF_WATER_9659});

            if (!flour.isEmpty() && !dough.isEmpty()) {
                dispatch(dough.get(0).useOnItem(flour.get(0)));
                sleep(600);
            }
        } else if (progress == 160) {
            final Optional<GameObject> range = new GameObjectQuery().containsName("Range").closest(client);
            final List<WidgetItem> dough = inventory.getWidgetItems(new int[]{ItemID.BREAD_DOUGH});

            if (range.isPresent()) {
                dispatch(dough.get(0).useOnObject(client, range.get()));
                sleep(2000);
            }
        } else if (progress == 170) {
            final ClientObjectAction1 action = new ClientObjectAction1(3072, 3090, 9710, 0);
            dispatch(action);
            sleep(2000);
        }
    }
}
