package com.github.thecrazyphoenix.societies.api.land;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.api.Society;
import com.github.thecrazyphoenix.societies.api.SocietyElement;
import com.github.thecrazyphoenix.societies.api.SubSociety;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.rank.MemberRank;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Models a claim owned by a society.
 */
public interface Claim extends SocietyElement {
    /**
     * Checks if this claim protects the given block.
     * @param block The block to check
     * @return True if the block is protected, false otherwise.
     */
    boolean isClaimed(Vector3i block);

    /**
     * Retrieves the permissions that are used if a rank's permission is undefined.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission.
     */
    PermissionHolder<ClaimPermission> getDefaultPermissions();

    /**
     * Retrieves the permissions of a given rank.
     * @param rank The rank whose permissions to retrieve.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission.
     */
    PermissionHolder<ClaimPermission> getPermissions(MemberRank rank);

    /**
     * Retrieves the permissions of a given sub-society or allied society.
     * @param subSociety The society whose permissions to retrieve.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission.
     */
    PermissionHolder<ClaimPermission> getPermissions(Society subSociety);

    /**
     * Retrieves all the member claims this claim contains.
     * @return The retrieved member claims as a Set.
     */
    Set<MemberClaim> getMemberClaims();

    /**
     * Retrieves the land tax paid by members who own land in this claim.
     * @return The land tax rate per block per day.
     */
    BigDecimal getLandTax();

    /**
     * Retrieves the cost of buying land in this claim.
     * This defines the value of member claims.
     * @return The land value per block.
     */
    BigDecimal getLandValue();
}
