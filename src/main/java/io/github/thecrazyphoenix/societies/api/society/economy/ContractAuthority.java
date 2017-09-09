package io.github.thecrazyphoenix.societies.api.society.economy;

import java.util.Set;

/**
 * Models an object that imposes contracts upon AccountHolders.
 */
public interface ContractAuthority {
    /**
     * Retrieves the active contracts that are enforced by this authority.
     * @return The active contracts as an unmodifiable set.
     */
    Set<? extends Contract> getContracts();
}
