package net.runelite.api.device;

public interface Keyboard {

    void typeString(String str, boolean pressEnter, int sleepMin, int sleepMax);

    void typeString(String str, int sleepMin, int sleepMax);

    void typeString(String str, boolean pressEnter);

    void typeChar(char ch);

    void typeKey(char ch, int keycode);

    void typeKey(int keyCode);

}
