package com.github.thecrazyphoenix.societies;

import com.github.thecrazyphoenix.societies.api.Society;
import org.spongepowered.api.plugin.Plugin;

import java.util.Set;

@Plugin(id = "societies")
public class Societies {
    // TODO Configuration loading
    private Set<Society> societies;
    private Set<Society> allSocieties;

    /**
     * Retrieves the societies that are not owned by other societies.
     * @return The societies as a set.
     */
    public Set<Society> getSocieties() {
        return societies;
    }

    /**
     * Retrieves all the societies.
     * @return The societies as a set.
     */
    public Set<Society> getAllSocieties() {
        return societies;
    }
}
