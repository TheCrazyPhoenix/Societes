package io.github.thecrazyphoenix.societies.api;

import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;

import java.util.Map;
import java.util.UUID;

/**
 * Entry point for the Societies plugin API.
 * An instance of this interface can be obtained through the Sponge ServiceManager if the plugin is present.
 * @see org.spongepowered.api.service.ServiceManager
 */
public interface SocietiesService {     // TODO Utility contract builder?
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
     * Adds the given authority to the authorities.
     * The authority's contracts will be automatically handled by this service until the authority is removed.
     * Authorities can only be added once.
     * @param authority The authority to add.
     */
    void addAuthority(ContractAuthority authority);

    /**
     * Removes the given authority from the authorities.
     * The authority's contracts will no longer be automatically handled by this service.
     * @param authority The authority to remove.
     */
    void removeAuthority(ContractAuthority authority);

    /**
     * Creates a new society builder with this service as the owner.
     * @return The created builder.
     */
    Society.Builder societyBuilder();
}
