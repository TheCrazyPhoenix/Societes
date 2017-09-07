package io.github.thecrazyphoenix.societies.api.society.economy;

import org.spongepowered.api.service.economy.account.Account;

/**
 * Models an object that can send and receive money.
 */
public interface AccountHolder {
    /**
     * Retrieves this AccountHolder's account.
     * @return The account the salary should be added to and the taxes should be taken from.
     */
    Account getAccount();
}
