package io.github.thecrazyphoenix.societies.society;

import com.flowpowered.math.vector.Vector3i;
import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.Cuboid;
import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.event.ClaimChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbsolutePermissionHolder;
import io.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import io.github.thecrazyphoenix.societies.society.economy.VariableContract;
import io.github.thecrazyphoenix.societies.society.internal.SocietyElementImpl;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClaimImpl extends SocietyElementImpl implements Claim {
    private static final String TAX_NAME = "Land Tax";
    private static final long TAX_INTERVAL = TimeUnit.DAYS.toMillis(1L);

    private PermissionHolder<ClaimPermission> defaultPermissions;
    private Map<MemberRank, PermissionHolder<ClaimPermission>> memberRankPermissions;
    private Map<SubSociety, PermissionHolder<ClaimPermission>> subSocietyPermissions;
    private Map<MemberRank, PermissionHolder<ClaimPermission>> viewMemberRankPermissions;
    private Map<SubSociety, PermissionHolder<ClaimPermission>> viewSubSocietyPermissions;

    private Set<Cuboid> cuboids;
    private Set<MemberClaim> memberClaims;
    private Set<Cuboid> viewCuboids;
    private Set<MemberClaim> viewMemberClaims;

    private Map<String, BigDecimal> landTax;
    private Map<String, BigDecimal> landValue;
    private Set<Contract> contracts;
    private Set<Contract> viewContracts;

    private ClaimImpl(Builder builder) {
        super(builder.societies, builder.society);
        defaultPermissions = CommonMethods.mapToHolder(societies, society, PowerlessPermissionHolder.CLAIM, builder.defaultPermissions);
        viewMemberRankPermissions = Collections.unmodifiableMap(memberRankPermissions = new HashMap<>());
        viewSubSocietyPermissions = Collections.unmodifiableMap(subSocietyPermissions = new HashMap<>());
        builder.memberRankPermissions.forEach((r, m) -> memberRankPermissions.put(r, CommonMethods.mapToHolder(societies, society, PowerlessPermissionHolder.CLAIM, m)));
        builder.subSocietyPermissions.forEach((s, m) -> subSocietyPermissions.put(s, CommonMethods.mapToHolder(societies, society, PowerlessPermissionHolder.CLAIM, m)));
        viewCuboids = Collections.unmodifiableSet(cuboids = new HashSet<>());
        viewMemberClaims = Collections.unmodifiableSet(memberClaims = new HashSet<>());
        landTax = new HashMap<>(builder.landTax);
        landValue = new HashMap<>(builder.landValue);
        viewContracts = Collections.unmodifiableSet(contracts = builder.landTax.entrySet().stream().map(e -> new VariableContract(societies, society, TAX_NAME, e.getKey(), TAX_INTERVAL, h -> getLandTax(h, e.getKey()), society.getMembers().values())).collect(Collectors.toSet()));
    }

    @Override
    public Set<Cuboid> getClaimCuboids() {
        return viewCuboids;
    }

    @Override
    public PermissionHolder<ClaimPermission> getDefaultPermissions() {
        return defaultPermissions;
    }

    @Override
    public Optional<PermissionHolder<ClaimPermission>> getPermissions(MemberRank rank) {
        return rank.getParent().isPresent() ? Optional.ofNullable(memberRankPermissions.get(rank)) : Optional.of(AbsolutePermissionHolder.CLAIM);
    }

    @Override
    public void setPermissions(MemberRank rank, PermissionHolder<ClaimPermission> permissions) {
        memberRankPermissions.put(rank, permissions);
    }

    @Override
    public Map<MemberRank, PermissionHolder<ClaimPermission>> getMemberRankPermissions() {
        return viewMemberRankPermissions;
    }

    @Override
    public Optional<PermissionHolder<ClaimPermission>> getPermissions(SubSociety subSociety) {
        return Optional.ofNullable(subSocietyPermissions.get(subSociety));
    }

    @Override
    public void setPermissions(SubSociety subSociety, PermissionHolder<ClaimPermission> permissions) {
        // TODO Add event for this modification and fix all other setPermissions-related events.
        subSocietyPermissions.put(subSociety, permissions);
    }

    @Override
    public Map<SubSociety, PermissionHolder<ClaimPermission>> getSubSocietyPermissions() {
        return viewSubSocietyPermissions;
    }

    @Override
    public Set<MemberClaim> getMemberClaims() {
        return viewMemberClaims;
    }

    @Override
    public Map<Currency, BigDecimal> getLandTax() {
        Map<Currency, BigDecimal> result = landTax.entrySet().stream().collect(Collectors.toMap(e -> societies.getEconomyService().getCurrencies().stream().filter(c -> c.getId().equalsIgnoreCase(e.getKey())).findAny().orElse(null), Map.Entry::getValue));
        result.remove(null);        // Ignore missing currencies
        return result;
    }

    @Override
    public boolean setLandTax(Currency currency, BigDecimal value, Cause cause) {
        if (!societies.queueEvent(new ClaimChangeEventImpl.ChangeLandTax(cause, this, currency, value))) {
            if (BigDecimal.ZERO.equals(value)) {
                contracts.removeIf(c -> c.getCurrency() == currency);
                landTax.remove(currency.getId());
            } else if (landTax.put(currency.getId(), value) == null) {
                contracts.add(new VariableContract(societies, society, TAX_NAME, currency.getId(), TAX_INTERVAL, h -> getLandTax(h, currency.getId()), society.getMembers().values()));
            }
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public Map<Currency, BigDecimal> getLandValue() {
        Map<Currency, BigDecimal> result = landValue.entrySet().stream().collect(Collectors.toMap(e -> societies.getEconomyService().getCurrencies().stream().filter(c -> c.getId().equalsIgnoreCase(e.getKey())).findAny().orElse(null), Map.Entry::getValue));
        result.remove(null);        // Ignore missing currencies
        return result;
    }

    @Override
    public boolean setLandValue(Currency currency, BigDecimal value, Cause cause) {
        if (!societies.queueEvent(new ClaimChangeEventImpl.ChangeLandValue(cause, this, currency, value))) {
            if (BigDecimal.ZERO.equals(value)) {
                landValue.remove(currency.getId());
            } else {
                landValue.put(currency.getId(), value);
            }
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public MemberClaimImpl.Builder memberClaimBuilder() {
        return new MemberClaimImpl.Builder(societies, this);
    }

    @Override
    public CuboidImpl.Builder cuboidBuilder() {
        return new CuboidImpl.Builder(societies, this);
    }

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new ClaimChangeEventImpl.Destroy(cause, this))) {
            society.getClaimsRaw().remove(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean isClaimed(final Vector3i block) {
        return cuboids.stream().anyMatch(c -> c.isClaimed(block));
    }

    @Override
    public long getClaimedVolume() {
        long max = cuboids.stream().mapToLong(Cuboid::getClaimedVolume).sum();
        return max - cuboids.stream().mapToLong(c1 -> cuboids.stream().filter(c2 -> c1 != c2).mapToLong(c1::getIntersectingVolume).sum()).sum() / 2;
    }

    @Override
    public boolean isIntersecting(Cuboid cuboid) {
        return cuboids.stream().anyMatch(cuboid::isIntersecting);
    }

    @Override
    public long getIntersectingVolume(final Cuboid cuboid) {
        long max = cuboids.stream().mapToLong(cuboid::getIntersectingVolume).sum();
        return max - cuboids.stream().mapToLong(c1 -> cuboids.stream().filter(c2 -> c1 != c2).mapToLong(c2 -> getIntersectingVolume(cuboid, c1, c2)).sum()).sum() / 2;
    }

    @Override
    public boolean isContaining(Cuboid cuboid) {
        return cuboid.getClaimedVolume() == getIntersectingVolume(cuboid);
    }

    Set<Cuboid> getClaimCuboidsRaw() {
        return cuboids;
    }

    Set<MemberClaim> getMemberClaimsRaw() {
        return memberClaims;
    }

    private long getIntersectingVolume(Cuboid a, Cuboid b, Cuboid c) {
        Vector3i inter1 = a.getFirstCorner().max(b.getFirstCorner()).max(c.getFirstCorner());
        Vector3i inter2 = a.getSecondCorner().min(b.getSecondCorner()).min(c.getSecondCorner());
        return inter1.min(inter2).equals(inter1) ? CommonMethods.getVolume(inter1, inter2) : 0L;
    }

    @Override
    public Set<Contract> getContracts() {
        return viewContracts;
    }

    private BigDecimal getLandTax(AccountHolder receiver, String currency) {
        return landTax.get(currency).multiply(BigDecimal.valueOf(-memberClaims.stream().filter(c -> c.getOwner().orElse(null) == receiver).mapToLong(MemberClaim::getClaimedVolume).sum()));
    }

    public static class Builder implements Claim.Builder {
        private final Societies societies;
        private final SocietyImpl society;
        private Map<String, BigDecimal> landTax;
        private Map<String, BigDecimal> landValue;
        private Map<ClaimPermission, PermissionState> defaultPermissions;
        private Map<MemberRank, Map<ClaimPermission, PermissionState>> memberRankPermissions;
        private Map<SubSociety, Map<ClaimPermission, PermissionState>> subSocietyPermissions;

        Builder(Societies societies, SocietyImpl society) {
            this.societies = societies;
            this.society = society;
            landTax = new HashMap<>();
            landValue = new HashMap<>();
            defaultPermissions = new HashMap<>();
            memberRankPermissions = new HashMap<>();
            subSocietyPermissions = new HashMap<>();
        }

        @Override
        public Builder landTax(Currency currency, BigDecimal landTax) {
            return landTax(currency.getId(), landTax);
        }

        public Builder landTax(String currency, BigDecimal landTax) {
            this.landTax.put(currency, landTax);
            return this;
        }

        @Override
        public Builder landValue(Currency currency, BigDecimal landValue) {
            return landValue(currency.getId(), landValue);
        }

        public Builder landValue(String currency, BigDecimal landValue) {
            this.landValue.put(currency, landValue);
            return this;
        }

        @Override
        public Claim.Builder defaultPermission(ClaimPermission permission, PermissionState value) {
            defaultPermissions.put(permission, value);
            return this;
        }

        @Override
        public Claim.Builder memberRankPermission(MemberRank rank, ClaimPermission permission, PermissionState value) {
            memberRankPermissions.computeIfAbsent(rank, k -> new HashMap<>()).put(permission, value);
            return this;
        }

        @Override
        public Claim.Builder subSocietyPermission(SubSociety subSociety, ClaimPermission permission, PermissionState value) {
            subSocietyPermissions.computeIfAbsent(subSociety, k -> new HashMap<>()).put(permission, value);
            return this;
        }

        @Override
        public Optional<ClaimImpl> build(Cause cause) {
            ClaimImpl claim = new ClaimImpl(this);
            if (!societies.queueEvent(new ClaimChangeEventImpl.Create(cause, claim))) {
                society.getClaimsRaw().add(claim);
                societies.onSocietyModified();
                return Optional.of(claim);
            }
            return Optional.empty();
        }

        public SocietyImpl getSociety() {
            return society;
        }
    }
}
