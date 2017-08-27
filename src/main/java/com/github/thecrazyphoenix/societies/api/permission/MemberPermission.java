package com.github.thecrazyphoenix.societies.api.permission;

/**
 * Stores a list of possible permissions a member can have.
 */
public enum MemberPermission {
    /**
     * Allows the player to view the current members of the society.
     */
    VIEW_MEMBERS,

    /**
     * Allows the player to view the current sub-societies and, if they also have VIEW_MEMBERS, their members.
     */
    VIEW_SUBSOCIETIES,

    /**
     * Allows the players to kill non-members in the society's territory.
     * If less than a certain percentage of members with this permission are online, the territory is granted immunity to attacks and only territory with open borders can be visited by foreign players.
     * Otherwise, anyone can enter the territory, attack the players inside it and steal territory.
     */
    FIGHT_WARS,

    /**
     * Allows the player to spend the budget and assign taxes and salaries.
     */
    MANAGE_ECONOMY,

    /**
     * Allows the player to invite and kick players from the society.
     */
    MANAGE_MEMBERS,

    /**
     * Allows the player to create and delete alliances with other societies.
     * It also allows them to manage what information allies have access to.
     * Diplomatic decisions are visible to all members, the affected society and allies with which they are shared.
     */
    MANAGE_DIPLOMACY,

    /**
     * Allows the player to claim, unclaim, allocate and deallocate territory.
     * It also allows them to open the territory's borders to all Allies, non-enemies or sub-societies
     */
    MANAGE_TERRITORY,

    /**
     * Allows the player to manage the permissions of all ranks below their own.
     * They may only grant permissions their rank has. Individual permissions cannot be passed down.
     */
    MANAGE_PERMISSIONS,

    /**
     * Allows the player to change the leaders of sub-societies, as well as the ability to use all other society commands on them as if they were a member of the sub-society.
     * Those commands ignore the sub-society's permissions, but not the player's permissions.
     * If the player also has MANAGE_ECONOMY, they will be able to set sub-societies' daily grant and taxes.
     * If the player also has MANAGE_PERMISSIONS, they will be able to set sub-societies' permissions, but the player can only grant permissions they have.
     */
    MANAGE_SUBSOCIETIES,

    /**
     * Allows the player to rename the society and change its type.
     */
    MANAGE_SOCIETY
}
