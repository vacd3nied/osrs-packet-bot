package com.dbot.client.script;

import lombok.SneakyThrows;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.script.PollingScript;
import net.runelite.http.api.worlds.WorldClient;
import net.runelite.http.api.worlds.WorldType;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

public class Login extends PollingScript {

    private WorldClient worldClient = new WorldClient(new OkHttpClient());

    public Login() {
        super(0);
    }

    @Override
    @SneakyThrows
    public void poll() {
        if (client.getGameState() == GameState.LOGIN_SCREEN) {
            priorityBlock();

            client.setUsername(getProperty("username"));
            client.setPassword(getProperty("password"));

            final World world = getGameWorld();

            if (world != null && client.getGameState() != GameState.LOGGING_IN) {
                client.changeWorld(world);
                client.setGameState(GameState.LOGGING_IN);
            } else if (world == null) {
                System.err.println("No game world found!");
            }

        } else {
            priorityUnblock();
        }
    }

    private World getGameWorld() {
        try {
            final Optional<net.runelite.http.api.worlds.World> optional = worldClient.lookupWorlds().getWorlds().stream()
                    .filter(Objects::nonNull)
                    .filter(w -> "true".equals(getProperty("member")) == w.getTypes().contains(WorldType.MEMBERS))
                    .filter(w -> !w.getTypes().contains(WorldType.PVP))
                    .filter(w -> !w.getTypes().contains(WorldType.DEADMAN))
                    .filter(w -> !w.getTypes().contains(WorldType.DEADMAN_TOURNAMENT))
                    .filter(w -> !w.getTypes().contains(WorldType.SKILL_TOTAL))
                    .filter(w -> !w.getTypes().contains(WorldType.HIGH_RISK))
                    .min(Comparator.comparingInt(net.runelite.http.api.worlds.World::getPlayers));

            if (optional.isPresent()) {
                net.runelite.http.api.worlds.World w = optional.get();

                World world = client.createWorld();
                world.setId(w.getId());
                world.setAddress(w.getAddress());
                world.setActivity(w.getActivity());
                world.setPlayerCount(w.getPlayers());
                world.setLocation(w.getLocation());
                world.setTypes(convertTypes(w.getTypes()));

                return world;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private EnumSet<net.runelite.api.WorldType> convertTypes(final EnumSet<WorldType> types) {
        final EnumSet<net.runelite.api.WorldType> set = EnumSet.noneOf(net.runelite.api.WorldType.class);

        for (final WorldType type : types) {
            set.add(net.runelite.api.WorldType.valueOf(type.name()));
        }

        return set;
    }
}
