package com.dbot.client;

import com.dbot.client.callback.Hooks;
import com.dbot.client.impl.packet.BankImpl;
import com.dbot.client.impl.packet.InventoryImpl;
import com.dbot.client.impl.packet.MovementImpl;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

import java.applet.Applet;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.inject.Singleton;

import net.runelite.api.Bank;
import net.runelite.api.Client;
import com.dbot.client.task.Scheduler;
import net.runelite.api.Inventory;
import net.runelite.api.Movement;
import net.runelite.api.hooks.Callbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBotModule extends AbstractModule {

    private final Supplier<Applet> clientLoader;
    private final boolean rendering;

    public DBotModule(final Supplier<Applet> clientLoader, final boolean rendering) {
        this.clientLoader = clientLoader;
        this.rendering = rendering;
    }

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).toInstance(Executors.newSingleThreadScheduledExecutor());
        bind(Scheduler.class);

        bind(EventBus.class)
                .toInstance(new EventBus());
        bind(Random.class).toInstance(new Random());

        bind(Inventory.class).toInstance(new InventoryImpl());
        bind(Bank.class).toInstance(new BankImpl());
        bind(Movement.class).toInstance(new MovementImpl());
        bind(Callbacks.class).toInstance(new Hooks());

        bind(Logger.class)
                .annotatedWith(Names.named("Core Logger"))
                .toInstance(LoggerFactory.getLogger(DBot.class));
    }

    @Provides
    @Singleton
    Applet provideApplet() {
        return clientLoader.get();
    }

    @Provides
    @Singleton
    Client provideClient(@Nullable Applet applet) {
        return applet instanceof Client ? (Client) applet : null;
    }
}
