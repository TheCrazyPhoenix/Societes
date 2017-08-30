package com.github.thecrazyphoenix.societies.land;

import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.land.MemberClaim;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.permission.AbsolutePermissionHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemberClaimImpl extends CuboidClaim implements MemberClaim {
    private Claim parent;
    private Member owner;

    private PermissionHolder<ClaimPermission> defaultPermissions;
    private Map<Member, PermissionHolder<ClaimPermission>> memberPermissions;

    public MemberClaimImpl(Claim parent, Member owner, PermissionHolder<ClaimPermission> defaultPermissions) {
        super(parent.getSociety());
        this.parent = parent;
        this.owner = owner;
        this.defaultPermissions = defaultPermissions;
        memberPermissions = new HashMap<>();
        parent.getMemberClaims().add(this);
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
    public PermissionHolder<ClaimPermission> getDefaultPermissions() {
        return defaultPermissions;
    }

    @Override
    public Optional<PermissionHolder<ClaimPermission>> getPermissions(Member member) {
        return member == owner ? Optional.of(AbsolutePermissionHolder.CLAIM) : Optional.ofNullable(memberPermissions.get(member));
    }

    @Override
    public void setPermissions(Member member, PermissionHolder<ClaimPermission> permissions) {
        if (member == owner) {
            throw new IllegalArgumentException("attempt to overwrite owner's member claim permissions");
        }
    }
}
