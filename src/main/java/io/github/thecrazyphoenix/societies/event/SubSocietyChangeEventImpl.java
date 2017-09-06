package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.SubSocietyChangeEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.api.society.Taxable;
import org.spongepowered.api.event.cause.Cause;

public class SubSocietyChangeEventImpl extends TaxableChangeEventImpl implements SubSocietyChangeEvent {
    private SubSociety subSociety;

    public SubSocietyChangeEventImpl(Cause cause, SubSociety subSociety) {
        super(cause, subSociety);
        this.subSociety = subSociety;
    }

    @Override
    public SubSociety getSubSociety() {
        return subSociety;
    }

    public static class Create extends SubSocietyChangeEventImpl implements SubSocietyChangeEvent.Create {
        public Create(Cause cause, SubSociety society) {
            super(cause, society);
        }

        @Override
        public Taxable getTaxable() {
            return getSubSociety();
        }
    }

    public static class Destroy extends SubSocietyChangeEventImpl implements SubSocietyChangeEvent.Destroy {
        public Destroy(Cause cause, SubSociety society) {
            super(cause, society);
        }

        @Override
        public Taxable getTaxable() {
            return getSubSociety();
        }
    }

    public static class ChangePermission extends SubSocietyChangeEventImpl implements SubSocietyChangeEvent.ChangePermission {
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
}
