package com.dbot.client.script;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.Packet;
import net.runelite.api.packet.client.ClientUserMessage;
import net.runelite.api.packet.client.ClientWalkPacket;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.script.AbstractScript;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.script.ScriptContext;
import java.applet.Applet;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class Script {

    public static void eval(AbstractScript ctx) {
        System.out.println("Running Test Script.");

        final Client client = ctx.getClient();
        final Player localPlayer = client.getLocalPlayer();

    }

    public static void dispatch(AbstractScript ctx, Packet packet) {
        packet.writePacket(ctx.getClient().getNetWriter());
    }
}
