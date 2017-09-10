package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import io.github.thecrazyphoenix.societies.event.ChangeMemberClaimEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbsolutePermissionHolder;
import io.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemberClaimImpl extends CuboidImpl implements MemberClaim {
    private Member owner;

    private PermissionHolder<ClaimPermission> defaultPermissions;
    private Map<Member, PermissionHolder<ClaimPermission>> memberPermissions;
    private Map<Member, PermissionHolder<ClaimPermission>> viewMemberPermissions;

    private MemberClaimImpl(Builder builder) {
        super(builder);
        defaultPermissions = CommonMethods.mapToHolder(societies, society, PowerlessPermissionHolder.CLAIM, builder.defaultPermissions);
        viewMemberPermissions = Collections.unmodifiableMap(memberPermissions = new HashMap<>());
        builder.memberPermissions.forEach((u, m) -> memberPermissions.put(u, CommonMethods.mapToHolder(societies, society, PowerlessPermissionHolder.CLAIM, m)));
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
        CommonMethods.checkMatchingSociety(this, newOwner);
        if (!societies.queueEvent(new ChangeMemberClaimEventImpl.ChangeOwner(cause, this, newOwner))) {
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

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new ChangeMemberClaimEventImpl.Destroy(cause, this))) {
            parent.getMemberClaimsRaw().remove(this);
            return true;
        }
        return false;
    }

    public static class Builder extends CuboidImpl.Builder implements MemberClaim.Builder {
        private final Societies societies;
        private final ClaimImpl parent;
        private Map<ClaimPermission, PermissionState> defaultPermissions;
        private Map<Member, Map<ClaimPermission, PermissionState>> memberPermissions;

        Builder(Societies societies, ClaimImpl parent) {
            super(societies, parent);
            this.societies = societies;
            this.parent = parent;
            defaultPermissions = new HashMap<>();
            memberPermissions = new HashMap<>();
        }

        @Override
        public MemberClaim.Builder defaultPermission(ClaimPermission permission, PermissionState value) {
            defaultPermissions.put(permission, value);
            return this;
        }

        @Override
        public MemberClaim.Builder memberPermission(Member member, ClaimPermission permission, PermissionState value) {
            memberPermissions.computeIfAbsent(member, k -> new HashMap<>()).put(permission, value);
            return this;
        }

        @Override
        public Optional<MemberClaimImpl> build(Cause cause) {
            super.build();
            MemberClaimImpl memberClaim = new MemberClaimImpl(this);
            if (!societies.queueEvent(new ChangeMemberClaimEventImpl.Create(cause, memberClaim))) {
                parent.getMemberClaimsRaw().add(memberClaim);
                societies.onSocietyModified();
                return Optional.of(memberClaim);
            }
            return Optional.empty();
        }
    }
}
