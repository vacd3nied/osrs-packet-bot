package net.runelite.api;

import net.runelite.api.widgets.Widget;

public interface ScriptEvent
{
    int MOUSE_X = -2147483647;
    int MOUSE_Y = -2147483646;
    int MENU_OP = -2147483644;
    int WIDGET_ID = -2147483645;
    int WIDGET_INDEX = -2147483643;
    int WIDGET_TARGET_ID = -2147483642;
    int WIDGET_TARGET_INDEX = -2147483641;
    int KEY_CODE = -2147483640;
    int KEY_CHAR = -2147483639;
    String NAME = "event_opbase";

    /**
     * Gets the widget of the event.
     *
     * @return the widget
     * @see net.runelite.api.widgets.Widget
     */
    Widget getSource();

    /**
     * Gets the menu index of the event
     *
     * @return the index
     */
    int getOp();

    /**
     * Gets the target of the menu option
     *
     * @return the target
     * @see net.runelite.api.events.MenuOptionClicked
     */
    String getOpbase();

    /**
     * Parent relative x coordinate for mouse related events
     */
    int getMouseX();

    /**
     * Jagex typed keycode
     *
     * @return
     */
    int getTypedKeyCode();

    /**
     * Get the typed character, ascii.
     *
     * @return
     */
    int getTypedKeyChar();
}
