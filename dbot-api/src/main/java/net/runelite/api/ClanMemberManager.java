package net.runelite.api;

public interface ClanMemberManager extends NameableContainer<ClanMember>
{
    /**
     * Gets the clan owner of the currently joined clan chat
     *
     * @return
     */
    String getClanOwner();

    /**
     * Gets the clan chat name of the currently joined clan chat
     *
     * @return
     */
    String getClanChatName();
}

