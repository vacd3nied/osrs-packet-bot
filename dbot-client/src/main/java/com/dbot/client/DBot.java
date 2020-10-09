package com.dbot.client;

import com.dbot.client.script.*;
import com.dbot.client.util.BotProxySelector;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.applet.Applet;
import java.io.File;
import java.net.ProxySelector;
import java.util.*;
import javax.inject.Singleton;
import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.script.Script;
import org.slf4j.MDC;

@Slf4j
@Singleton
public class DBot {

    public static final File DBOT_DIR = new File(System.getProperty("user.home"), ".dbot");
    public static final File PROFILES_DIR = new File(DBOT_DIR, "profiles");
    public static final File LOGS_DIR = new File(DBOT_DIR, "logs");
    public static final File LOGS_FILE_NAME = new File(LOGS_DIR, "application");

    private static final List<BotService> services = Collections.synchronizedList(new LinkedList<>());
    private static final BotProxySelector proxySelector = new BotProxySelector();
    private static final Map<String, String> properties = new HashMap<>();
    private static int botCounter = 0;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        if (args.length > 2) {
            properties.put("username", args[0]);
            properties.put("password", args[1]);
            properties.put("render", "false");
        }

        properties.put("member", "false");

        if (args.length >= 3 && "render".equals(args[2])) {
            properties.put("render", "true");
        }

        PROFILES_DIR.mkdirs();
        MDC.put("logFileName", LOGS_FILE_NAME.getAbsolutePath());

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
        {
            log.error("Uncaught exception:", throwable);
            if (throwable instanceof AbstractMethodError) {
                log.error("Classes are out of date; Build with maven again.");
            }
        });

        final boolean rendering = properties.getOrDefault("render", "false").equals("true");
        ProxySelector.setDefault(proxySelector);


        launchClient(properties, rendering, Login.class);
    }

    @SafeVarargs
    private static void launchClient(final Map<String, String> properties, final boolean rendering, final Class<? extends Script>... classes) {
        final String username = properties.getOrDefault("username", "Bot-" + botCounter++);
        final ThreadGroup group = new ThreadGroup(username);

        proxySelector.addMapping(username, properties.get("proxyAddress"));

        final Thread thread = new Thread(group, () -> {
            final ClientLoader clientLoader = new ClientLoader();
            final Injector injector = Guice.createInjector(new DBotModule(clientLoader, rendering));

            final Applet applet = clientLoader.get();
            final Client client = (Client) applet;

            injector.injectMembers(client);
            applet.setSize(Constants.GAME_FIXED_WIDTH, Constants.GAME_FIXED_HEIGHT);
            client.setCallbacks(injector.getInstance(Callbacks.class));
            client.setRendering(rendering);

            boolean randomizeMachineInfo = Boolean.parseBoolean(properties.getOrDefault("randomizeMachineInfo", "false"));
            client.setRandomizeMachineInfo(randomizeMachineInfo);

            applet.init();
            applet.start();

            if (rendering) {
                JFrame frame = new JFrame("Dbot");
                JPanel panel = new JPanel();
                panel.add(applet);
                frame.add(panel);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }

            final BotService botService = new BotService();
            injector.injectMembers(botService);

            final Injector childInjector = injector.createChildInjector();
            final Script[] scripts = new Script[classes.length];

            try {
                for (int i = 0; i < classes.length; i++) {
                    final Script script = classes[i].newInstance();
                    childInjector.injectMembers(script);
                    injector.getInstance(EventBus.class).register(script);
                    scripts[i] = script;
                }

                for (final Script script : scripts) {
                    script.addProperties(properties);
                }

                botService.runScripts(scripts);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

}
