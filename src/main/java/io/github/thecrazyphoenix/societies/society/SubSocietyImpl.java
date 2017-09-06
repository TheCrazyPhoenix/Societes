package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.event.SubSocietyChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import io.github.thecrazyphoenix.societies.society.internal.AbstractTaxable;
import io.github.thecrazyphoenix.societies.society.internal.DefaultTaxable;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;

import java.util.Optional;

public class SubSocietyImpl extends AbstractTaxable<SocietyPermission> implements SubSociety {
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
        if (!societies.queueEvent(new SubSocietyChangeEventImpl.Destroy(cause, this))) {
            society.getSubSocietiesRaw().remove(subSociety.getIdentifier());
            return true;
        }
        return false;
    }

    @Override
    protected PermissionChangeEvent createEvent(SocietyPermission permission, PermissionState newState, Cause cause) {
        return new SubSocietyChangeEventImpl.ChangePermission(cause, this, permission, newState);
    }

    public static class Builder extends AbstractTaxable.Builder<Builder, SocietyPermission> implements SubSociety.Builder {
        private final Societies societies;
        private final SocietyImpl society;
        private Society subSociety;

        Builder(Societies societies, SocietyImpl society) {
            super(societies, society, new DefaultTaxable<>(societies, society), PowerlessPermissionHolder.SOCIETY);
            this.societies = societies;
            this.society = society;
        }

        @Override
        public Builder subSociety(Society subSociety) {
            this.subSociety = subSociety;
            return this;
        }

        @Override
        public Optional<SubSocietyImpl> build(Cause cause) {        // TODO Check if society is in societies and remove it from that map if event passes.
            CommonMethods.checkNotNullState(subSociety, "sub-society is mandatory");
            super.build();
            SubSocietyImpl subSociety = new SubSocietyImpl(this);
            if (!societies.queueEvent(new SubSocietyChangeEventImpl.Create(cause, subSociety))) {
                society.getSubSocietiesRaw().put(this.subSociety.getIdentifier(), subSociety);
                societies.onSocietyModified();
                return Optional.of(subSociety);
            }
            return Optional.empty();
        }
    }
}
