package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.api.event.SubSocietyChangeEvent;
import com.github.thecrazyphoenix.societies.api.society.Taxable;
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
}
