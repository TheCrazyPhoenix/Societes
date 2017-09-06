package io.github.thecrazyphoenix.societies.api;

import io.github.thecrazyphoenix.societies.api.society.Society;

import java.util.Map;
import java.util.UUID;

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
     * @return The societies as a set.
     */
    Map<String, Society> getSocieties(UUID worldUUID);

    /**
     * Retrieves all the societies in a given world.
     * @param worldUUID The UUID of the world whose societies to retrieve.
     * @return The societies as a set.
     */
    Map<String, Society> getAllSocieties(UUID worldUUID);

    /**
     * Creates a new society builder with this service as the owner.
     * @return The created builder.
     */
    Society.Builder societyBuilder();
}
