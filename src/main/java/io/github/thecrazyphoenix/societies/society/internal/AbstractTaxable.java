package io.github.thecrazyphoenix.societies.society.internal;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.society.Taxable;
import io.github.thecrazyphoenix.societies.event.TaxableChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbstractPermissionHolder;
import io.github.thecrazyphoenix.societies.society.SocietyImpl;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;

public abstract class AbstractTaxable<T extends Enum<T>> extends AbstractPermissionHolder<T> implements Taxable {
    protected Taxable parent;
    private BigDecimal fixedTax;
    private BigDecimal salary;

    protected AbstractTaxable(Builder<?, T> builder) {
        super(builder);
        parent = builder.parentTaxable;
    }

    @Override
    public BigDecimal getFixedTax() {
        return fixedTax == null ? parent.getFixedTax() : fixedTax;
    }

    @Override
    public BigDecimal getSalary() {
        return salary == null ? parent.getSalary() : salary;
    }

    @Override
    public boolean setFixedTax(BigDecimal newTax, Cause cause) {
        // TODO Fix null issues (and for setSalary)
        if (!societies.queueEvent(new TaxableChangeEventImpl.ChangeFixedTax(cause, this, newTax))) {
            fixedTax = newTax;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public boolean setSalary(BigDecimal newSalary, Cause cause) {
        if (!societies.queueEvent(new TaxableChangeEventImpl.ChangeSalary(cause, this, newSalary))) {
            salary = newSalary;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T, P extends Enum<P>> extends AbstractPermissionHolder.Builder<T, P> {
        private final Taxable parentTaxable;
        private BigDecimal fixedTax;
        private BigDecimal salary;

        protected Builder(Societies societies, SocietyImpl society, Taxable parentTaxable, PermissionHolder<P> parentPermissions) {
            super(societies, society, parentPermissions);
            this.parentTaxable = parentTaxable;
        }

        public T fixedTax(BigDecimal fixedTax) {
            this.fixedTax = fixedTax;
            return (T) this;
        }

        public T salary(BigDecimal salary) {
            this.salary = salary;
            return (T) this;
        }

        protected void build() {
            fixedTax = CommonMethods.orDefault(fixedTax, BigDecimal.ZERO);
            salary = CommonMethods.orDefault(salary, BigDecimal.ZERO);
        }
    }
}
