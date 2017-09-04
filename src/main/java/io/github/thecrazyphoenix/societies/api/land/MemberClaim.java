package io.github.thecrazyphoenix.societies.api.land;

import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.SocietyElement;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import org.spongepowered.api.event.cause.Cause;

import java.util.Map;
import java.util.Optional;

/**
 * Models a member claim (as in owned by a player) within a society claim.
 * Players must buy these from the society and can sell them back if they so wish.
 */
public interface MemberClaim extends SocietyElement, Cuboid {
    /**
     * Retrieves the owner of this claim.
     * @return The owner if this claim is owned, {@link Optional#empty()} otherwise.
     */
    Optional<Member> getOwner();

    /**
     * Sets this member claim's new owner.
     * This will not perform the transaction associated with purchasing the claim.
     * @param newOwner The new owner of this claim.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setOwner(Member newOwner, Cause cause);

    /**
     * Retrieves the claim this member claim is in.
     * @return The parent of this claim.
     */
    Claim getParent();

    /**
     * Retrieves the permissions that are used if a member's permission is undefined.
     * @return The default permissions as a PermissionHolder of ClaimPermission.
     */
    PermissionHolder<ClaimPermission> getDefaultPermissions();

    /**
     * Retrieves the permissions of the given member in this claim.
     * @param member The member whose permissions to retrieve. The member must belong to the same society as this object.
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission if present, otherwise {@link Optional#empty()}.
     */
    Optional<PermissionHolder<ClaimPermission>> getPermissions(Member member);

    /**
     * Retrieves all the permissions associated with members.
     * @return The permissions as an unmodifiable member-indexed map of permission holders of claim permissions.
     */
    Map<Member, PermissionHolder<ClaimPermission>> getMemberPermissions();

    /**
     * Sets the given member's permissions.
     * Use this method if {@link #getPermissions(Member)} returns {@link Optional#empty()} and you wish to modify the permissions.
     * @param member The member whose permissions to set.
     * @param permissions The object holding those permissions. If null, the member will use the default permissions.
     */
    void setPermissions(Member member, PermissionHolder<ClaimPermission> permissions);
}
