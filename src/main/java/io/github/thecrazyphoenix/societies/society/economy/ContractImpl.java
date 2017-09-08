package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class ContractImpl implements Contract {     // TODO Sort this out
    private Societies societies;
    private String name;
    private String currency;
    private long interval;
    private BiFunction<AccountHolder, AccountHolder, BigDecimal> amount;

    public ContractImpl(Builder builder) {
        societies = builder.societies;
        name = builder.name;
        currency = builder.currency;
        amount = builder.amount;
        interval = builder.interval;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Currency getCurrency() {
        return societies.getEconomyService().getCurrencies().stream().filter(c -> c.getId().equalsIgnoreCase(currency)).findAny().orElseThrow(() -> new IllegalStateException("no currency with id " + currency));
    }

    @Override
    public BigDecimal getAmount(AccountHolder sender, AccountHolder receiver) {
        return amount.apply(sender, receiver);
    }

    @Override
    public Set<UnorderedPair<AccountHolder, AccountHolder>> getApplicablePairs() {
        return null;
    }

    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public boolean destroy(Cause cause) {
        return false;
    }

    public static class Builder implements Contract.Builder {
        private final Societies societies;
        private String name;
        private String currency;
        private long interval;
        private TimeUnit intervalUnit;
        private BiFunction<AccountHolder, AccountHolder, BigDecimal> amount;

        public Builder(Societies societies) {
            this.societies = societies;
        }

        @Override
        public Contract.Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder currency(Currency currency) {
            return currency(currency.getId());
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        @Override
        public Builder interval(long interval, TimeUnit unit) {
            this.interval = interval;
            intervalUnit = unit;
            return this;
        }

        @Override
        public Builder amount(BiFunction<AccountHolder, AccountHolder, BigDecimal> amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public Optional<ContractImpl> build(Cause cause) {
            CommonMethods.checkNotNullState(name, "name is mandatory");
            CommonMethods.checkNotNullState(currency, "currency is mandatory");
            CommonMethods.checkNotNullState(amount, "amount is mandatory");
            if (interval <= 0L) throw new IllegalStateException("interval is mandatory and must be positive");
            CommonMethods.checkNotNullState(intervalUnit, "interval unit is mandatory");
            ContractImpl contract = new ContractImpl(this);
            // Create event is already on the to-do list.
            return Optional.of(contract);
        }
    }
}
