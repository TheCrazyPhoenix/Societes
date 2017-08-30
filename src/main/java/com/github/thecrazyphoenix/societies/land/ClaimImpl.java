package com.github.thecrazyphoenix.societies.land;

import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.land.MemberClaim;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ClaimImpl extends CuboidClaim implements Claim {
    private PermissionHolder<ClaimPermission> defaultPermissions;
    private Map<MemberRank, PermissionHolder<ClaimPermission>> memberRankPermissions;
    private Map<Society, PermissionHolder<ClaimPermission>> societyPermissions;

    private Set<MemberClaim> memberClaims;
    private BigDecimal landTax;
    private BigDecimal landValue;

    public ClaimImpl(Society society, PermissionHolder<ClaimPermission> defaultPermissions) {
        super(society);
        this.defaultPermissions = defaultPermissions;
        memberRankPermissions = new HashMap<>();
        societyPermissions = new HashMap<>();
        memberClaims = new HashSet<>();
        landTax = BigDecimal.ZERO;
        landValue = BigDecimal.ZERO;
        society.getClaims().add(this);
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
    public Optional<PermissionHolder<ClaimPermission>> getPermissions(Society subSociety) {
        return Optional.ofNullable(societyPermissions.get(subSociety));
    }

    @Override
    public void setPermissions(Society society, PermissionHolder<ClaimPermission> permissions) {
        societyPermissions.put(society, permissions);
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
    public void setLandTax(BigDecimal value) {
        landTax = value;
    }

    @Override
    public BigDecimal getLandValue() {
        return landValue;
    }

    @Override
    public void setLandValue(BigDecimal value) {
        landValue = value;
    }
}
