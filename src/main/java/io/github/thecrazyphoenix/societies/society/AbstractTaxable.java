package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.Taxable;
import io.github.thecrazyphoenix.societies.event.TaxableChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.PermissionHolderImpl;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;

public abstract class AbstractTaxable<T extends Enum<T>> extends PermissionHolderImpl<T> implements Taxable {
    protected Taxable parent;
    private BigDecimal fixedTax;
    private BigDecimal salary;

    public AbstractTaxable(Societies societies, Society society, Taxable parentTaxable, PermissionHolder<T> parentPermissions) {
        super(societies, society, parentPermissions);
        parent = parentTaxable;
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
}
