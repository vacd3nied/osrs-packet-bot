package com.dbot.client.script;

import net.runelite.api.GameState;
import net.runelite.api.script.PollingScript;

public class AntiLogout extends PollingScript {

    public AntiLogout() {
        super(0);
    }

    @Override
    public void poll() {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        client.setMouseIdleTicks(0);
    }
}
