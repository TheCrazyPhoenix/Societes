package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ClaimChangeEvent;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;

public class ClaimChangeEventImpl extends SocietyElementChangeEventImpl implements ClaimChangeEvent {
    private Claim claim;

    public ClaimChangeEventImpl(Cause cause, Claim claim) {
        super(cause, claim);
        this.claim = claim;
    }

    @Override
    public Claim getClaim() {
        return claim;
    }

    public static class Create extends ClaimChangeEventImpl implements ClaimChangeEvent.Create {
        public Create(Cause cause, Claim claim) {
            super(cause, claim);
        }
    }

    public static class Destroy extends ClaimChangeEventImpl implements ClaimChangeEvent.Destroy {
        public Destroy(Cause cause, Claim claim) {
            super(cause, claim);
        }
    }

    public static class ChangeVolume extends ClaimChangeEventImpl implements ClaimChangeEvent.ChangeVolume {
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

    public static class ChangeLandValue extends ClaimChangeEventImpl implements ClaimChangeEvent.ChangeLandValue {
        private BigDecimal newLandValue;

        public ChangeLandValue(Cause cause, Claim claim, BigDecimal newLandValue) {
            super(cause, claim);
            this.newLandValue = newLandValue;
        }

        @Override
        public BigDecimal newLandValue() {
            return newLandValue;
        }
    }

    public static class ChangeLandTax extends ClaimChangeEventImpl implements ClaimChangeEvent.ChangeLandTax {
        private BigDecimal newLandTax;

        public ChangeLandTax(Cause cause, Claim claim, BigDecimal newLandTax) {
            super(cause, claim);
            this.newLandTax = newLandTax;
        }

        @Override
        public BigDecimal newLandTax() {
            return newLandTax;
        }
    }
}
