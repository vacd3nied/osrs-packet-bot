package com.dbot.client.script.tut;

import net.runelite.api.GameState;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientWidgetAction1;

public class BankSection extends TutorialSection {

    public BankSection() {
        guide = "Account Guide";
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

        if (progress == 510) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3120, 3124, 10083, 0);
            dispatch(action1);
            sleep(2500);
        } else if (progress == 520) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3119, 3121, 26815, 0);
            dispatch(action1);
            sleep(2000);
        } else if (progress == 525) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3125, 3124, 9721, 0);
            dispatch(action1);
            sleep(2500);
        } else if (progress == 530) {
            talkToGuide();
        } else if (progress == 531) {
            final ClientWidgetAction1 action1 = new ClientWidgetAction1(10747943, -1, -1);
            dispatch(action1);
            sleep(500);
        } else if (progress == 532) {
            talkToGuide();
        } else if (progress == 540) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3130, 3124, 9722, 0);
            dispatch(action1);
            sleep(2500);
        }
    }
}
