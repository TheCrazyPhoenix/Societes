package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.MemberRankChangeEvent;
import io.github.thecrazyphoenix.societies.api.land.Claim;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class MemberRankChangeEventImpl extends TaxableChangeEventImpl implements MemberRankChangeEvent {
    private MemberRank memberRank;

    public MemberRankChangeEventImpl(Cause cause, MemberRank memberRank) {
        super(cause, memberRank);
        this.memberRank = memberRank;
    }

    @Override
    public MemberRank getMemberRank() {
        return memberRank;
    }

    public static class Create extends TaxableChangeEventImpl.Create implements MemberRankChangeEvent.Create {
        private MemberRank memberRank;

        public Create(Cause cause, MemberRank memberRank) {
            super(cause, memberRank);
            this.memberRank = memberRank;
        }

        @Override
        public MemberRank getMemberRank() {
            return memberRank;
        }
    }

    public static class Destroy extends TaxableChangeEventImpl.Destroy implements MemberRankChangeEvent.Destroy {
        private MemberRank memberRank;

        public Destroy(Cause cause, MemberRank memberRank) {
            super(cause, memberRank);
            this.memberRank = memberRank;
        }

        @Override
        public MemberRank getMemberRank() {
            return memberRank;
        }
    }

    public static class ChangeParent extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangeParent {
        private MemberRank newParent;

        public ChangeParent(Cause cause, MemberRank memberRank, MemberRank newParent) {
            super(cause, memberRank);
            this.newParent = newParent;
        }

        @Override
        public Optional<MemberRank> getNewParent() {
            return Optional.ofNullable(newParent);
        }
    }

    public static class ChangeTitle extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangeTitle {
        private Text newTitle;

        public ChangeTitle(Cause cause, MemberRank memberRank, Text newTitle) {
            super(cause, memberRank);
            this.newTitle = newTitle;
        }

        @Override
        public Text getNewTitle() {
            return newTitle;
        }
    }

    public static class ChangeDescription extends MemberRankChangeEventImpl implements MemberRankChangeEvent.ChangeDescription {
        private Text newDescription;

        public ChangeDescription(Cause cause, MemberRank memberRank, Text newDescription) {
            super(cause, memberRank);
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

        public ChangePermission(Cause cause, MemberRank memberRank, MemberPermission changedPermission, PermissionState newValue) {
            super(cause, memberRank);
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

        public ChangeClaimPermission(Cause cause, MemberRank memberRank, Claim claim, ClaimPermission changedPermission, PermissionState newValue) {
            super(cause, memberRank);
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
