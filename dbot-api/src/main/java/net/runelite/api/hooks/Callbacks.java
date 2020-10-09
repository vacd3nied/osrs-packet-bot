/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api.hooks;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.WidgetItem;
import org.slf4j.Logger;

/**
 * Interface of callbacks the injected client uses to send events
 */
public interface Callbacks {
    void focusGained(KeyFocusListener l, FocusEvent focusEvent);

    void focusLost(KeyFocusListener l, FocusEvent focusEvent);

    /**
     * Post an event. See the events in net.runelite.api.events.
     *
     * @param event the event
     */
    void post(Object event);

    /**
     * Post a deferred event, which gets delayed until the next cycle.
     *
     * @param event the event
     */
    void postDeferred(Object event);

    /**
     * Called each client cycle.
     */
    void clientMainLoop(Client client, boolean b);

    /**
     * Called after receiving update NPCs packet from server.
     */
    void updateNpcs();

    /**
     * Called after the scene is drawn.
     */
    void drawScene();

    void drawAfterWidgets();

    /**
     * Called after logic that is drawing 2D objects is processed.
     */
    void drawAboveOverheads();

    void drawAboveOverheads(TextureProvider textureProvider, int var1);

    void drawRegion(Region region, int var1, int var2, int var3, int var4, int var5, int var6);

    /**
     * Client top-most draw method, rendering over top of most of game interfaces.
     *
     * @param mainBufferProvider the main buffer provider
     * @param graphics           the graphics
     * @param x                  the x
     * @param y                  the y
     */
    void draw(MainBufferProvider mainBufferProvider, Graphics graphics, int x, int y);

    /**
     * Called before the client will render an item widget.
     */
    void drawItem(int itemId, WidgetItem widgetItem);

    /**
     * Mouse pressed event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mousePressed(MouseEvent mouseEvent);

    /**
     * Mouse released event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mouseReleased(MouseEvent mouseEvent);

    /**
     * Mouse clicked event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mouseClicked(MouseEvent mouseEvent);

    /**
     * Mouse entered event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mouseEntered(MouseEvent mouseEvent);

    /**
     * Mouse exited event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mouseExited(MouseEvent mouseEvent);

    /**
     * Mouse dragged event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mouseDragged(MouseEvent mouseEvent);

    /**
     * Mouse moved event. If this event will be consumed it will not be propagated further to client.
     *
     * @param mouseEvent the mouse event
     * @return the mouse event
     */
    MouseEvent mouseMoved(MouseEvent mouseEvent);

    /**
     * Mouse wheel moved event. If this event will be consumed it will not be propagated further to client.
     *
     * @param event the event
     * @return the mouse wheel event
     */
    MouseWheelEvent mouseWheelMoved(MouseWheelEvent event);

    /**
     * Key pressed event.
     *
     * @param keyEvent the key event
     */
    void keyPressed(KeyEvent keyEvent);

    /**
     * Key released event.
     *
     * @param keyEvent the key event
     */
    void keyReleased(KeyEvent keyEvent);


    Logger getLogger();

    /**
     * Key typed event.
     *
     * @param keyEvent the key event
     */
    void keyTyped(KeyEvent keyEvent);

    void packetFiltered(ClientPacket clientPacket, PacketNode packetNode);

    void packetDispatched(ClientPacket clientPacket, PacketNode packetNode);

    boolean menuActionHook(int actionParam, int widgetId, int menuAction, int id, String menuOption, String menuTarget, int var6, int var7);

    void projectileMoved(Projectile projectile, int targetX, int targetY, int targetZ, int cycle);

    void setMessage(MessageNode messageNode, int type, String name, String sender, String value);

    void onNpcUpdate(boolean var0, PacketBuffer var1);

    void onSetCombatInfo(Actor actor, int combatInfoId, int gameCycle, int var3, int var4, int healthRatio, int health);

    void postItemComposition(ItemComposition itemComposition);

    void menuOpened(Client client, int var1, int var2);

    void onActorHitsplat(Actor actor, int type, int value, int var3, int var4,
                         int gameCycle, int duration);

    void onGraphicsObjectCreated(GraphicsObject go, int var1, int var2, int var3, int var4, int var5, int var6, int var7);

    void addChatMessage(int type, String name, String message, String sender);
}
