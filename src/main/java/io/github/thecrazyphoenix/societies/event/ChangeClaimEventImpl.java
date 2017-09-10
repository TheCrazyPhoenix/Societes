package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ChangeClaimEvent;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;

public class ChangeClaimEventImpl extends ChangeSocietyElementEventImpl implements ChangeClaimEvent {
    private Claim claim;

    public ChangeClaimEventImpl(Cause cause, Claim claim) {
        super(cause, claim);
        this.claim = claim;
    }

    @Override
    public Claim getClaim() {
        return claim;
    }

    public static class Create extends ChangeClaimEventImpl implements ChangeClaimEvent.Create {
        public Create(Cause cause, Claim claim) {
            super(cause, claim);
        }
    }

    public static class Destroy extends ChangeClaimEventImpl implements ChangeClaimEvent.Destroy {
        public Destroy(Cause cause, Claim claim) {
            super(cause, claim);
        }
    }

    public static class ChangeVolume extends ChangeClaimEventImpl implements ChangeClaimEvent.ChangeVolume {
        private int newVolume;

        public ChangeVolume(Cause cause, Claim claim, int newVolume) {
            super(cause, claim);
            this.newVolume = newVolume;
        }

        @Override
        public int getNewVolume() {
            return newVolume;
        }
    }

    public static class ChangeLandValue extends ChangeClaimEventImpl implements ChangeClaimEvent.ChangeLandValue {
        private Currency currency;
        private BigDecimal newLandValue;

        public ChangeLandValue(Cause cause, Claim claim, Currency currency, BigDecimal newLandValue) {
            super(cause, claim);
            this.currency = currency;
            this.newLandValue = newLandValue;
        }

        @Override
        public Currency getCurrency() {
            return currency;
        }

        @Override
        public BigDecimal getNewLandValue() {
            return newLandValue;
        }
    }

    public static class ChangeLandTax extends ChangeClaimEventImpl implements ChangeClaimEvent.ChangeLandTax {
        private Currency currency;
        private BigDecimal newLandTax;

        public ChangeLandTax(Cause cause, Claim claim, Currency currency, BigDecimal newLandTax) {
            super(cause, claim);
            this.currency = currency;
            this.newLandTax = newLandTax;
        }

        @Override
        public Currency getCurrency() {
            return currency;
        }

        @Override
        public BigDecimal getNewLandTax() {
            return newLandTax;
        }
    }
}
