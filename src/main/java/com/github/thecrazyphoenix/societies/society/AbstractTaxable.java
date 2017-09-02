package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.Taxable;
import com.github.thecrazyphoenix.societies.event.TaxableChangeEventImpl;
import com.github.thecrazyphoenix.societies.permission.PermissionHolderImpl;
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
        if (!societies.queueEvent(new TaxableChangeEventImpl.ChangeFixedTax(cause, society, this, newTax))) {
            fixedTax = newTax;
            return true;
        }
        return false;
    }

    @Override
    public boolean setSalary(BigDecimal newSalary, Cause cause) {
        if (!societies.queueEvent(new TaxableChangeEventImpl.ChangeSalary(cause, society, this, newSalary))) {
            salary = newSalary;
            return true;
        }
        return false;
    }
}
