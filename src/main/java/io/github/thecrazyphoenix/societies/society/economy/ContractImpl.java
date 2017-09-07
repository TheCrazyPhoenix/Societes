package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ContractImpl implements Contract {
    private Societies societies;
    private String name;
    private AccountHolder sender;
    private AccountHolder receiver;
    private Currency currency;
    private BigDecimal amount;
    private long interval;
    private BooleanSupplier transferCondition;
    private BooleanSupplier existenceCondition;
    private Consumer<TransferResult> transferCallback;

    public ContractImpl(Builder builder) {
        societies = builder.societies;
        name = builder.name;
        sender = builder.sender;
        receiver = builder.receiver;
        currency = builder.currency;
        amount = builder.amount;
        interval = builder.interval;
        transferCondition = builder.transferCondition;
        existenceCondition = builder.existenceCondition;
        transferCallback = builder.transferCallback;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AccountHolder getSender() {
        return sender;
    }

    @Override
    public AccountHolder getReceiver() {
        return receiver;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public BooleanSupplier getTransferCondition() {
        return transferCondition;
    }

    @Override
    public BooleanSupplier getExistenceCondition() {
        return existenceCondition;
    }

    @Override
    public Consumer<TransferResult> getTransferCallback() {
        return transferCallback;
    }

    @Override
    public boolean destroy(Cause cause) {
        // Destroy event is already on the to-do list.
        societies.getContracts().remove(this);
        return true;
    }

    public static class Builder implements Contract.Builder {
        private final Societies societies;
        private String name;
        private AccountHolder sender;
        private AccountHolder receiver;
        private Currency currency;
        private BigDecimal amount;
        private long interval;
        private TimeUnit intervalUnit;
        private BooleanSupplier transferCondition;
        private BooleanSupplier existenceCondition;
        private Consumer<TransferResult> transferCallback;

        public Builder(Societies societies) {
            this.societies = societies;
        }

        @Override
        public Contract.Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder sender(AccountHolder sender) {
            this.sender = sender;
            return this;
        }

        @Override
        public Builder receiver(AccountHolder receiver) {
            this.receiver = receiver;
            return this;
        }

        @Override
        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        @Override
        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public Builder interval(long interval, TimeUnit unit) {
            this.interval = interval;
            intervalUnit = unit;
            return this;
        }

        @Override
        public Builder transferCondition(BooleanSupplier transferCondition) {
            this.transferCondition = transferCondition;
            return this;
        }

        @Override
        public Builder existenceCondition(BooleanSupplier existenceCondition) {
            this.existenceCondition = existenceCondition;
            return this;
        }

        @Override
        public Builder transferCallback(Consumer<TransferResult> transferCallback) {
            this.transferCallback = transferCallback;
            return this;
        }

        @Override
        public Optional<ContractImpl> build(Cause cause) {
            CommonMethods.checkNotNullState(name, "name is mandatory");
            CommonMethods.checkNotNullState(sender, "sender is mandatory");
            CommonMethods.checkNotNullState(receiver, "receiver is mandatory");
            CommonMethods.checkNotNullState(currency, "currency is mandatory");
            CommonMethods.checkNotNullState(amount, "amount is mandatory");
            if (interval <= 0L) throw new IllegalStateException("interval is mandatory and must be positive");
            CommonMethods.checkNotNullState(intervalUnit, "interval unit is mandatory");
            transferCondition = CommonMethods.orDefault(transferCondition, () -> true);
            existenceCondition = CommonMethods.orDefault(existenceCondition, () -> true);
            transferCallback = CommonMethods.orDefault(transferCallback, result -> {});
            ContractImpl contract = new ContractImpl(this);
            // Create event is already on the to-do list.
            return Optional.of(contract);
        }
    }
}
