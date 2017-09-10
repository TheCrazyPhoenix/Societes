package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.ChangePermissionEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.event.ChangeSubSocietyEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbstractPermissionHolder;
import io.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;

import java.util.Optional;

public class SubSocietyImpl extends AbstractPermissionHolder<SocietyPermission> implements SubSociety {
    private Society subSociety;

    private SubSocietyImpl(Builder builder) {
        super(builder);
        subSociety = builder.subSociety;
    }

    @Override
    public Society toSociety() {
        return subSociety;
    }

    @Override
    public Account getAccount() {
        return subSociety.getAccount();
    }

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new ChangeSubSocietyEventImpl.Destroy(cause, this))) {
            society.getSubSocietiesRaw().remove(subSociety.getIdentifier());
            return true;
        }
        return false;
    }

    @Override
    protected ChangePermissionEvent createEvent(SocietyPermission permission, PermissionState newState, Cause cause) {
        return new ChangeSubSocietyEventImpl.ChangePermission(cause, this, permission, newState);
    }

    public static class Builder extends AbstractPermissionHolder.Builder<Builder, SocietyPermission> implements SubSociety.Builder {
        private final Societies societies;
        private final SocietyImpl society;
        private Society subSociety;

        Builder(Societies societies, SocietyImpl society) {
            super(societies, society, PowerlessPermissionHolder.SOCIETY);
            this.societies = societies;
            this.society = society;
        }

        @Override
        public Builder subSociety(Society subSociety) {
            this.subSociety = subSociety;
            return this;
        }

        @Override
        public Optional<SubSocietyImpl> build(Cause cause) {
            CommonMethods.checkNotNullState(subSociety, "sub-society is mandatory");
            if (!society.getWorldUUID().equals(subSociety.getWorldUUID())) {
                throw new IllegalStateException("sub-society must be in the same world");
            } else if (!societies.getRootSocieties(subSociety.getWorldUUID()).containsKey(subSociety.getIdentifier())) {
                throw new IllegalStateException("sub-society cannot have two parent societies");
            } else if (subSociety == society) {
                throw new IllegalStateException("sub-society cannot have itself as a parent");
            }
            SubSocietyImpl subSociety = new SubSocietyImpl(this);
            if (!societies.queueEvent(new ChangeSubSocietyEventImpl.Create(cause, subSociety))) {
                society.getSubSocietiesRaw().put(this.subSociety.getIdentifier(), subSociety);
                societies.getRootSocieties(this.subSociety.getWorldUUID()).remove(this.subSociety.getIdentifier());
                societies.onSocietyModified();
                return Optional.of(subSociety);
            }
            return Optional.empty();
        }
    }
}
