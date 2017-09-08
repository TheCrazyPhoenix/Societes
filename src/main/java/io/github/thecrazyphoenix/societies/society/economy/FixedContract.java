package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.func.UnorderedPairImpl;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FixedContract extends AbstractContract {
    private AccountHolder sender;
    private BigDecimal amount;
    private Collection<? extends AccountHolder> applicable;

    public FixedContract(Societies societies, AccountHolder sender, String name, String currency, long interval, BigDecimal amount, Collection<? extends AccountHolder> applicable, Function<Cause, Boolean> onDestroy) {
        super(societies, name, currency, interval, onDestroy);
        this.sender = sender;
        this.amount = amount;
        this.applicable = applicable;
    }

    @Override
    public BigDecimal getAmount(AccountHolder sender, AccountHolder receiver) {
        return this.sender == sender && applicable.contains(receiver) ? amount : this.sender == receiver && applicable.contains(sender) ? amount.negate() : BigDecimal.ZERO;
    }

    @Override
    public Set<UnorderedPair<AccountHolder, AccountHolder>> getApplicablePairs() {
        return applicable.stream().<UnorderedPair<AccountHolder, AccountHolder>>map(h -> new UnorderedPairImpl<>(sender, h)).collect(Collectors.toSet());
    }
}
