package com.dbot.client.script.tut;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.ClientMouseClickedPacket;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.widgets.WidgetItem;

import java.util.List;
import java.util.Optional;

public class CombatSection extends TutorialSection {

    private final TileArea RANGE_AREA = new TileArea(3107, 9511, 3112, 9513, 0);
    private long lastAttack = 0;

    public CombatSection() {
        guide = "Combat Instructor";
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

        if (progress == 370) {
            talkToGuide();
        } else if (progress == 390) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(10747961, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 400) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(25362433, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 405) {
            final List<WidgetItem> dagger = inventory.getWidgetItems(new int[]{ItemID.BRONZE_DAGGER});

            if (!dagger.isEmpty()) {
                dispatch(dagger.get(0).equip());
                sleep(500);
            }
        } else if (progress == 410) {
            talkToGuide();
        } else if (progress == 420) {
            final List<WidgetItem> shield = inventory.getWidgetItems(new int[]{ItemID.WOODEN_SHIELD, ItemID.WOODEN_SHIELD_7676});
            final List<WidgetItem> sword = inventory.getWidgetItems(new int[]{ItemID.BRONZE_SWORD});

            if (!sword.isEmpty()) {
                dispatch(sword.get(0).equip());
                sleep(500);
            }

            if (!shield.isEmpty()) {
                dispatch(shield.get(0).equip());
                sleep(500);
            }
        } else if (progress == 430) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(10747957, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 440) {
            openGate();
        } else if (progress == 450 || progress == 460) {
            if (!inRatCage()) {
                openGate();
            } else {
                attackRat();
            }
        } else if (progress == 470) {
            if (inRatCage()) {
                openGate();
            } else {
                talkToGuide();
            }
        } else if (progress == 480 || progress == 490) {
            final List<WidgetItem> shortBow = inventory.getWidgetItems(new int[]{ItemID.SHORTBOW});
            final List<WidgetItem> arrows = inventory.getWidgetItems(new int[]{ItemID.BRONZE_ARROW});

            if (!shortBow.isEmpty()) {
                dispatch(shortBow.get(0).equip());
                sleep(500);
                return;
            }

            if (!arrows.isEmpty()) {
                dispatch(arrows.get(0).equip());
                sleep(500);
                return;
            }

            final WorldPoint location = client.getLocalPlayer().getWorldLocation();
            if (!RANGE_AREA.contains(location) && client.getLocalDestinationLocation() == null) {
                dispatch(RANGE_AREA.randomTile().walkTo());
                sleep(1000);
            } else {
                attackRat();
            }
        } else if (progress == 500) {
            final ClientObjectAction1 action = new ClientObjectAction1(3111, 9526, 9727, 0);
            dispatch(action);
            sleep(2000);
        }
    }

    private void openGate() {
        final ClientObjectAction1 action1 = new ClientObjectAction1(3111, 9518, 9719, 0);
        dispatch(action1);
        sleep(2000);
    }

    private void attackRat() {
        if (lastAttack + 4000 > System.currentTimeMillis())
            return;

        final Actor interacting = client.getLocalPlayer().getInteracting();

        if (interacting != null && interacting.getName().contains("rat"))
            return;

        final Optional<NPC> currRat = new NPCQuery().nameEquals("Giant rat").isInteractingWith(client.getLocalPlayer()).closest(client);

        if (currRat.isPresent() && client.getLocalPlayer().getInteracting() != null) {
            return;
        } else if (currRat.isPresent()) {
            dispatch(new ClientMouseClickedPacket(400, 20 + random.nextInt( 450), 20 + random.nextInt(400)));
            dispatch(currRat.get().attack());
            lastAttack = System.currentTimeMillis();
            sleep(1000);
            return;
        }

        final Optional<NPC> rat = new NPCQuery().nameEquals("Giant rat").isInteractingWith(null).closest(client);

        if (rat.isPresent()) {
            System.out.println("Attacking!");
            // for some reason attack operations dont work without a mouse click packets
            dispatch(new ClientMouseClickedPacket(400, 20 + random.nextInt( 450), 20 + random.nextInt(400)));
            dispatch(rat.get().attack());
            lastAttack = System.currentTimeMillis();
            sleep(1000);
        }
    }

    private boolean inRatCage() {
        final WorldPoint worldLocation = client.getLocalPlayer().getWorldLocation();
        int x = worldLocation.getX();
        int y = worldLocation.getY();

        if (x >= 3098 && y >= 9515 && x <= 3109 && y <= 9521)
            return true;

        if (x >= 3109 && y >= 9517 && x <= 3110 && y <= 9519)
            return true;

        if (x >= 3098 && y >= 9512 && x <= 3106 && y <= 9515)
            return true;

        return x >= 3098 && y >= 9522 && x <= 3104 && y <= 9525;
    }
}
