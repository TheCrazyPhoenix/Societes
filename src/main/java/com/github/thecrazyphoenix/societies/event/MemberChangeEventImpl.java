package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.MemberChangeEvent;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.land.MemberClaim;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

public class MemberChangeEventImpl extends SocietyChangeEventImpl implements MemberChangeEvent {
    private Member member;

    public MemberChangeEventImpl(Cause cause, Society society, Member member) {
        super(cause, society);
        this.member = member;
    }

    @Override
    public Member getMember() {
        return member;
    }

    public static class Create extends TaxableChangeEventImpl.Create implements MemberChangeEvent.Create {
        private Member member;

        public Create(Cause cause, Society society, Member member) {
            super(cause, society, member);
            this.member = member;
        }

        @Override
        public Member getMember() {
            return member;
        }
    }

    public static class Destroy extends TaxableChangeEventImpl.Destroy implements MemberChangeEvent.Destroy {
        private Member member;

        public Destroy(Cause cause, Society society, Member member) {
            super(cause, society, member);
            this.member = member;
        }

        @Override
        public Member getMember() {
            return member;
        }
    }

    public static class ChangeRank extends MemberChangeEventImpl implements MemberChangeEvent.ChangeRank {
        private MemberRank newRank;

        public ChangeRank(Cause cause, Society society, Member member, MemberRank newRank) {
            super(cause, society, member);
            this.newRank = newRank;
        }

        @Override
        public MemberRank getNewRank() {
            return newRank;
        }
    }

    public static class ChangeTitle extends MemberChangeEventImpl implements MemberChangeEvent.ChangeTitle {
        private Text newTitle;

        public ChangeTitle(Cause cause, Society society, Member member, Text newTitle) {
            super(cause, society, member);
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

        public ChangePermission(Cause cause, Society society, Member member, MemberPermission changedPermission, PermissionState newValue) {
            super(cause, society, member);
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

        public ChangeClaimPermission(Cause cause, Society society, Member member, Claim claim, MemberClaim memberClaim, ClaimPermission changedPermission, PermissionState newValue) {
            super(cause, society, member);
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
    }
}
