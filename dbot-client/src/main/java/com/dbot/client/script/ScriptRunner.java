package com.dbot.client.script;

import bsh.Interpreter;
import lombok.SneakyThrows;
import net.runelite.api.GameState;
import net.runelite.api.script.AbstractScript;
import net.runelite.api.script.PollingScript;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ScriptRunner extends PollingScript {

    private static final String PATH = "dbot-client/src/main/java/com/dbot/client/script/Script.java";
    private final Interpreter bsh = new bsh.Interpreter();
    private final Scanner scanner = new Scanner(System.in);

    @SneakyThrows
    public ScriptRunner() {
        super(0);
    }

    private void eval() {
        try {
            Class<?> eval = (Class<?>) bsh.eval(new InputStreamReader(new FileInputStream(PATH)));
            eval.getDeclaredMethod("eval", AbstractScript.class).invoke(null, this);
        } catch (Throwable e) {
            System.err.println("Error... " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void poll() {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        String line = scanner.nextLine();

        if ("eval".equals(line) && client.getGameState() == GameState.LOGGED_IN) {
            try {
                eval();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
