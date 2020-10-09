package net.runelite.api.device;

import net.runelite.api.Actor;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;

import java.awt.*;

public interface Mouse {

    int getX();

    int getY();

    void setPosition(int x, int y);

    boolean isOnScreen();

    boolean setFocus(boolean focused);

    boolean isFocused();

    void click(boolean left);

    void click(int x, int y, boolean left);

    void move(int destX, int destY);

    boolean hover(Actor actor);

    boolean hover(GameObject obj);

    boolean hover(TileObject obj);

    boolean hover(Widget widget);

    boolean hover(WidgetItem item);

    boolean hover(Rectangle rect);

    boolean click(Rectangle rect);

    boolean click(Actor actor);

    boolean click(GameObject obj);

    boolean click(TileObject obj);

    boolean click(Widget widget);

    boolean click(WidgetItem item);

    boolean click(Rectangle rect, boolean left);

    boolean click(Actor actor, boolean left);

    boolean click(GameObject obj, boolean left);

    boolean click(TileObject obj, boolean left);

    boolean click(Widget widget, boolean left);

    boolean click(WidgetItem item, boolean left);


}
