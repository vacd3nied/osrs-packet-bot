package com.dbot.client.script.tut;

import net.runelite.api.GameState;
import net.runelite.api.packet.client.ClientDialog;
import net.runelite.api.packet.client.ClientObjectAction1;
import net.runelite.api.packet.client.ClientUserMessage;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;

import java.util.Random;

public class GuideSection extends TutorialSection {

    private final String[] VERBS = {"Dr ", "Sir ", "big", "fire", "Big", "Small", "grumpy", "happy", "elite", "cool", "69", "420", "top", "chad"};
    private final String[] NOUNS = {"nub", "dino", "heart", "rad", "no", "chad", "karen", "oliv", "abi", "brool", "trmp", "wc", "pker", "sklz"};
    private final String[] SUFFIXIES = {"69", "1", "420", "GG", "0", "lol", "Jr", "Sr"};

    private static final Random random = new Random();
    private static final int WIDGET_EXPERIENCE_PLAYER = 1;
    private static final String ACCEPT_BUTTON = "Accept";

    public GuideSection() {
        guide = "Gielinor Guide";
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

        if (progress >= 0 && progress <= 2) {
            final Widget lookupName = client.getWidget(558, 17);

            if (lookupName != null && !lookupName.isHidden()) {
                dispatch(lookupName.action1());
                sleep(1000);
                dispatch(new ClientUserMessage(generateName()));
                sleep(3000);

                final Widget setName = client.getWidget(558, 18);
                if (setName != null && !setName.isHidden()) {
                    dispatch(setName.action1());
                    sleep(3000);
                    return;
                }
            }


            final Widget widget = client.getWidget(269, 100);

            if (widget != null && !widget.isHidden()) {
                client.menuAction(-1, 17629283, 24, -1, "", "", -1, -1);
                sleep(3000);
                return;
            }

            final Widget dialog = client.getWidget(WidgetID.DIALOG_OPTION_GROUP_ID, WIDGET_EXPERIENCE_PLAYER);

            if (dialog != null && !dialog.isHidden()) {
                dispatch(new ClientDialog(dialog.getId(), 3));
                sleep(1000);
                return;
            }

            talkToGuide();
        } else if (progress == 3) {
            final ClientWidgetAction1 action = new ClientWidgetAction1(10747945, -1, -1);
            dispatch(action);
            sleep(500);
        } else if (progress == 10) {
            final ClientObjectAction1 oa1 = new ClientObjectAction1(3098, 3107, 9398, 0);
            dispatch(oa1);
            sleep(1000);
        } else {
            talkToGuide();
        }
    }

    private String generateName() {
        String name = "";

        name += VERBS[random.nextInt(VERBS.length)];
        name += NOUNS[random.nextInt(NOUNS.length)];
        name += SUFFIXIES[random.nextInt(SUFFIXIES.length)];

        if (name.length() > 12) {
            return generateName();
        }

        return name;
    }
}
