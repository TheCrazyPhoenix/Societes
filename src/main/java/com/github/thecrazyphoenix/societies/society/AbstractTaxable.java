package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.Taxable;
import com.github.thecrazyphoenix.societies.permission.PermissionHolderImpl;

import java.math.BigDecimal;

public abstract class AbstractTaxable<T extends Enum<T>> extends PermissionHolderImpl<T> implements Taxable {
    protected Taxable parent;
    private BigDecimal fixedTax;
    private BigDecimal salary;

    public AbstractTaxable(Society society, Taxable parentTaxable, PermissionHolder<T> parentPermissions) {
        super(society, parentPermissions);
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
    public void setFixedTax(BigDecimal newTax) {
        fixedTax = newTax;
    }

    @Override
    public void setSalary(BigDecimal newSalary) {
        salary = newSalary;
    }
}
