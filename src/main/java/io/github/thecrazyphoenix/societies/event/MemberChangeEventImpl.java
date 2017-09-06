package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.society.Cuboid;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.event.MemberChangeEvent;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

public class MemberChangeEventImpl extends TaxableChangeEventImpl implements MemberChangeEvent {
    private Member member;

    public MemberChangeEventImpl(Cause cause, Member member) {
        super(cause, member);
        this.member = member;
    }

    @Override
    public Member getMember() {
        return member;
    }

    public static class Create extends TaxableChangeEventImpl.Create implements MemberChangeEvent.Create {
        private Member member;

        public Create(Cause cause, Member member) {
            super(cause, member);
            this.member = member;
        }

        @Override
        public Member getMember() {
            return member;
        }
    }

    public static class Destroy extends TaxableChangeEventImpl.Destroy implements MemberChangeEvent.Destroy {
        private Member member;

        public Destroy(Cause cause, Member member) {
            super(cause, member);
            this.member = member;
        }

        @Override
        public Member getMember() {
            return member;
        }
    }

    public static class ChangeRank extends MemberChangeEventImpl implements MemberChangeEvent.ChangeRank {
        private MemberRank newRank;

        public ChangeRank(Cause cause, Member member, MemberRank newRank) {
            super(cause, member);
            this.newRank = newRank;
        }

        @Override
        public MemberRank getNewRank() {
            return newRank;
        }
    }

    public static class ChangeTitle extends MemberChangeEventImpl implements MemberChangeEvent.ChangeTitle {
        private Text newTitle;

        public ChangeTitle(Cause cause, Member member, Text newTitle) {
            super(cause, member);
            this.newTitle = newTitle;
        }

        @Override
        public Text getNewTitle() {
            return newTitle;
        }
    }

    public static class ChangePermission extends MemberChangeEventImpl implements MemberChangeEvent.ChangePermission {
        private MemberPermission changedPermission;
        private PermissionState newValue;

        public ChangePermission(Cause cause, Member member, MemberPermission changedPermission, PermissionState newValue) {
            super(cause, member);
            this.changedPermission = changedPermission;
            this.newValue = newValue;
        }

        @Override
        public MemberPermission getChangedPermission() {
            return changedPermission;
        }

        @Override
        public PermissionState getNewValue() {
            return newValue;
        }
    }

    public static class ChangeClaimPermission extends MemberChangeEventImpl implements MemberChangeEvent.ChangeClaimPermission {
        private Claim claim;
        private MemberClaim memberClaim;
        private ClaimPermission changedPermission;
        private PermissionState newValue;

        public ChangeClaimPermission(Cause cause, Member member, Claim claim, MemberClaim memberClaim, ClaimPermission changedPermission, PermissionState newValue) {
            super(cause, member);
            this.claim = claim;
            this.memberClaim = memberClaim;
            this.changedPermission = changedPermission;
            this.newValue = newValue;
        }

        @Override
        public Claim getClaim() {
            return claim;
        }

        @Override
        public MemberClaim getMemberClaim() {
            return memberClaim;
        }

        @Override
        public ClaimPermission getChangedPermission() {
            return changedPermission;
        }

        @Override
        public PermissionState getNewValue() {
            return newValue;
        }

        @Override
        public Cuboid getCuboid() {
            return memberClaim;
        }
    }
}
