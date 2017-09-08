package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Models a claim owned by a society.
 */
public interface Claim extends SocietyElement, ClaimedLand, ContractAuthority {
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
     * @return The retrieved member claims as an unmodifiable set.
     */
    Set<MemberClaim> getMemberClaims();

    /**
     * Retrieves the land tax paid by members who own land in this claim.
     * @return The land tax rate per block per day as an unmodifiable currency-indexed map.
     */
    Map<Currency, BigDecimal> getLandTax();

    /**
     * Sets the land tax for the given currency.
     * @param currency The currency whose tax value to set.
     * @param value The rate per block per day to set it to.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setLandTax(Currency currency, BigDecimal value, Cause cause);

    /**
     * Retrieves the cost of buying land in this claim.
     * This defines the value of member claims.
     * @return The land value per block as an unmodifiable currency-indexed.
     */
    Map<Currency, BigDecimal> getLandValue();

    /**
     * Sets the land value for the given currency.
     * @param currency The currency whose tax value to set.
     * @param value The value per block to set it to.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setLandValue(Currency currency, BigDecimal value, Cause cause);

    /**
     * Creates a new member claim builder with this claim as the parent.
     * @return The created builder.
     */
    MemberClaim.Builder memberClaimBuilder();

    /**
     * Creates a new cuboid builder with this claim as the parent.
     * @return The created builder.
     */
    Cuboid.Builder cuboidBuilder();

    /**
     * Attempts to destroy this object.
     * @param cause The cause of the construction of the object.
     * @return True if the object was destroyed, false if the event was cancelled.
     */
    boolean destroy(Cause cause);

    /**
     * Enables the construction of a new object.
     */
    interface Builder {
        /**
         * Sets the land tax value for the given currency.
         * This parameter defaults to {@link BigDecimal#ZERO}
         * @return This object for chaining.
         */
        Builder landTax(Currency currency, BigDecimal landTax);

        /**
         * Sets the created claim's land value.
         * This parameter defaults to {@link BigDecimal#ZERO}
         * @return This object for chaining.
         */
        Builder landValue(Currency currency, BigDecimal landValue);

        /**
         * Sets the default given permission to the given value for the created claim.
         * All permissions default to {@link PermissionState#NONE}.
         * @return This object for chaining.
         */
        Builder defaultPermission(ClaimPermission permission, PermissionState value);

        /**
         * Sets the given member rank's given permission to the given value for the created claim.
         * All permissions default to {@link PermissionState#NONE}.
         * @return This object for chaining.
         */
        Builder memberRankPermission(MemberRank rank, ClaimPermission permission, PermissionState value);

        /**
         * Sets the given sub-society's given permission to the given value for the created claim.
         * All permissions default to {@link PermissionState#NONE}.
         * @return This object for chaining.
         */
        Builder subSocietyPermission(SubSociety subSociety, ClaimPermission permission, PermissionState value);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends Claim> build(Cause cause);
    }
}
