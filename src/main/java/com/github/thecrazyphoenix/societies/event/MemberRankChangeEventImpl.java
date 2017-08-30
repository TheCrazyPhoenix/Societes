package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.MemberRankChangeEvent;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

public class MemberRankChangeEventImpl extends SocietyChangeEventImpl implements MemberRankChangeEvent {
    private MemberRank memberRank;

    public MemberRankChangeEventImpl(Cause cause, Society society, MemberRank memberRank) {
        super(cause, society);
        this.memberRank = memberRank;
    }

    @Override
    public MemberRank getMemberRank() {
        return memberRank;
    }

    public static class Create extends TaxableChangeEventImpl.Create implements MemberRankChangeEvent.Create {
        private MemberRank memberRank;

        public Create(Cause cause, Society society, MemberRank memberRank) {
            super(cause, society, memberRank);
            this.memberRank = memberRank;
        }

        @Override
        public MemberRank getMemberRank() {
            return memberRank;
        }
    }

    public static class Destroy extends TaxableChangeEventImpl.Destroy implements MemberRankChangeEvent.Destroy {
        private MemberRank memberRank;

        public Destroy(Cause cause, Society society, MemberRank memberRank) {
            super(cause, society, memberRank);
            this.memberRank = memberRank;
        }

        @Override
        public MemberRank getMemberRank() {
            return memberRank;
        }
    }

    public static class ChangeTitle extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangeTitle {
        private Text newTitle;

        public ChangeTitle(Cause cause, Society society, MemberRank memberRank, Text newTitle) {
            super(cause, society, memberRank);
            this.newTitle = newTitle;
        }

        @Override
        public Text getNewTitle() {
            return newTitle;
        }
    }

    public static class ChangeDescription extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangeDescription {
        private Text newDescription;

        public ChangeDescription(Cause cause, Society society, MemberRank memberRank, Text newDescription) {
            super(cause, society, memberRank);
            this.newDescription = newDescription;
        }

        @Override
        public Text getNewDescription() {
            return newDescription;
        }
    }

    public static class ChangePermission extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangePermission {
        private MemberPermission changedPermission;
        private PermissionState newValue;

        public ChangePermission(Cause cause, Society society, MemberRank memberRank, MemberPermission changedPermission, PermissionState newValue) {
            super(cause, society, memberRank);
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

    public static class ChangeClaimPermission extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangeClaimPermission {
        private Claim claim;
        private ClaimPermission changedPermission;
        private PermissionState newValue;

        public ChangeClaimPermission(Cause cause, Society society, MemberRank memberRank, Claim claim, ClaimPermission changedPermission, PermissionState newValue) {
            super(cause, society, memberRank);
            this.claim = claim;
            this.changedPermission = changedPermission;
            this.newValue = newValue;
        }

        @Override
        public Claim getClaim() {
            return claim;
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
