package net.runelite.api;

public interface NameableContainer<T extends Nameable>
{
    /**
     * Get the number of members in this container
     *
     * @return
     */
    int getCount();

    /**
     * Get the members in this container
     *
     * @return
     */
    T[] getMembers();

    /**
     * Find a nameable by name
     *
     * @param name the name
     * @return
     */
    T findByName(String name);
}

