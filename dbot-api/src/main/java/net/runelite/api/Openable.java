package net.runelite.api;

import net.runelite.api.script.Script;

public interface Openable {

    boolean isOpen();

    Script open();

}
