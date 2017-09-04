package io.github.thecrazyphoenix.societies.api.society;

/**
 * Models an object owned by a {@link Society} object.
 */
public interface SocietyElement {
    /**
     * Retrieves the society this object belongs to.
     * @return The society.
     */
    Society getSociety();
}
