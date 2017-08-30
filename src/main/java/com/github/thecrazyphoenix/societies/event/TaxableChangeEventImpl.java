package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.TaxableChangeEvent;
import com.github.thecrazyphoenix.societies.api.society.Taxable;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;

public class TaxableChangeEventImpl extends SocietyChangeEventImpl implements TaxableChangeEvent {
    private Taxable taxable;

    public TaxableChangeEventImpl(Cause cause, Society society, Taxable taxable) {
        super(cause, society);
        this.taxable = taxable;
    }

    @Override
    public Taxable getTaxable() {
        return taxable;
    }

    public static class Create extends TaxableChangeEventImpl implements TaxableChangeEvent.Create {
        public Create(Cause cause, Society society, Taxable taxable) {
            super(cause, society, taxable);
        }
    }

    public static class Destroy extends TaxableChangeEventImpl implements TaxableChangeEvent.Destroy {
        public Destroy(Cause cause, Society society, Taxable taxable) {
            super(cause, society, taxable);
        }
    }

    public static class ChangeFixedTax extends TaxableChangeEventImpl implements TaxableChangeEvent.ChangeFixedTax {
        private BigDecimal newFixedTax;

        public ChangeFixedTax(Cause cause, Society society, Taxable taxable, BigDecimal newFixedTax) {
            super(cause, society, taxable);
            this.newFixedTax = newFixedTax;
        }

        @Override
        public BigDecimal getNewFixedTax() {
            return null;
        }
    }

    public static class ChangeSalary extends TaxableChangeEventImpl implements TaxableChangeEvent.ChangeSalary {
        private BigDecimal newSalary;

        public ChangeSalary(Cause cause, Society society, Taxable taxable, BigDecimal newSalary) {
            super(cause, society, taxable);
            this.newSalary = newSalary;
        }

        @Override
        public BigDecimal getNewSalary() {
            return newSalary;
        }
    }
}
