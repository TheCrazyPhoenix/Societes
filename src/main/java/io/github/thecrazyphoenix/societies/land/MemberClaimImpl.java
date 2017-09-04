package io.github.thecrazyphoenix.societies.land;

import com.flowpowered.math.vector.Vector3i;
import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.land.Claim;
import io.github.thecrazyphoenix.societies.api.land.MemberClaim;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.event.MemberClaimChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbsolutePermissionHolder;
import org.spongepowered.api.event.cause.Cause;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemberClaimImpl extends CuboidImpl implements MemberClaim {
    private Claim parent;
    private Member owner;

    private PermissionHolder<ClaimPermission> defaultPermissions;
    private Map<Member, PermissionHolder<ClaimPermission>> memberPermissions;
    private Map<Member, PermissionHolder<ClaimPermission>> viewMemberPermissions;

    public MemberClaimImpl(Societies societies, Claim parent, Member owner, PermissionHolder<ClaimPermission> defaultPermissions, Vector3i corner1, Vector3i corner2, Cause cause) {
        super(societies, parent.getSociety(), corner1, corner2);
        this.parent = parent;
        this.owner = owner;
        this.defaultPermissions = defaultPermissions;
        memberPermissions = new HashMap<>();
        viewMemberPermissions = Collections.unmodifiableMap(memberPermissions);
        if (!societies.queueEvent(new MemberClaimChangeEventImpl.Create(cause, this))) {
            parent.getMemberClaims().add(this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    @Override
    public Claim getParent() {
        return parent;
    }

    @Override
    public Optional<Member> getOwner() {
        return Optional.ofNullable(owner);
    }

    @Override
    public boolean setOwner(Member newOwner, Cause cause) {
        if (!societies.queueEvent(new MemberClaimChangeEventImpl.ChangeOwner(cause, this, newOwner))) {
            owner = newOwner;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public PermissionHolder<ClaimPermission> getDefaultPermissions() {
        return defaultPermissions;
    }

    @Override
    public Optional<PermissionHolder<ClaimPermission>> getPermissions(Member member) {
        return member == owner ? Optional.of(AbsolutePermissionHolder.CLAIM) : Optional.ofNullable(memberPermissions.get(member));
    }

    @Override
    public Map<Member, PermissionHolder<ClaimPermission>> getMemberPermissions() {
        return viewMemberPermissions;
    }

    @Override
    public void setPermissions(Member member, PermissionHolder<ClaimPermission> permissions) {
        if (member == owner) {
            throw new IllegalArgumentException("attempt to overwrite owner's member claim permissions");
        }
        memberPermissions.put(member, permissions);
        societies.onSocietyModified();
    }
}
