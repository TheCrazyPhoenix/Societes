package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ChangeSubSocietyEvent;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import org.spongepowered.api.event.cause.Cause;

public class ChangeSubSocietyEventImpl extends ChangeSocietyElementEventImpl implements ChangeSubSocietyEvent {
    private SubSociety subSociety;

    public ChangeSubSocietyEventImpl(Cause cause, SubSociety subSociety) {
        super(cause, subSociety);
        this.subSociety = subSociety;
    }

    @Override
    public SubSociety getSubSociety() {
        return subSociety;
    }

    @Override
    public AccountHolder getAccountHolder() {
        return subSociety;
    }

    public static class Create extends ChangeSubSocietyEventImpl implements ChangeSubSocietyEvent.Create {
        public Create(Cause cause, SubSociety society) {
            super(cause, society);
        }
    }

    public static class Destroy extends ChangeSubSocietyEventImpl implements ChangeSubSocietyEvent.Destroy {
        public Destroy(Cause cause, SubSociety society) {
            super(cause, society);
        }
    }

    public static class ChangePermission extends ChangeSubSocietyEventImpl implements ChangeSubSocietyEvent.ChangePermission {
        private SocietyPermission changedPermission;
        private PermissionState newValue;

        public ChangePermission(Cause cause, SubSociety subSociety, SocietyPermission changedPermission, PermissionState newValue) {
            super(cause, subSociety);
            this.changedPermission = changedPermission;
            this.newValue = newValue;
        }

        @Override
        public SocietyPermission getChangedPermission() {
            return changedPermission;
        }

        @Override
        public PermissionState getNewValue() {
            return newValue;
        }
    }

    public static class ChangeClaimPermission extends ChangeSubSocietyEventImpl implements ChangeSubSocietyEvent.ChangeClaimPermission {
        private Claim claim;
        private ClaimPermission changedPermission;
        private PermissionState newValue;

        public ChangeClaimPermission(Cause cause, SubSociety subSociety, Claim claim, ClaimPermission changedPermission, PermissionState newValue) {
            super(cause, subSociety);
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
