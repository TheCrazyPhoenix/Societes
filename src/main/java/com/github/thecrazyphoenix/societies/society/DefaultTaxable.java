package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.Taxable;
import org.spongepowered.api.service.economy.account.Account;

import java.math.BigDecimal;

public class DefaultTaxable<T extends Enum<T>> extends SocietyElementImpl implements Taxable {
    public DefaultTaxable(Society society) {
        super(society);
    }

    @Override
    public BigDecimal getFixedTax() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getSalary() {
        return BigDecimal.ZERO;
    }

    @Override
    public void setFixedTax(BigDecimal newTax) {
        throw new UnsupportedOperationException("unused method call, report this to the developer");
    }

    @Override
    public void setSalary(BigDecimal newSalary) {
        throw new UnsupportedOperationException("unused method call, report this to the developer");
    }

    @Override
    public Account getAccount() {
        throw new UnsupportedOperationException("unused method call, report this to the developer");
    }
}
