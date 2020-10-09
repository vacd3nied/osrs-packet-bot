package com.dbot.client.script.tut;

import net.runelite.api.GameState;
import net.runelite.api.VarPlayer;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public class QuestSection extends TutorialSection {

    public QuestSection() {
        guide = "Quest Guide";
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

        if (progress == 200) {
            run();
        } else if (progress == 210) {
            if (!isRunning()) {
                run();
                return;
            }

            final ClientObjectAction1 action1 = new ClientObjectAction1(3086, 3126, 9716, 0);
            dispatch(action1);
            sleep(2000);
        } else if (progress == 220) {
            talkToGuide();
        } else if (progress == 230) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(10747959, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 240) {
            talkToGuide();
            sleep(1500);
        } else if (progress == 250) {
            final ClientObjectAction1 action1 = new ClientObjectAction1(3088, 3119, 9726, 0);
            dispatch(action1);
            sleep(2000);
        }
    }


    private boolean isRunning() {
        return client.getVar(VarPlayer.RUNNING) == 1;
    }

    private void run() {
        Widget widget = client.getWidget(WidgetInfo.MINIMAP_TOGGLE_RUN_ORB);

        if (widget != null && !widget.isHidden()) {
            dispatch(widget.action1());
            sleep(500);
        }
    }
}
