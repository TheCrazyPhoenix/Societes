package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.economy.MutableContract;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MutableFixedContract extends FixedContract implements MutableContract {
    public MutableFixedContract(Societies societies, AccountHolder sender, String name, String currency, long interval, BigDecimal amount, Collection<? extends AccountHolder> applicable) {
        super(societies, sender, name, currency, interval, amount, applicable);
    }

    @Override
    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    @Override
    public boolean setCurrency(Currency currency) {
        this.currency = currency.getId();
        return true;
    }

    @Override
    public boolean setInterval(long interval, TimeUnit timeUnit) {
        this.interval = timeUnit.toMillis(interval);
        return true;
    }

    @Override
    public Set<? extends UnorderedPair<? extends AccountHolder, ? extends AccountHolder>> setAmount(AccountHolder sender, AccountHolder receiver, BigDecimal amount) {
        if (BigDecimal.ZERO.equals(getAmount(sender, receiver))) {
            return Collections.emptySet();
        }
        this.amount = amount;
        return getApplicablePairs();
    }

    @Override
    public boolean destroy() {
        return false;
    }
}
