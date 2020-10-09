package com.dbot.client.script.tut;

import net.runelite.api.NPC;
import net.runelite.api.QueryResult;
import net.runelite.api.VarPlayer;
import net.runelite.api.packet.client.ClientDialog;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.queries.WidgetQuery;
import net.runelite.api.script.PollingScript;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import java.util.Optional;

public class TutorialSection extends PollingScript {

    private static final int SCRIPT_HIDE_PERSISTENT_MESSAGE = 101;

    private String[] CONTINUE_TEXT = {
            "Click here to continue",
            "Click to continue"
    };

    protected String guide = null;
    private long lastContinue = 0;

    public TutorialSection() {
        super(0);
    }

    @Override
    public void poll() {

    }

    protected boolean pendingContinue() {
        final Widget continueWidget = getContinueWidget();

        return continueWidget != null && !continueWidget.isHidden();
    }

    protected void selectContinue() {
        final Widget continueWidget = getContinueWidget();

        if (continueWidget != null && lastContinue + 1000 < System.currentTimeMillis()) {
            int chatOption = -1;

            if (continueWidget.getId() == WidgetInfo.DIALOG_SPRITE.getId())
                chatOption = 0;

            lastContinue = System.currentTimeMillis();
            if (continueWidget.getId() == WidgetInfo.CHATBOX_FULL_INPUT.getId()) {
                client.runScript(SCRIPT_HIDE_PERSISTENT_MESSAGE, 0);
            } else {
                ClientDialog dialog = new ClientDialog(continueWidget.getId(), chatOption);
                dispatch(dialog);
            }
        }
    }

    protected Widget getContinueWidget() {
        final Widget npcContinue = client.getWidget(WidgetInfo.DIALOG_NPC_CONTINUE);

        if (npcContinue != null)
            return npcContinue;

        final Widget playerContinue = client.getWidget(WidgetInfo.DIALOG_PLAYER_CONTINUE);

        if (playerContinue != null)
            return playerContinue;

        final Widget gameContinue = client.getWidget(229, 2);

        if (gameContinue != null)
            return gameContinue;

        final Widget input = client.getWidget(WidgetInfo.CHATBOX_FULL_INPUT);

        if (input != null && !input.isHidden())
            return input;

        final Widget gg = client.getWidget(WidgetInfo.DIALOG_SPRITE);

        if (gg != null)
            return gg;

        return null;
    }

    protected int getProgress() {
        return client.getVar(VarPlayer.TUTORIAL_ISLAND_PROGRESS);
    }

    protected Optional<NPC> getInstructor() {
        return new NPCQuery().nameContains(guide).closest(client);
    }

    protected void talkToGuide() {
        final Optional<NPC> closest = getInstructor();

        if (closest.isPresent()) {
            if (!closest.get().equals(client.getLocalPlayer().getInteracting())) {
                dispatch(closest.get().action1());
                sleep(500);
            }
        }
    }

    protected void sleep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
        }
    }

    public Widget findWidget(String text) {
        final WidgetQuery query = new WidgetQuery().containsText(text).isNotHidden();
        final QueryResult<Widget> eval = query.eval(client);

        if (eval != null) {
            return eval.stream().findFirst().orElse(null);
        }

        return null;
    }
}
