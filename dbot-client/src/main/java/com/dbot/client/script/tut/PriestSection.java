package com.dbot.client.script.tut;

import net.runelite.api.GameState;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.packet.client.ClientWidgetAction1;

public class PriestSection extends TutorialSection {

    public PriestSection() {
        guide = "Brother Brace";
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

        // 3131, 3111

        if (progress == 550) {
            if (!getInstructor().isPresent()) {
                final ClientWalkPacket walk = new ClientWalkPacket(3131, 3111, 0);
                dispatch(walk);
                sleep(2000);
            } else {
                talkToGuide();
            }
        } else if (progress == 560) {
            final ClientWidgetAction1 action1 = new ClientWidgetAction1(10747962, -1, -1);
            dispatch(action1);
            sleep(500);
        } else if (progress == 570) {
            talkToGuide();
        } else if (progress == 580) {
            final ClientWidgetAction1 action1 = new ClientWidgetAction1(10747944, -1, -1);
            dispatch(action1);
            sleep(500);
        } else if (progress == 590) {
            final ClientWidgetAction1 action1 = new ClientWidgetAction1(10747944, -1, -1);
            dispatch(action1);
            sleep(500);
        } else if (progress == 600) {
            talkToGuide();
        } else if (progress == 610) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3122, 3102, 9723, 0);
            dispatch(action1);
            sleep(2000);
        }
    }
}
