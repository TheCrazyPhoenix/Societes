package com.github.thecrazyphoenix.societies.api.land;

import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.SocietyElement;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Models a claim owned by a society.
 */
public interface Claim extends SocietyElement, ClaimedLand {
    /**
     * Retrieves the cuboids that make up this claim.
     * The cuboids may overlap.
     * @return The cuboids as an unmodifiable set.
     */
    Set<Cuboid> getClaimCuboids();

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
     * Retrieves all the permissions associated with member ranks.
     * @return The permissions as an unmodifiable member rank-indexed map of permission holders of claim permissions.
     */
    Map<MemberRank, PermissionHolder<ClaimPermission>> getMemberRankPermissions();

    /**
     * Retrieves the permissions of a given sub-society.
     * @param subSociety The society whose permissions to retrieve.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission if present, otherwise {@link Optional#empty()}.
     */
    Optional<PermissionHolder<ClaimPermission>> getPermissions(SubSociety subSociety);

    /**
     * Sets the given sub-society's permissions.
     * Use this method if {@link #getPermissions(SubSociety)} returns {@link Optional#empty()} and you wish to modify the permissions.
     * @param subSociety The sub-society whose permissions to set.
     * @param permissions The object holding those permissions. If null, the sub-society will use the default permissions.
     */
    void setPermissions(SubSociety subSociety, PermissionHolder<ClaimPermission> permissions);

    /**
     * Retrieves all the permissions associated with sub-societies.
     * @return The permissions as an unmodifiable sub-society-indexed map of permission holders of claim permissions.
     */
    Map<SubSociety, PermissionHolder<ClaimPermission>> getSubSocietyPermissions();

    /**
     * Retrieves all the member claims this claim contains.
     * Before modifying this set, users should check {@link #isContaining(Cuboid)} on the MemberClaim to ensure that the member claim is correctly classified.
     * Modifying this set with an invalid value may cause undefined behaviour.
     * @return The retrieved member claims as a mutable set.
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
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setLandTax(BigDecimal value, Cause cause);

    /**
     * Retrieves the cost of buying land in this claim.
     * This defines the value of member claims.
     * @return The land value per block.
     */
    BigDecimal getLandValue();

    /**
     * Sets the land value.
     * @param value The value per block to set it to.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setLandValue(BigDecimal value, Cause cause);
}
