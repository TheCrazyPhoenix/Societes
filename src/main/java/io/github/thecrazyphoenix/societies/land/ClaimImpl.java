package io.github.thecrazyphoenix.societies.land;

import com.flowpowered.math.vector.Vector3i;
import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.land.Claim;
import io.github.thecrazyphoenix.societies.api.land.Cuboid;
import io.github.thecrazyphoenix.societies.api.land.MemberClaim;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.event.ClaimChangeEventImpl;
import io.github.thecrazyphoenix.societies.society.SocietyElementImpl;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ClaimImpl extends SocietyElementImpl implements Claim {
    private PermissionHolder<ClaimPermission> defaultPermissions;
    private Map<MemberRank, PermissionHolder<ClaimPermission>> memberRankPermissions;
    private Map<SubSociety, PermissionHolder<ClaimPermission>> subSocietyPermissions;
    private Map<MemberRank, PermissionHolder<ClaimPermission>> viewMemberRankPermissions;
    private Map<SubSociety, PermissionHolder<ClaimPermission>> viewSubSocietyPermissions;

    private Set<Cuboid> cuboids;
    private Set<MemberClaim> memberClaims;
    private BigDecimal landTax;
    private BigDecimal landValue;

    public ClaimImpl(Societies societies, Society society, PermissionHolder<ClaimPermission> defaultPermissions, Cause cause) {
        super(societies, society);
        this.defaultPermissions = defaultPermissions;
        memberRankPermissions = new HashMap<>();
        subSocietyPermissions = new HashMap<>();
        viewMemberRankPermissions = Collections.unmodifiableMap(memberRankPermissions);
        viewSubSocietyPermissions = Collections.unmodifiableMap(subSocietyPermissions);
        cuboids = new HashSet<>();
        memberClaims = new HashSet<>();
        landTax = BigDecimal.ZERO;
        landValue = BigDecimal.ZERO;
        if (!societies.queueEvent(new ClaimChangeEventImpl.Create(cause, this))) {
            society.getClaims().add(this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    @Override
    public Set<Cuboid> getClaimCuboids() {
        return cuboids;
    }

    @Override
    public PermissionHolder<ClaimPermission> getDefaultPermissions() {
        return defaultPermissions;
    }

    @Override
    public Optional<PermissionHolder<ClaimPermission>> getPermissions(MemberRank rank) {
        return Optional.ofNullable(memberRankPermissions.get(rank));
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
        return memberClaims;
    }

    @Override
    public BigDecimal getLandTax() {
        return landTax;
    }

    @Override
    public boolean setLandTax(BigDecimal value, Cause cause) {
        if (!societies.queueEvent(new ClaimChangeEventImpl.ChangeLandTax(cause, this, value))) {
            landTax = value;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal getLandValue() {
        return landValue;
    }

    @Override
    public boolean setLandValue(BigDecimal value, Cause cause) {
        if (!societies.queueEvent(new ClaimChangeEventImpl.ChangeLandValue(cause, this, value))) {
            landValue = value;
            societies.onSocietyModified();
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

    private long getIntersectingVolume(Cuboid a, Cuboid b, Cuboid c) {
        Vector3i inter1 = a.getFirstCorner().max(b.getFirstCorner()).max(c.getFirstCorner());
        Vector3i inter2 = a.getSecondCorner().min(b.getSecondCorner()).min(c.getSecondCorner());
        return inter1.min(inter2).equals(inter1) ? CommonMethods.getVolume(inter1, inter2) : 0L;
    }
}
