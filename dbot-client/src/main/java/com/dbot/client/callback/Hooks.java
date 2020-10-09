/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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
package com.dbot.client.callback;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PostItemComposition;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.SetMessage;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.widgets.Widget;

import static net.runelite.api.widgets.WidgetInfo.WORLD_MAP_VIEW;

import com.dbot.client.input.KeyManager;
import com.dbot.client.input.MouseManager;
import com.dbot.client.task.Scheduler;
import com.dbot.client.util.DeferredEventBus;
import net.runelite.api.widgets.WidgetItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks implements Callbacks {

    public static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private static final long CHECK = 600; // ms - how often to run checks

    @Inject
    private Client client;

    @Inject
    public EventBus eventBus;

    @Inject
    private DeferredEventBus _deferredEventBus;

    @Inject
    private Scheduler scheduler;

    @Inject
    private MouseManager mouseManager;

    @Inject
    private KeyManager keyManager;

    @Inject
    private ClientThread clientThread;

    private final GameTick tick = new GameTick();

    private static Dimension lastStretchedDimensions;
    private static BufferedImage stretchedImage;
    private static Graphics2D stretchedGraphics;

    private static long lastCheck;
    private static boolean shouldProcessGameTick;

    @Override
    public void clientMainLoop(Client client, boolean b) {
        if (shouldProcessGameTick) {
            shouldProcessGameTick = false;

            _deferredEventBus.replay();

            eventBus.post(tick);

            int tick = client.getTickCount();
            client.setTickCount(tick + 1);
        }

        clientThread.invoke();

        long now = System.currentTimeMillis();

        if (now - lastCheck < CHECK) {
            return;
        }

        lastCheck = now;

        try {
            // tick pending scheduled tasks
            scheduler.tick();

            // cull infoboxes

            checkWorldMap();
        } catch (Exception ex) {
            log.warn("error during main loop tasks", ex);
        }
    }

    /**
     * When the world map opens it loads about ~100mb of data into memory, which
     * represents about half of the total memory allocated by the client.
     * This gets cached and never released, which causes GC pressure which can affect
     * performance. This method reinitailzies the world map cache, which allows the
     * data to be garbage collecged, and causes the map data from disk each time
     * is it opened.
     */
    private void checkWorldMap() {
        Widget widget = client.getWidget(WORLD_MAP_VIEW);

        if (widget != null) {
            return;
        }

        RenderOverview renderOverview = client.getRenderOverview();

        if (renderOverview == null) {
            return;
        }

        WorldMapManager manager = renderOverview.getWorldMapManager();

        if (manager != null && manager.isLoaded()) {
            log.debug("World map was closed, reinitializing");
            renderOverview.initializeWorldMap(renderOverview.getWorldMapData());
        }
    }

    @Override
    public void packetFiltered(ClientPacket clientPacket, PacketNode packetNode) {
    }

    @Override
    public void packetDispatched(ClientPacket clientPacket, PacketNode packetNode) {
        String name = net.runelite.api.packet.client.ClientPacket.getPacketName(clientPacket);

        if (name != null && (name.equals("CLIENT_FLUSH") || name.equals("CLIENT_CLASS_VERIFIER")))
            return;

        System.out.println("PacketDispatched: " + clientPacket.getPacketId() + " - " + name);
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent) {
        return mouseManager.processMousePressed(mouseEvent);
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent) {
        return mouseManager.processMouseReleased(mouseEvent);
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
        return mouseManager.processMouseClicked(mouseEvent);
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent) {
        return mouseManager.processMouseEntered(mouseEvent);
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent) {
        return mouseManager.processMouseExited(mouseEvent);
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent) {
        return mouseManager.processMouseDragged(mouseEvent);
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent) {
        return mouseManager.processMouseMoved(mouseEvent);
    }

    @Override
    public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event) {
        return mouseManager.processMouseWheelMoved(event);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        keyManager.processKeyPressed(keyEvent);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keyManager.processKeyReleased(keyEvent);
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        keyManager.processKeyTyped(keyEvent);
    }

    @Override
    public void focusGained(KeyFocusListener l, FocusEvent focusEvent) {
        FocusChanged focusChanged = new FocusChanged();
        focusChanged.setFocused(true);

        eventBus.post(focusChanged);
    }

    @Override
    public void focusLost(KeyFocusListener l, FocusEvent focusEvent) {
        FocusChanged focusChanged = new FocusChanged();
        focusChanged.setFocused(false);

        eventBus.post(focusChanged);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

    @Override
    public void postDeferred(Object event) {
        _deferredEventBus.post(event);
    }

    @Override
    public void updateNpcs() {
        shouldProcessGameTick = true;
    }

    @Override
    public void drawScene() {

    }

    @Override
    public void drawAfterWidgets()
    {

    }

    @Override
    public void drawAboveOverheads() {

    }

    @Override
    public void drawAboveOverheads(TextureProvider textureProvider, int var1)
    {

    }

    @Override
    public void drawRegion(Region region, int var1, int var2, int var3, int var4, int var5, int var6)
    {

    }

    @Override
    public void draw(MainBufferProvider mainBufferProvider, Graphics graphics, int x, int y) {
        if (graphics == null) {
            return;
        }

        BufferedImage image = (BufferedImage) mainBufferProvider.getImage();
        final Graphics2D graphics2d = (Graphics2D) image.getGraphics();

        graphics2d.setColor(Color.BLUE);

        // Stretch the game image if the user has that enabled
        if (!client.isResized() && client.isStretchedEnabled()) {
            Dimension stretchedDimensions = client.getStretchedDimensions();

            if (lastStretchedDimensions == null || !lastStretchedDimensions.equals(stretchedDimensions)) {
				/*
					Reuse the resulting image instance to avoid creating an extreme amount of objects
				 */
                stretchedImage = new BufferedImage(stretchedDimensions.width, stretchedDimensions.height, BufferedImage.TYPE_INT_RGB);

                if (stretchedGraphics != null) {
                    stretchedGraphics.dispose();
                }
                stretchedGraphics = (Graphics2D) stretchedImage.getGraphics();

                lastStretchedDimensions = stretchedDimensions;

				/*
					Fill Canvas before drawing stretched image to prevent artifacts.
				*/
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, client.getCanvas().getWidth(), client.getCanvas().getHeight());
            }

            stretchedGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    client.isStretchedFast()
                            ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                            : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            stretchedGraphics.drawImage(image, 0, 0, stretchedDimensions.width, stretchedDimensions.height, null);

            image = stretchedImage;
        }

        // Draw the image onto the game canvas
        graphics.drawImage(image, 0, 0, client.getCanvas());

//		renderHooks.processDrawComplete(image);
    }

    @Override
    public void drawItem(int itemId, WidgetItem widgetItem) {

    }

    @Override
    public boolean menuActionHook(int actionParam, int widgetId, int menuAction, int id, String menuOption, String menuTarget, int var6, int var7) {
        /* Along the way, the RuneScape client may change a menuAction by incrementing it with 2000.
         * I have no idea why, but it does. Their code contains the same conditional statement.
         */
        if (menuAction >= 2000) {
            menuAction -= 2000;
        }

        MenuOptionClicked menuOptionClicked = new MenuOptionClicked();
        menuOptionClicked.setActionParam(actionParam);
        menuOptionClicked.setMenuOption(menuOption);
        menuOptionClicked.setMenuTarget(menuTarget);
        menuOptionClicked.setMenuAction(MenuAction.of(menuAction));
        menuOptionClicked.setId(id);
        menuOptionClicked.setWidgetId(widgetId);

        log.debug("Menu action clicked: {}", menuOptionClicked);

        eventBus.post(menuOptionClicked);

        return menuOptionClicked.isConsumed();
    }

    @Override
    public void addChatMessage(int type, String name, String message, String sender) {
        if (log.isDebugEnabled()) {
            log.debug("Chat message type {}: {}", ChatMessageType.of(type), message);
        }

        ChatMessageType chatMessageType = ChatMessageType.of(type);
        ChatMessage chatMessage = new ChatMessage(chatMessageType, name, message, sender);

        eventBus.post(chatMessage);
    }

    /**
     * Called when a projectile is set to move towards a point. For
     * projectiles that target the ground, like AoE projectiles from
     * Lizardman Shamans, this is only called once
     *
     * @param projectile The projectile being moved
     * @param targetX    X position of where the projectile is being moved to
     * @param targetY    Y position of where the projectile is being moved to
     * @param targetZ    Z position of where the projectile is being moved to
     * @param cycle
     */
    @Override
    public void projectileMoved(Projectile projectile, int targetX, int targetY, int targetZ, int cycle) {
        LocalPoint position = new LocalPoint(targetX, targetY);
        ProjectileMoved projectileMoved = new ProjectileMoved();
        projectileMoved.setProjectile(projectile);
        projectileMoved.setPosition(position);
        projectileMoved.setZ(targetZ);
        eventBus.post(projectileMoved);
    }

    @Override
    public void setMessage(MessageNode messageNode, int type, String name, String sender, String value) {
        SetMessage setMessage = new SetMessage();
        setMessage.setMessageNode(messageNode);
        setMessage.setType(ChatMessageType.of(type));
        setMessage.setName(name);
        setMessage.setSender(sender);
        setMessage.setValue(value);

        eventBus.post(setMessage);
    }

    @Override
    public void onNpcUpdate(boolean var0, PacketBuffer var1) {
        // The NPC update event seem to run every server tick,
        // but having the game tick event after all packets
        // have been processed is typically more useful.
        shouldProcessGameTick = true;
    }

    @Override
    public void onSetCombatInfo(Actor actor, int combatInfoId, int gameCycle, int var3, int var4, int healthRatio, int health) {
        if (healthRatio == 0) {
            ActorDeath death = new ActorDeath();
            death.setActor(actor);
            eventBus.post(death);
        }
    }

    @Override
    public void postItemComposition(ItemComposition itemComposition) {
        PostItemComposition event = new PostItemComposition();
        event.setItemComposition(itemComposition);
        eventBus.post(event);
    }

    @Override
    public void menuOpened(Client client, int var1, int var2) {
        MenuOpened event = new MenuOpened();
        event.setMenuEntries(client.getMenuEntries());
        eventBus.post(event);
    }

    /**
     * Called after a hitsplat has been processed on an actor.
     * Note that this event runs even if the hitsplat didn't show up,
     * i.e. the actor already had 4 visible hitsplats.
     *
     * @param actor     The actor the hitsplat was applied to
     * @param type      The hitsplat type (i.e. color)
     * @param value     The value of the hitsplat (i.e. how high the hit was)
     * @param var3
     * @param var4
     * @param gameCycle The gamecycle the hitsplat was applied on
     * @param duration  The amount of gamecycles the hitsplat will last for
     */
    @Override
    public void onActorHitsplat(Actor actor, int type, int value, int var3, int var4,
                                int gameCycle, int duration) {
        Hitsplat hitsplat = new Hitsplat(Hitsplat.HitsplatType.fromInteger(type), value,
                gameCycle + duration);

        HitsplatApplied event = new HitsplatApplied();
        event.setActor(actor);
        event.setHitsplat(hitsplat);
        eventBus.post(event);
    }

    @Override
    public void onGraphicsObjectCreated(GraphicsObject go, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        GraphicsObjectCreated event = new GraphicsObjectCreated(go);
        eventBus.post(event);
    }
}
