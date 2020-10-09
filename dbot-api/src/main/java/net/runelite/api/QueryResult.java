package net.runelite.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import net.runelite.api.coords.LocalPoint;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@EqualsAndHashCode
public @Data class QueryResult<EntityType> implements Iterable<EntityType> {

    @NonNull
    private final List<EntityType> results;

    @NonNull
    @Override
    public Iterator<EntityType> iterator() {
        return results.iterator();
    }

    @NonNull
    public Stream<EntityType> stream() {
        return results.stream();
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public int count() {
        return results.size();
    }

    public EntityType first() {
        if (count() > 0) {
            return results.get(0);
        }

        return null;
    }

    public static <EntityType extends TileObject> Optional<EntityType> closestTileObject(
            final QueryResult<EntityType> queryResult,
            final LocalPoint localPoint) {
        return queryResult.stream().min(Comparator.comparingInt(a -> (a).getLocalLocation().distanceTo(localPoint)));
    }

    public static <EntityType extends Actor> Optional<EntityType> closestActor(
            final QueryResult<EntityType> queryResult,
            final LocalPoint localPoint) {
        return queryResult.stream().min(Comparator.comparingInt(a -> (a).getLocalLocation().distanceTo(localPoint)));
    }
}
