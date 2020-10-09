package net.runelite.api.queries;

import net.runelite.api.Client;
import net.runelite.api.Query;
import net.runelite.api.widgets.Widget;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WidgetQuery extends Query<Widget, WidgetQuery> {

    private Widget[] parents = null;

    @Override
    public Widget[] result(Client client) {
        Widget[] widgets = parents;

        try {
            if (widgets == null)
                widgets = client.getWidgetRoots();

            return getChildren(widgets).stream()
                    .filter(Objects::nonNull)
                    .filter(predicate)
                    .toArray(Widget[]::new);
        } catch (Exception e) {
            return new Widget[0];
        }
    }

    public void setParents(final Widget[] parents) {
        this.parents = parents;
    }

    public WidgetQuery isNotHidden() {
        and(widget -> !widget.isHidden());

        return this;
    }

    public WidgetQuery containsPoint(final Point point) {
        return containsPoint(point.x, point.y);
    }

    public WidgetQuery hasSprite(final int texture) {
        return hasSprite(new int[]{texture});
    }

    public WidgetQuery hasSprite(final int[] textures) {
        predicate = and(widget -> {
            for (int texture : textures) {
                if (widget.getSpriteId() == texture)
                    return true;
            }

            return false;
        });

        return this;
    }

    public WidgetQuery containsPoint(final int x, final int y) {
        predicate = and(widget -> widget.getBounds().contains(x, y));

        return this;
    }

    public WidgetQuery containsText(final String text) {
        predicate = and(widget ->
        {
            final String widgetText = widget.getText();

            return widgetText != null && widgetText.toLowerCase().contains(text.toLowerCase());
        });
        return this;
    }

    private List<Widget> getChildren(final Widget[] parents) {
        if (parents == null) {
            return Collections.emptyList();
        }

        final List<Widget> widgets = new LinkedList<>();

        for (Widget widget : parents) {
            widgets.addAll(getChildren(widget));
        }

        return widgets;
    }

    private List<Widget> getChildren(final Widget widget) {
        if (widget == null)
            return Collections.emptyList();

        final List<Widget> widgets = new LinkedList<>();
        widgets.add(widget);

        Widget[] children = widget.getDynamicChildren();

        for (Widget child : children) {
            if (child != null) {
                widgets.addAll(getChildren(child));
            }
        }

        children = widget.getStaticChildren();

        for (Widget child : children) {
            if (child != null) {
                widgets.addAll(getChildren(child));
            }
        }

        children = widget.getNestedChildren();

        for (Widget child : children) {
            if (child != null) {
                widgets.addAll(getChildren(child));
            }
        }

        return widgets;
    }
}
