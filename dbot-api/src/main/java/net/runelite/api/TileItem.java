package net.runelite.api;

public interface TileItem extends Renderable
{
    /**
     * @return the ID of the item
     * @see ItemID
     */
    int getId();

    int getQuantity();
}
