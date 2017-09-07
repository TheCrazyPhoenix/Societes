package io.github.thecrazyphoenix.societies.api.society.economy;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Models a money transfer between two AccountHolders at a fixed time interval.
 */
public interface Contract {     // TODO Add events related to the new contract API.
    /**
     * Retrieves the name of this contract.
     * This exists for informational purposes and is not required to be unique.
     * @return The name.
     */
    String getName();

    /**
     * Retrieves the AccountHolder that sends the amount.
     * @return The sender.
     */
    AccountHolder getSender();

    /**
     * Retrieves the AccountHolder that receives the amount.
     * @return The receiver.
     */
    AccountHolder getReceiver();

    /**
     * Retrieves the currency this contract deals in.
     * @return The currency.
     */
    Currency getCurrency();

    /**
     * Retrieves the amount transferred between the AccountHolders associated by this contract.
     * A negative amount indicates that the sender is receiving money from the receiver.
     * @return The amount as a BigDecimal.
     */
    BigDecimal getAmount();

    /**
     * Retrieves the time interval at which the amount is transferred between the AccountHolders.
     * @return The time interval in milliseconds.
     */
    long getInterval();

    /**
     * Retrieves the condition that determines whether a payment should be made.
     * This condition is checked just before each transfer. If it is false, the transfer is skipped.
     * @return The condition as a BooleanSupplier.
     */
    BooleanSupplier getTransferCondition();

    /**
     * Retrieves the condition that determines whether this contract should continue existing.
     * This condition is checked just before the transfer condition. If it is false, an attempt is made to destroy this contract and, if successful, transfers caused by this contract will no longer take place.
     * @return The condition as a BooleanSupplier.
     */
    BooleanSupplier getExistenceCondition();

    /**
     * Retrieves the callback that is called after each transfer.
     * @return The callback as a Consumer of TransferResult.
     */
    Consumer<TransferResult> getTransferCallback();

    /**
     * Attempts to destroy this object.
     * @param cause The cause of the construction of the object.
     * @return True if the object was destroyed, false if the event was cancelled.
     */
    boolean destroy(Cause cause);

    interface Builder {
        /**
         * Sets the created contract's name.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder name(String name);

        /**
         * Sets the created contract's sender.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder sender(AccountHolder sender);

        /**
         * Sets the created contract's receiver.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder receiver(AccountHolder receiver);

        /**
         * Sets the created contract's currency.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder currency(Currency currency);

        /**
         * Sets the created contract's amount.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder amount(BigDecimal amount);

        /**
         * Sets the created contract's interval.
         * This parameter is mandatory.
         * @param unit The unit the given interval is in.
         * @return This object for chaining.
         */
        Builder interval(long interval, TimeUnit unit);

        /**
         * Sets the created contract's transfer condition.
         * Defaults to {@code () -> true}.
         * @return This object for chaining.
         */
        Builder transferCondition(BooleanSupplier transferCondition);

        /**
         * Sets the created contract's existance condition.
         * Defaults to {@code () -> true}.
         * @return This object for chaining.
         */
        Builder existenceCondition(BooleanSupplier existenceCondition);
        /**
         * Sets the created contract's transfer callback.
         * Defaults to {@code result -> {}}.
         * @return This object for chaining.
         */
        Builder transferCallback(Consumer<TransferResult> transferCallback);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends Contract> build(Cause cause);
    }
}
