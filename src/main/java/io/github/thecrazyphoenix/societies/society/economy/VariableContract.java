package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.func.UnorderedPairImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VariableContract extends AbstractContract {
    private AccountHolder sender;
    private Function<AccountHolder, BigDecimal> amount;
    private Collection<? extends AccountHolder> applicable;

    public VariableContract(Societies societies, AccountHolder sender, String name, String currency, long interval, Function<AccountHolder, BigDecimal> amount, Collection<? extends AccountHolder> applicable) {
        super(societies, name, currency, interval);
        this.sender = sender;
        this.amount = amount;
        this.applicable = applicable;
    }

    @Override
    public BigDecimal getAmount(AccountHolder sender, AccountHolder receiver) {
        return this.sender == sender ? amount.apply(receiver) : this.sender == receiver ? amount.apply(sender) : BigDecimal.ZERO;
    }

    @Override
    public Set<UnorderedPair<AccountHolder, AccountHolder>> getApplicablePairs() {
        return applicable.stream().<UnorderedPair<AccountHolder, AccountHolder>>map(h -> new UnorderedPairImpl<>(sender, h)).collect(Collectors.toSet());
    }
}
