package com.github.thecrazyphoenix.societies.api.land;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.api.Member;
import com.github.thecrazyphoenix.societies.api.SocietyElement;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;

import java.util.Optional;

/**
 * Models a member claim (as in owned by a player) within a society claim.
 * Players must buy these from the society and can sell them back if they so wish.
 */
public interface MemberClaim extends SocietyElement {
    /**
     * Checks if the given block is claimed.
     * @param block The coordinates of the block to check.
     * @return True if this claim protects that block, false otherwise.
     */
    boolean isClaimed(Vector3i block);

    /**
     * Retrieves the volume of this claim.
     * @return The volume of the claim in blocks (cubic metres).
     */
    int getClaimedVolume();

    /**
     * Retrieves the owner of this claim.
     * @return The owner if this claim is owned, {@link Optional#empty()} otherwise.
     */
    Optional<Member> getOwner();

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
     * @return The retrieved permissions as a PermissionHolder of ClaimPermission.
     */
    PermissionHolder<ClaimPermission> getPermissions(Member member);
}
