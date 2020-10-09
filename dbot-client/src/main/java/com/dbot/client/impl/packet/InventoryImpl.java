package com.dbot.client.impl.packet;

import com.dbot.client.callback.ClientThread;
import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Condition;
import net.runelite.api.Inventory;
import net.runelite.api.ItemActions;
import net.runelite.api.device.Mouse;
import net.runelite.api.packet.Packet;
import net.runelite.api.packet.client.ClientWidgetAction1;
import net.runelite.api.script.PollingScript;
import net.runelite.api.script.Script;
import net.runelite.api.script.ScriptState;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import java.util.Collection;
import java.util.LinkedList;

public class InventoryImpl implements Inventory {

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Override
    public Widget getWidget() {
        return client.getWidget(WidgetInfo.INVENTORY);
    }

    @Override
    public Collection<WidgetItem> getWidgetItems() {
        final Widget inv = getWidget();

        if (inv == null)
            return new LinkedList<>();

        return inv.getWidgetItems();
    }

    @Override
    public boolean interact(final WidgetItem item, final ItemActions action) {

        return false;
    }

    @Override
    public boolean isOpen() {
        final Widget invContainer = getContainerWidget();

        return invContainer != null && !invContainer.isHidden();
    }

    @Override
    public Script open() {
        return new PollingScript(0) {
            @Override
            public void poll() {
                if (isOpen()) {
                    setState(ScriptState.STOP);
                } else {
                    final Widget tab = getTabWidget();

                    if (tab != null && !tab.isHidden()) {
                        final ClientWidgetAction1 packet = new ClientWidgetAction1(tab.getItemId(), -1, -1);
                        sendPacket(packet);
                    }
                }
            }
        };
    }

    private void sendPacket(final Packet packet) {
        clientThread.invokeLater(() -> {
            packet.writePacket(client.getNetWriter());
        });
    }

    private Widget getContainerWidget() {
        Widget invContainer = client.getWidget(WidgetInfo.FIXED_VIEWPORT_INVENTORY_CONTAINER);

        if (invContainer == null)
            invContainer = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_CONTAINER);

        return invContainer;
    }

    private Widget getTabWidget() {
        Widget invContainer = client.getWidget(WidgetInfo.FIXED_VIEWPORT_INVENTORY_ICON);

        if (invContainer == null)
            invContainer = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_ICON);

        return invContainer;
    }
}
