package io.github.thecrazyphoenix.societies.api.society.economy;

import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Models a Contract that can be modified by external sources.
 */
public interface MutableContract extends Contract {
    /**
     * Attempts to set the name of this Contract.
     * @param name The name to set it to.
     * @return True if it succeeded, false otherwise.
     */
    boolean setName(String name);

    /**
     * Attempts to set the currency of this Contract.
     * @param currency The currency to set it to.
     * @return True if it succeeded, false otherwise.
     */
    boolean setCurrency(Currency currency);

    /**
     * Attempts to set the time interval between which transfers are made.
     * @param interval The interval to set it to.
     * @param timeUnit The unit of the interval.
     * @return True if it succeeded, false otherwise.
     */
    boolean setInterval(long interval, TimeUnit timeUnit);

    /**
     * Attempts to set the amount transferred between the given AccountHolders.
     * This may modify the amount transferred between other AccountHolders.
     * The exact behaviour depends on the contract of the interfaces offering access to a MutableContract.
     * @param sender The AccountHolder that should send the given amount.
     * @param receiver The AccountHolder that should receive the given amount.
     * @param amount The amount to set the transfer to.
     * @return The set of all unordered pairs that were modified if successful, otherwise an empty set.
     */
    Set<? extends UnorderedPair<? extends AccountHolder, ? extends AccountHolder>> setAmount(AccountHolder sender, AccountHolder receiver, BigDecimal amount);

    /**
     * Attempts to destroy this Contract and prepare it for garbage collection.
     * The exact behaviour depends on the contract of the interfaces offering access to a MutableContract.
     * @return True if it succeeded, false otherwise.
     */
    boolean destroy();
}
