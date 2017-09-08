package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.event.SocietyChangeEventImpl;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class SocietyImpl implements Society {
    private Societies societies;
    private UUID worldUUID;

    private String id;
    private Text name;
    private Text abbreviatedName;
    private String accountName;

    private Map<UUID, Member> members;
    private Set<Claim> claims;
    private Map<String, MemberRank> ranks;
    private Map<String, SubSociety> subSocieties;
    private Map<UUID, Member> viewMembers;
    private Set<Claim> viewClaims;
    private Map<String, MemberRank> viewRanks;
    private Map<String, SubSociety> viewSubSocieties;

    private SocietyImpl(Builder builder) {
        societies = builder.societies;
        worldUUID = builder.world;
        accountName = String.format("%s:%s", Societies.PLUGIN_ID, id = CommonMethods.nameToID(name = builder.name));
        abbreviatedName = builder.abbreviatedName;
        viewMembers = Collections.unmodifiableMap(members = new HashMap<>());
        viewClaims = Collections.unmodifiableSet(claims = new HashSet<>());
        viewRanks = Collections.unmodifiableMap(ranks = new HashMap<>());
        viewSubSocieties = Collections.unmodifiableMap(subSocieties = new HashMap<>());
    }

    @Override
    public UUID getWorldUUID() {
        return worldUUID;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public Text getAbbreviatedName() {
        return abbreviatedName;
    }

    @Override
    public Map<UUID, Member> getMembers() {
        return viewMembers;
    }

    @Override
    public Set<Claim> getClaims() {
        return viewClaims;
    }

    @Override
    public Map<String, MemberRank> getRanks() {
        return viewRanks;
    }

    @Override
    public Map<String, SubSociety> getSubSocieties() {
        return viewSubSocieties;
    }

    @Override
    public Account getAccount() {
        return societies.getEconomyService().getOrCreateAccount(accountName).orElseThrow(IllegalStateException::new);
    }

    @Override
    public MemberRankImpl.Builder rankBuilder() {
        return new MemberRankImpl.Builder(societies, this, null);
    }

    @Override
    public ClaimImpl.Builder claimBuilder() {
        return new ClaimImpl.Builder(societies, this);
    }

    @Override
    public SubSocietyImpl.Builder subSocietyBuilder() {
        return new SubSocietyImpl.Builder(societies, this);
    }

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new SocietyChangeEventImpl.Destroy(cause, this))) {
            societies.getSocietiesService().getSocieties(worldUUID).remove(id);
            societies.getSocietiesService().getAllSocieties(worldUUID).remove(id);
            return true;
        }
        return false;
    }

    Map<UUID, Member> getMembersRaw() {
        return members;
    }

    Set<Claim> getClaimsRaw() {
        return claims;
    }

    Map<String, MemberRank> getRanksRaw() {
        return ranks;
    }

    Map<String, SubSociety> getSubSocietiesRaw() {
        return subSocieties;
    }

    @Override
    public Set<Contract> getContracts() {
        return Collections.emptySet();
    }

    public static class Builder implements Society.Builder {
        private final Societies societies;
        private UUID world;
        private Text name;
        private Text abbreviatedName;

        public Builder(Societies societies) {
            this.societies = societies;
        }

        @Override
        public Builder world(UUID world) {
            this.world = world;
            return this;
        }

        @Override
        public Builder name(Text name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder abbreviatedName(Text abbreviatedName) {
            this.abbreviatedName = abbreviatedName;
            return this;
        }

        @Override
        public Optional<SocietyImpl> build(Cause cause) {
            CommonMethods.checkNotNullState(world, "world is mandatory");
            CommonMethods.checkNotNullState(name, "name is mandatory");
            CommonMethods.checkNotNullState(abbreviatedName, "abbreviated name is mandatory");
            SocietyImpl society = new SocietyImpl(this);
            if (!societies.queueEvent(new SocietyChangeEventImpl.Create(cause, society))) {
                societies.getRootSocieties(world).put(society.getIdentifier(), society);
                societies.getAllSocieties(world).put(society.getIdentifier(), society);
                societies.onSocietyModified();
                return Optional.of(society);
            }
            return Optional.empty();
        }
    }
}
