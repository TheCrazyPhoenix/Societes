package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.TaxableChangeEvent;
import io.github.thecrazyphoenix.societies.api.society.Taxable;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;

public class TaxableChangeEventImpl extends SocietyElementChangeEventImpl implements TaxableChangeEvent {
    private Taxable taxable;

    public TaxableChangeEventImpl(Cause cause, Taxable taxable) {
        super(cause, taxable);
        this.taxable = taxable;
    }

    @Override
    public Taxable getTaxable() {
        return taxable;
    }

    public static class Create extends TaxableChangeEventImpl implements TaxableChangeEvent.Create {
        public Create(Cause cause, Taxable taxable) {
            super(cause, taxable);
        }
    }

    public static class Destroy extends TaxableChangeEventImpl implements TaxableChangeEvent.Destroy {
        public Destroy(Cause cause, Taxable taxable) {
            super(cause, taxable);
        }
    }

    public static class ChangeFixedTax extends TaxableChangeEventImpl implements TaxableChangeEvent.ChangeFixedTax {
        private BigDecimal newFixedTax;

        public ChangeFixedTax(Cause cause, Taxable taxable, BigDecimal newFixedTax) {
            super(cause, taxable);
            this.newFixedTax = newFixedTax;
        }

        @Override
        public BigDecimal getNewFixedTax() {
            return newFixedTax;
        }
    }

    public static class ChangeSalary extends TaxableChangeEventImpl implements TaxableChangeEvent.ChangeSalary {
        private BigDecimal newSalary;

        public ChangeSalary(Cause cause, Taxable taxable, BigDecimal newSalary) {
            super(cause, taxable);
            this.newSalary = newSalary;
        }

        @Override
        public BigDecimal getNewSalary() {
            return newSalary;
        }
    }
}
