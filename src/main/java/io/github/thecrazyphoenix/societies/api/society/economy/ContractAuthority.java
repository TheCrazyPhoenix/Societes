package io.github.thecrazyphoenix.societies.api.society.economy;

import java.util.Collection;

/**
 * Models an object that imposes contracts upon AccountHolders.
 */
public interface ContractAuthority {
    /**
     * Retrieves the active contracts that are applied to the given AccountHolder due to this authority.
     * These contracts are separate from the AccountHolder's contracts, therefore the returned contracts may not be a subset of those returned by the AccountHolder.
     * @param accountHolder The AccountHolder to check.
     * @return The active contracts as an unmodifiable collection.
     */
    Collection<Contract> getContracts(AccountHolder accountHolder);
}
