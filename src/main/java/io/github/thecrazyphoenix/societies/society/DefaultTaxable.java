package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.Taxable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;

import java.math.BigDecimal;

public class DefaultTaxable<T extends Enum<T>> extends SocietyElementImpl implements Taxable {
    public DefaultTaxable(Societies societies, Society society) {
        super(societies, society);
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
    public boolean setFixedTax(BigDecimal newTax, Cause cause) {
        throw new UnsupportedOperationException("unused method call, report this to the developer");
    }

    @Override
    public boolean setSalary(BigDecimal newSalary, Cause cause) {
        throw new UnsupportedOperationException("unused method call, report this to the developer");
    }

    @Override
    public Account getAccount() {
        throw new UnsupportedOperationException("unused method call, report this to the developer");
    }
}
