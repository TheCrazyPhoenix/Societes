package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ChangeMemberRankEvent;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

public class ChangeMemberRankEventImpl extends ChangeSocietyElementEventImpl implements ChangeMemberRankEvent {
    private MemberRank memberRank;

    public ChangeMemberRankEventImpl(Cause cause, MemberRank memberRank) {
        super(cause, memberRank);
        this.memberRank = memberRank;
    }

    @Override
    public MemberRank getMemberRank() {
        return memberRank;
    }

    public static class Create extends ChangeMemberRankEventImpl implements ChangeMemberRankEvent.Create {
        public Create(Cause cause, MemberRank memberRank) {
            super(cause, memberRank);
        }
    }

    public static class Destroy extends ChangeMemberRankEventImpl implements ChangeMemberRankEvent.Destroy {
        public Destroy(Cause cause, MemberRank memberRank) {
            super(cause, memberRank);
        }
    }

    public static class ChangeTitle extends ChangeMemberRankEventImpl implements ChangeMemberRankEvent.ChangeTitle {
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

    public static class ChangeDescription extends ChangeMemberRankEventImpl implements ChangeMemberRankEvent.ChangeDescription {
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

    public static class ChangePermission extends ChangeMemberRankEventImpl implements ChangeMemberRankEvent.ChangePermission {
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

    public static class ChangeClaimPermission extends ChangeMemberRankEventImpl implements ChangeMemberRankEvent.ChangeClaimPermission {
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
