package com.github.thecrazyphoenix.societies.api;

import java.util.Set;

/**
 * Entry point for the Societies plugin API.
 * An instance of this interface can be obtained through the Sponge ServiceManager if the plugin is present.
 *
 * @see org.spongepowered.api.service.ServiceManager
 */
public interface SocietiesService {
    /**
     * Retrieves the societies that are not owned by other societies.
     * @return The societies as a set.
     */
    Set<Society> getSocieties();

    /**
     * Retrieves all the societies.
     * @return The societies as a set.
     */
    Set<Society> getAllSocieties();
}
