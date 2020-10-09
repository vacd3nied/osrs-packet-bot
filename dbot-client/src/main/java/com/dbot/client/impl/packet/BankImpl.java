package com.dbot.client.impl.packet;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.*;
import net.runelite.api.packet.client.ClientPacket;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.api.queries.NPCQuery;
import net.runelite.api.script.PollingScript;
import net.runelite.api.script.Script;
import net.runelite.api.script.ScriptState;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import java.util.List;
import java.util.Optional;

public class BankImpl implements Bank {

    @Inject
    private Client client;

    @Inject
    private Movement movement;

    @Override
    public void deposit(final WidgetItem item, final int amount) {
        final ClientPacket packet;
        switch (amount) {
            case 1:
                packet = new ClientWidgetAction2(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(), item.getIndex(), item.getId());
                break;
            case 5:
                packet = new ClientWidgetAction4(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(), item.getIndex(), item.getId());
                break;
            case 10:
                packet = new ClientWidgetAction5(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(), item.getIndex(), item.getId());
                break;
            default:
        }
    }

    @Override
    public void withdraw(final WidgetItem item, final int amount) {
        ClientPacket packet = null;

        switch (amount) {
            case 1:
                packet = new ClientWidgetAction1(WidgetInfo.BANK_ITEM_CONTAINER.getId(), item.getIndex(), item.getId());
                break;
            case 5:
                packet = new ClientWidgetAction3(WidgetInfo.BANK_ITEM_CONTAINER.getId(), item.getIndex(), item.getId());
                break;
            case 10:
                packet = new ClientWidgetAction4(WidgetInfo.BANK_ITEM_CONTAINER.getId(), item.getIndex(), item.getId());
                break;
            default:
        }

        if (packet != null) {
            packet.writePacket(client.getNetWriter());
        }
    }

    @Override
    public void depositAll(final WidgetItem item) {
        final ClientPacket packet = new ClientWidgetAction8(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(), item.getIndex(), item.getId());
        packet.writePacket(client.getNetWriter());
    }

    @Override
    public void withdrawAll(final WidgetItem item) {
        final ClientPacket packet = new ClientWidgetAction7(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(), item.getIndex(), item.getId());
        packet.writePacket(client.getNetWriter());
    }

    @Override
    public void depositAll() {
        final ClientPacket packet = new ClientWidgetAction1(WidgetInfo.BANK_DEPOSIT_INVENTORY.getId(), -1, -1);
        packet.writePacket(client.getNetWriter());
    }

    @Override
    public void depositEquipment() {
        final ClientPacket packet = new ClientWidgetAction1(WidgetInfo.BANK_DEPOSIT_EQUIPMENT.getId(), -1, -1);
        packet.writePacket(client.getNetWriter());
    }

    @Override
    public boolean isOpen() {
        final Widget widget = client.getWidget(WidgetInfo.BANK_CONTAINER);

        return widget != null && !widget.isHidden();
    }

    @Override
    public Script open() {
        return new PollingScript(0) {
            long lastPacket = 0;
            @Override
            public void poll() {
                if (isOpen() || client.getGameState() != GameState.LOGGED_IN) {
                    setState(ScriptState.STOP);
                    return;
                }

                final GameObjectQuery objQuery = new GameObjectQuery().idEquals(BANK_IDS);
                final NPCQuery npcQuery = new NPCQuery().nameContains("Banker");

                final Optional<GameObject> closestObj = objQuery.closest(client);
                if (closestObj.isPresent()) {
                    final GameObject obj = closestObj.get();
                    if (obj.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()) < 20
                            && lastPacket + 1000 < System.currentTimeMillis()) {
                        obj.action2(client).writePacket(client.getNetWriter());
                        lastPacket = System.currentTimeMillis();
                        return;
                    }
                } else {
                    final Optional<NPC> closestNpc = npcQuery.closest(client);
                    if (closestNpc.isPresent() && lastPacket + 1000 < System.currentTimeMillis()) {
                        closestNpc.get().action2().writePacket(client.getNetWriter());
                        lastPacket = System.currentTimeMillis();
                        return;
                    }
                }

                final List<WorldPoint> nearest = ObjectRepository.nearest(client.getLocalPlayer().getWorldLocation(), BANK_IDS);

                for (final WorldPoint worldPoint : nearest) {
                    this.yield(movement.walkTo(client, worldPoint, 10));
                    return;
                }
            }
        };
    }
}
