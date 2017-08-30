package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.ClaimChangeEvent;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;

public class ClaimChangeEventImpl extends SocietyChangeEventImpl implements ClaimChangeEvent {
    private Claim claim;

    public ClaimChangeEventImpl(Cause cause, Society society, Claim claim) {
        super(cause, society);
        this.claim = claim;
    }

    @Override
    public Claim getClaim() {
        return claim;
    }

    public static class Create extends ClaimChangeEventImpl implements ClaimChangeEvent.Create {
        public Create(Cause cause, Society society, Claim claim) {
            super(cause, society, claim);
        }
    }

    public static class Destroy extends ClaimChangeEventImpl implements ClaimChangeEvent.Destroy {
        public Destroy(Cause cause, Society society, Claim claim) {
            super(cause, society, claim);
        }
    }

    public static class ChangeVolume extends ClaimChangeEventImpl implements ClaimChangeEvent.ChangeVolume {
        private int newVolume;

        public ChangeVolume(Cause cause, Society society, Claim claim, int newVolume) {
            super(cause, society, claim);
            this.newVolume = newVolume;
        }

        @Override
        public int getNewVolume() {
            return newVolume;
        }
    }

    public static class ChangeLandValue extends ClaimChangeEventImpl implements ClaimChangeEvent.ChangeLandValue {
        private BigDecimal newLandValue;

        public ChangeLandValue(Cause cause, Society society, Claim claim, BigDecimal newLandValue) {
            super(cause, society, claim);
            this.newLandValue = newLandValue;
        }

        @Override
        public BigDecimal newLandValue() {
            return newLandValue;
        }
    }

    public static class ChangeLandTax extends ClaimChangeEventImpl implements ClaimChangeEvent.ChangeLandTax {
        private BigDecimal newLandTax;

        public ChangeLandTax(Cause cause, Society society, Claim claim, BigDecimal newLandTax) {
            super(cause, society, claim);
            this.newLandTax = newLandTax;
        }

        @Override
        public BigDecimal newLandTax() {
            return newLandTax;
        }
    }
}
