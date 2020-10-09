package net.runelite.api;

import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Inventory extends Openable {

    Widget getWidget();

    Collection<WidgetItem> getWidgetItems();

    boolean interact(final WidgetItem item, final ItemActions action);

    default List<WidgetItem> getWidgetItems(Integer... ids) {
        final List<Integer> idsList = Arrays.asList(ids);

        return getWidgetItems().stream().filter(w ->
                idsList.contains(w.getId())
        ).collect(Collectors.toList());
    }

    default List<WidgetItem> getWidgetItems(int... ids) {
        final List<Integer> idsList = Arrays.stream(ids).boxed().collect(Collectors.toList());

        return getWidgetItems().stream().filter(w ->
                idsList.contains(w.getId())
        ).collect(Collectors.toList());
    }

    default int getCount() {
        return getWidgetItems().size();
    }

    default boolean isFull() {
        return getCount() == 28;
    }

    default boolean isEmpty() {
        return getCount() == 0;
    }
}
