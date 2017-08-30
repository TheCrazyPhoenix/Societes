package com.github.thecrazyphoenix.societies.api.society;

import com.github.thecrazyphoenix.societies.api.land.Claim;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.Map;
import java.util.Set;

/**
 * Models a society.
 */
public interface Society {
    /**
     * Retrieves the name of this society.
     * The unformatted value must be unique, non-empty and may only contain alphanumeric characters, spaces and -&#;:.?!
     * @return The name as a Text object.
     */
    Text getName();

    /**
     * Retrieves the abbreviated name of this society.
     * The unformatted value must not be empty and may only contain alphanumeric characters and -&#;:.?!
     * @return The abbreviated name as a Text object.
     */
    Text getAbbreviatedName();

    /**
     * Retrieves the leaders of this society.
     * @return The leaders as a set of members.
     */
    Set<Member> getLeaders();

    /**
     * Retrieves the members of this society. This includes the leaders.
     * @return The members as a set.
     */
    Set<Member> getMembers();

    /**
     * Retrieves the societies directly under this society's authority. Indirect sub-societies are not included.
     * @return The sub-societies as a set.
     */
    Set<SubSociety> getSubSocieties();

    /**
     * Retrieves the claims owned by this society.
     * If this society is a sub-society, these claims will be in the claims of the parent society.
     * @return The claims as a set.
     */
    Set<Claim> getClaims();

    /**
     * Retrieves the available ranks of this society.
     * @return The ranks as a string-indexed map.
     */
    Map<String, MemberRank> getRanks();

    /**
     * Retrieves this society's account.
     * @return The account as an Account.
     */
    Account getAccount();
}