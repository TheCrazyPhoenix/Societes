package io.github.thecrazyphoenix.societies.api;

import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entry point for the Societies plugin API.
 * An instance of this interface can be obtained through the Sponge ServiceManager if the plugin is present.
 *
 * @see org.spongepowered.api.service.ServiceManager
 */
public interface SocietiesService {
    /**
     * Retrieves the societies in a given world that are not owned by other societies.
     * @param worldUUID The UUID of the world whose societies to retrieve.
     * @return The societies as an unmodifiable string-indexed map.
     */
    Map<String, Society> getSocieties(UUID worldUUID);

    /**
     * Retrieves all the societies in a given world.
     * @param worldUUID The UUID of the world whose societies to retrieve.
     * @return The societies as an unmodifiable string-indexed map.
     */
    Map<String, Society> getAllSocieties(UUID worldUUID);

    /**
     * Retrieves the contracts not imposed by a {@link ContractAuthority}
     * @return The contracts as a unmodifiable collection.
     */
    Collection<Contract> getContracts();

    /**
     * Retrieves the contracts not imposed by a {@link ContractAuthority} where the sender or holder is the given AccountHolder.
     * @param accountHolder The AccountHolder to check.
     * @return The contracts as an unmodifiable collection.
     */
    default Collection<Contract> getContracts(AccountHolder accountHolder) {
        return getContracts().stream().filter(c -> c.getSender() == accountHolder || c.getReceiver() == accountHolder).collect(Collectors.toList());
    }

    /**
     * Retrieves the authorities that impose contracts.
     * @return The authorities as an unmodifiable set.
     */
    Set<ContractAuthority> getAuthorities();

    /**
     * Adds the given authority to the authorities.
     * The authority's contracts will be automatically handled by this service until the authority is removed.
     * Authorities can only be added once.
     * @param authority The authority to add.
     */
    void addAuthority(ContractAuthority authority);

    /**
     * Removes the given authority to the authorities.
     * The authority's contracts will no longer be automatically handled by this service.
     * @param authority The authority to remove.
     */
    void removeAuthority(ContractAuthority authority);

    /**
     * Creates a new society builder with this service as the owner.
     * @return The created builder.
     */
    Society.Builder societyBuilder();

    /**
     * Creates a new contract builder with this service as the owner.
     * @return The created builder.
     */
    Contract.Builder contractBuilder();
}
