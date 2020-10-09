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
package net.runelite.api.widgets;

import java.awt.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.packet.client.*;

import javax.annotation.Nullable;

/**
 * An item that is being represented in a {@link Widget}.
 */
@AllArgsConstructor
@ToString
@Getter
public class WidgetItem
{
	/**
	 * The ID of the item represented.
	 *
	 * @see net.runelite.api.ItemID
	 */
	private final int id;
	/**
	 * The quantity of the represented item.
	 */
	private final int quantity;
	/**
	 * The index position of this WidgetItem inside its parents
	 * WidgetItem array.
	 *
	 * @see Widget#getWidgetItems()
	 */
	private final int index;
	/**
	 * The area where the widget is drawn on the canvas.
	 */
	private final Rectangle canvasBounds;
	/**
	 * The widget which contains this item.
	 */
	@Nullable
	private final Widget widget;

	/**
	 * The canvas bounds for the widget, if it is being dragged.
	 */
	@Nullable
	private final Rectangle draggingCanvasBounds;

	/**
	 * Gets the upper-left coordinate of where the widget is being drawn
	 * on the canvas.
	 *
	 * @return the upper-left coordinate of where this widget is drawn
	 */
	public Point getCanvasLocation()
	{
		return new Point((int) canvasBounds.getX(), (int) canvasBounds.getY());
	}

	public WidgetItem(int id, int quantity, int index, Rectangle canvasBounds) {
		this(id, quantity, index, canvasBounds, null, null);
	}

	public ClientDropItem drop() {
		return new ClientDropItem(id, index);
	}

	public ClientItemOnObject1 useOnObject(final Client client, final GameObject obj) {
		final WorldPoint worldLocation = obj.getWorldLocation();
		final Point regionMin = obj.getRegionMinLocation();

		final WorldPoint w = new WorldPoint(client.getBaseX() + regionMin.getX(), client.getBaseY() + regionMin.getY(), worldLocation.getPlane());

		int widgetId = widget != null ? widget.getId() : WidgetInfo.INVENTORY.getId();
		return new ClientItemOnObject1(id, index, widgetId, w.getX(), w.getY(), obj.getId(), 0);
	}

	public ClientItemEquip equip() {
		int widgetId = widget != null ? widget.getId() : WidgetInfo.INVENTORY.getId();
		return new ClientItemEquip(id, index, widgetId);
	}

	public ClientItemAction1 action1() {
		int widgetId = widget != null ? widget.getId() : WidgetInfo.INVENTORY.getId();
		return new ClientItemAction1(id, index, widgetId);
	}

	public ClientItemExamine examine() {
		return new ClientItemExamine(id);
	}

	public ClientItemOnItem useOnItem(final WidgetItem item) {
		int widgetId = widget != null ? widget.getId() : WidgetInfo.INVENTORY.getId();
		return new ClientItemOnItem(id, item.id, index, item.index, widgetId);
	}
}
