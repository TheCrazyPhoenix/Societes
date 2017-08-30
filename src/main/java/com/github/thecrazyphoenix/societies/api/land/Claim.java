package com.github.thecrazyphoenix.societies.api.land;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SocietyElement;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;

import java.math.BigDecimal;
import java.util.Optional;
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
     * Retrieves the volume of this claim.
     * @return The volume in blocks (i.e. cubic metres)
     */
    int getClaimedVolume();

    /**
     * Retrieves the permissions that are used if a rank's permissions are undefined.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission.
     */
    PermissionHolder<ClaimPermission> getDefaultPermissions();

    /**
     * Retrieves the permissions of a given rank.
     * @param rank The rank whose permissions to retrieve.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission if present, otherwise {@link Optional#empty()}.
     */
    Optional<PermissionHolder<ClaimPermission>> getPermissions(MemberRank rank);

    /**
     * Sets the given rank's permissions.
     * Use this method if {@link #getPermissions(MemberRank)} returns {@link Optional#empty()} and you wish to modify the permissions.
     * @param rank The rank whose permissions to set.
     * @param permissions The object holding those permissions. If null, the rank will use the default permissions.
     */
    void setPermissions(MemberRank rank, PermissionHolder<ClaimPermission> permissions);

    /**
     * Retrieves the permissions of a given sub-society or allied society.
     * @param subSociety The society whose permissions to retrieve.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission if present, otherwise {@link Optional#empty()}.
     */
    Optional<PermissionHolder<ClaimPermission>> getPermissions(Society subSociety);

    /**
     * Sets the given society's permissions.
     * Use this method if {@link #getPermissions(Society)} returns {@link Optional#empty()} and you wish to modify the permissions.
     * @param society The society whose permissions to set. If null, the society will use the default permissions.
     * @param permissions The object holding those permissions.
     */
    void setPermissions(Society society, PermissionHolder<ClaimPermission> permissions);

    /**
     * Retrieves all the member claims this claim contains.
     * This set must be mutable.
     * @return The retrieved member claims as a Set.
     */
    Set<MemberClaim> getMemberClaims();

    /**
     * Retrieves the land tax paid by members who own land in this claim.
     * @return The land tax rate per block per day.
     */
    BigDecimal getLandTax();

    /**
     * Sets the land tax.
     * @param value The rate per block per day to set it to.
     */
    void setLandTax(BigDecimal value);

    /**
     * Retrieves the cost of buying land in this claim.
     * This defines the value of member claims.
     * @return The land value per block.
     */
    BigDecimal getLandValue();

    /**
     * Sets the land value.
     * @param value The value per block to set it to.
     */
    void setLandValue(BigDecimal value);
}
