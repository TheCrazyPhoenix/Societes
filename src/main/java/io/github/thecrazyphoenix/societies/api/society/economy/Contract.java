package io.github.thecrazyphoenix.societies.api.society.economy;

import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Models a money transfer between two AccountHolders at a fixed time interval.
 * Transfers caused by a contract must have a cause where the first object is the contract.
 */
public interface Contract {
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
    Set<? extends UnorderedPair<? extends AccountHolder, ? extends AccountHolder>> getApplicablePairs();
}
