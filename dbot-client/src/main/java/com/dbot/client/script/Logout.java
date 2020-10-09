package com.dbot.client.script;

import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.queries.PlayerQuery;
import net.runelite.api.script.PollingScript;
import net.runelite.api.widgets.WidgetInfo;

public class Logout extends PollingScript {

    public Logout() {
        super(0);
    }

    @Override
    public void poll() {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        final Player[] result = new PlayerQuery().result(client);
        final WorldPoint worldLocation = client.getLocalPlayer().getWorldLocation();


        if (result.length > 1 && lastPacket + 1000 < System.currentTimeMillis() && worldLocation.getY() > 3521) {
            final ClientWidgetAction1 widgetAction1 = new ClientWidgetAction1(WidgetInfo.CLICK_HERE_TO_LOGOUT.getId(), -1, -1);
            dispatch(widgetAction1);
        }
    }
}
