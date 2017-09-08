package io.github.thecrazyphoenix.societies.api.society.economy;

import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Models a money transfer between two AccountHolders at a fixed time interval.
 * Transfers caused by a contract must have a cause where the first object is the contract.
 */
public interface Contract {     // TODO Add events related to the new contract API.
    /**
     * Retrieves the name of this contract.
     * This exists for informational purposes and is not required to be unique.
     * @return The name.
     */
    String getName();

    /**
     * Retrieves the currency this contract deals in.
     * @return The currency.
     */
    Currency getCurrency();

    /**
     * Retrieves the time interval at which the amount is transferred between the AccountHolders.
     * @return The time interval in milliseconds.
     */
    long getInterval();

    /**
     * Retrieves the amount transferred between the given AccountHolders.
     * A negative amount indicates that the sender is receiving money from the receiver.
     * {@code getAmount(a, b)} must equal {@code getAmount(b, a).negate()} when called in the same tick.
     * @return The amount as a BigDecimal.
     */
    BigDecimal getAmount(AccountHolder sender, AccountHolder receiver);

    /**
     * Retrieves the pairs of AccountHolders for which {@link #getAmount(AccountHolder, AccountHolder)} does not return {@link BigDecimal#ZERO}
     * @return The pairs as an unmodifiable set of unordered pairs.
     */
    Set<UnorderedPair<AccountHolder, AccountHolder>> getApplicablePairs();

    /**
     * Attempts to destroy this object.
     * @param cause The cause of the construction of the object.
     * @return True if the object was destroyed, false if the event was cancelled.
     */
    boolean destroy(Cause cause);

    interface Builder {     // Currently unused, probably never will be used.
        /**
         * Sets the created contract's name.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder name(String name);

        /**
         * Sets the created contract's currency.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder currency(Currency currency);

        /**
         * Sets the created contract's interval.
         * This parameter is mandatory.
         * @param unit The unit the given interval is in.
         * @return This object for chaining.
         */
        Builder interval(long interval, TimeUnit unit);

        /**
         * Sets the created contract's amount. The first AccountHolder is the sender and the second is the receiver.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder amount(BiFunction<AccountHolder, AccountHolder, BigDecimal> amount);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends Contract> build(Cause cause);
    }
}
