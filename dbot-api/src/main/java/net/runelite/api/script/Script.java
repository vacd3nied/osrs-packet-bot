package net.runelite.api.script;

import java.util.Map;
import java.util.Queue;

public interface Script {

    Queue<Runnable> getExecQueue(ScriptState state);

    String getProperty(String name);

    void setProperty(String name, String value);

    void addProperties(Map<String, String> properties);

    ScriptState getState();

    void setState(ScriptState state);

    Script getDelegate();

    void yield(Script script);

}
