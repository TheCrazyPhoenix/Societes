package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Models a society.
 */
public interface Society extends AccountHolder, ContractAuthority {
    /**
     * Retrieves the world of this society.
     * @return The UUID of the world this society exists in.
     */
    UUID getWorldUUID();

    /**
     * Retrieves the unique identifier of this society.
     * @return The identifier as a string.
     */
    String getIdentifier();

    /**
     * Retrieves the name of this society.
     * The unformatted value must be unique, non-empty and may only contain alphanumeric characters, spaces and -&#;:.?!
     * @return The name as a Text object.
     */
    Text getName();

    /**
     * Retrieves the abbreviated name of this society.
     * The unformatted value must not be empty and may only contain alphanumeric characters and -&#;:.?!
     * @return The abbreviated name as a Text object.
     */
    Text getAbbreviatedName();

    /**
     * Retrieves the members of this society. This includes the leaders.
     * @return The members as an unmodifiable UUID-indexed map.
     */
    Map<UUID, Member> getMembers();

    /**
     * Retrieves the claims owned by this society.
     * If this society is a sub-society, these claims will be in the claims of the parent society.
     * @return The claims as an unmodifiable set.
     */
    Set<Claim> getClaims();

    /**
     * Retrieves the available ranks of this society.
     * @return The ranks as an unmodifiable string-indexed map.
     */
    Map<String, MemberRank> getRanks();

    /**
     * Retrieves the societies directly under this society's authority. Indirect sub-societies are not included.
     * @return The sub-societies as an unmodifiable string-indexed map.
     */
    Map<String, SubSociety> getSubSocieties();

    /**
     * Creates a new rank builder with this society as the owner.
     * The created {@link MemberRank} will have no parent.
     * @return The created builder.
     * @see MemberRank#rankBuilder()
     */
    MemberRank.Builder rankBuilder();

    /**
     * Creates a new claim builder with this society as the owner.
     * @return The created builder.
     */
    Claim.Builder claimBuilder();

    /**
     * Creates a new sub-society builder with this society as the owner.
     * @return The created builder.
     */
    SubSociety.Builder subSocietyBuilder();

    /**
     * Attempts to destroy this object.
     * @param cause The cause of the construction of the object.
     * @return True if the object was destroyed, false if the event was cancelled.
     */
    boolean destroy(Cause cause);

    /**
     * Enables the construction of a new object.
     */
    interface Builder {
        /**
         * Sets the created society's world UUID.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder world(UUID world);

        /**
         * Sets the created society's name.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder name(Text name);

        /**
         * Sets the created society's abbreviated name.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder abbreviatedName(Text abbreviatedName);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends Society> build(Cause cause);
    }
}