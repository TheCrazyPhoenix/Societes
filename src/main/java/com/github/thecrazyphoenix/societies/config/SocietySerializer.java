package com.github.thecrazyphoenix.societies.config;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.land.Cuboid;
import com.github.thecrazyphoenix.societies.api.land.MemberClaim;
import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.api.society.Taxable;
import com.github.thecrazyphoenix.societies.land.ClaimImpl;
import com.github.thecrazyphoenix.societies.land.CuboidImpl;
import com.github.thecrazyphoenix.societies.land.MemberClaimImpl;
import com.github.thecrazyphoenix.societies.permission.PermissionHolderImpl;
import com.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import com.github.thecrazyphoenix.societies.society.MemberImpl;
import com.github.thecrazyphoenix.societies.society.MemberRankImpl;
import com.github.thecrazyphoenix.societies.society.SocietyImpl;
import com.github.thecrazyphoenix.societies.society.SubSocietyImpl;
import com.github.thecrazyphoenix.societies.util.CommonMethods;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class SocietySerializer {
    private static final String NAME_KEY = "name";
    private static final String ABBREVIATED_NAME_KEY = "abbreviated-name";
    private static final String RANKS_KEY = "ranks";
    private static final String MEMBERS_KEY = "members";
    private static final String SUB_SOCIETIES_KEY = "sub-societies";
    private static final String CLAIMS_KEY = "claims";

    private static final String ID_KEY = "id";
    private static final String PARENT_KEY = "parent";
    private static final String TITLE_KEY = "title";
    private static final String DESCRIPTION_KEY = "description";

    private static final String UUID_KEY = "uuid";
    private static final String RANK_KEY = "rank";
    private static final String LEADER_KEY = "leader";

    private static final String FIXED_TAX_KEY = "fixed-tax";
    private static final String SALARY_KEY = "salary";
    private static final String PERMISSIONS_KEY = "permissions";

    private static final String CUBOIDS_KEY = "cuboids";
    private static final String DEFAULT_PERMISSIONS_KEY = "default-permissions";
    private static final String MEMBER_PERMISSIONS_KEY = "member-permissions";
    private static final String SOCIETY_PERMISSIONS_KEY = "society-permissions";
    private static final String LAND_TAX_KEY = "land-tax";
    private static final String LAND_VALUE_KEY = "land-value";
    private static final String MEMBER_CLAIMS_KEY = "member-claims";
    private static final String OWNER_KEY = "owner";

    private static final String CORNER1_KEY = "c1";
    private static final String CORNER2_KEY = "c2";

    private static final TypeToken<Text> TEXT_TOKEN = TypeToken.of(Text.class);
    private static final TypeToken<UUID> UUID_TOKEN = TypeToken.of(UUID.class);
    private static final TypeToken<BigDecimal> BIG_DECIMAL_TOKEN = TypeToken.of(BigDecimal.class);
    private static final TypeToken<Vector3i> VECTOR3I_TOKEN = TypeToken.of(Vector3i.class);

    private Logger logger;
    private Societies societies;

    public SocietySerializer(Logger logger, Societies societies) {
        this.logger = logger;
        this.societies = societies;
    }

    public void serializeSocieties(final ConfigurationNode rootNode, Map<String, Society> societies, Map<String, Society> allSocieties) {
        allSocieties.values().forEach(society -> serializeSociety(society, rootNode.getAppendedNode()));
    }

    public void deserializeSocieties(ConfigurationNode rootNode, Map<String, Society> societies, final Map<String, Society> allSocieties) {
        rootNode.getChildrenList().stream().map(this::deserializeSociety).forEach(s -> allSocieties.put(s.getIdentifier(), s));
        societies.putAll(allSocieties);
        allSocieties.values().stream().flatMap(s -> s.getSubSocieties().values().stream()).map(SubSociety::toSociety).map(Society::getIdentifier).forEach(societies::remove);
        SubSocietyImpl.updateAll(allSocieties);
    }

    private void serializeSociety(Society society, ConfigurationNode node) {
        try {
            node.getNode(NAME_KEY).setValue(TEXT_TOKEN, society.getName());
            node.getNode(ABBREVIATED_NAME_KEY).setValue(TEXT_TOKEN, society.getAbbreviatedName());
            society.getRanks().forEach((key, rank) -> serializeRank(rank, node.getNode(RANKS_KEY, key)));
            society.getMembers().values().forEach(member -> serializeMember(member, node.getNode(MEMBERS_KEY).getAppendedNode()));
            society.getSubSocieties().values().forEach(ss -> serializeSubSociety(ss, node.getNode(SUB_SOCIETIES_KEY).getAppendedNode()));
            society.getClaims().forEach(claim -> serializeClaim(claim, node.getNode(CLAIMS_KEY).getAppendedNode()));
        } catch (ObjectMappingException e) {
            logger.warn("Failed to serialize society", e);
        }
    }

    private Society deserializeSociety(final ConfigurationNode node) {
        try {
            final Society society = new SocietyImpl(societies, node.getNode(NAME_KEY).getValue(TEXT_TOKEN), node.getNode(ABBREVIATED_NAME_KEY).getValue(TEXT_TOKEN), null);
            node.getNode(RANKS_KEY).getChildrenList().forEach(n -> deserializeRank(society, n));
            society.getRanks().values().forEach(rank -> rank.setParent(society.getRanks().get(node.getNode(RANKS_KEY, PARENT_KEY).getString()), null));
            node.getNode(MEMBERS_KEY).getChildrenList().forEach(n -> deserializeMember(society, n));
            node.getNode(SUB_SOCIETIES_KEY).getChildrenList().forEach(n -> deserializeSubSociety(society, n));
            node.getNode(CLAIMS_KEY).getChildrenList().forEach(n -> deserializeClaim(society, n));
            return society;
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize society", e);
        }
        return null;
    }

    private void serializeClaim(Claim claim, ConfigurationNode node) {
        try {
            for (Cuboid cuboid : claim.getClaimCuboids()) {
                serializeCuboid(cuboid, node.getNode(CUBOIDS_KEY).getAppendedNode());
            }
            node.getNode(LAND_TAX_KEY).setValue(BIG_DECIMAL_TOKEN, claim.getLandTax());
            node.getNode(LAND_VALUE_KEY).setValue(BIG_DECIMAL_TOKEN, claim.getLandValue());
            serializePermissions(claim.getDefaultPermissions(), node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission.values());
            for (Map.Entry<MemberRank, PermissionHolder<ClaimPermission>> entry : claim.getMemberRankPermissions().entrySet()) {
                serializePermissions(entry.getValue(), node.getNode(MEMBER_PERMISSIONS_KEY, entry.getKey().getIdentifier()), ClaimPermission.values());
            }
            for (Map.Entry<SubSociety, PermissionHolder<ClaimPermission>> entry : claim.getSubSocietyPermissions().entrySet()) {
                serializePermissions(entry.getValue(), node.getNode(SOCIETY_PERMISSIONS_KEY, entry.getKey().toSociety().getIdentifier()), ClaimPermission.values());
            }
            for (MemberClaim memberClaim : claim.getMemberClaims()) {
                serializeMemberClaim(memberClaim, node.getNode(MEMBER_CLAIMS_KEY).getAppendedNode());
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to serialize claim", e);
        }
    }

    private void deserializeClaim(Society society, ConfigurationNode node) {
        Claim claim = new ClaimImpl(societies, society, new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM), null);
        try {
            for (ConfigurationNode child : node.getNode(CUBOIDS_KEY).getChildrenList()) {
                claim.getClaimCuboids().add(deserializeCuboid(society, child));
            }
            claim.setLandTax(node.getNode(LAND_TAX_KEY).getValue(BIG_DECIMAL_TOKEN), null);
            claim.setLandValue(node.getNode(LAND_VALUE_KEY).getValue(BIG_DECIMAL_TOKEN), null);
            deserializePermissions(claim.getDefaultPermissions(), node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission::valueOf);
            PermissionHolder<ClaimPermission> buffer;
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(MEMBER_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
                //noinspection SuspiciousMethodCalls
                claim.setPermissions(society.getRanks().get(entry.getKey()), buffer = new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM));
                deserializePermissions(buffer, entry.getValue(), ClaimPermission::valueOf);
            }
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(SOCIETY_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
                //noinspection SuspiciousMethodCalls
                claim.setPermissions(society.getSubSocieties().get(entry.getKey()), buffer = new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM));
                deserializePermissions(buffer, entry.getValue(), ClaimPermission::valueOf);
            }
            for (ConfigurationNode n : node.getNode(MEMBER_CLAIMS_KEY).getChildrenList()) {
                deserializeMemberClaim(claim, n);
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize claim", e);
            society.getClaims().remove(claim);
        }
    }

    private void serializeMemberClaim(MemberClaim memberClaim, final ConfigurationNode node) throws ObjectMappingException {
        Optional<Member> owner = memberClaim.getOwner();
        if (owner.isPresent()) {
            node.getNode(OWNER_KEY).setValue(UUID_TOKEN, owner.get().getUser().getUniqueId());
        }
        serializeCuboid(memberClaim, node);
        serializePermissions(memberClaim.getDefaultPermissions(), node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission.values());
        for (Map.Entry<Member, PermissionHolder<ClaimPermission>> entry : memberClaim.getMemberPermissions().entrySet()) {
            serializePermissions(entry.getValue(), node.getNode(MEMBER_PERMISSIONS_KEY, entry.getKey().getUser().getUniqueId()), ClaimPermission.values());
        }
    }

    private void deserializeMemberClaim(Claim parent, ConfigurationNode node) throws ObjectMappingException {
        Society society = parent.getSociety();
        PermissionHolder<ClaimPermission> defaultPermissions = new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM);
        MemberClaim memberClaim = new MemberClaimImpl(societies, parent, society.getMembers().get(node.getNode(OWNER_KEY).getValue(UUID_TOKEN)), defaultPermissions, node.getNode(CORNER1_KEY).getValue(VECTOR3I_TOKEN), node.getNode(CORNER2_KEY).getValue(VECTOR3I_TOKEN), null);
        deserializePermissions(defaultPermissions, node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission::valueOf);
        PermissionHolder<ClaimPermission> buffer;
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(MEMBER_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
            //noinspection SuspiciousMethodCalls
            memberClaim.setPermissions(society.getMembers().get(entry.getKey()), buffer = new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM));
            deserializePermissions(buffer, entry.getValue(), ClaimPermission::valueOf);
        }
    }

    private void serializeCuboid(Cuboid cuboid, ConfigurationNode node) throws ObjectMappingException {
        node.getNode(CORNER1_KEY).setValue(VECTOR3I_TOKEN, cuboid.getFirstCorner());
        node.getNode(CORNER2_KEY).setValue(VECTOR3I_TOKEN, cuboid.getSecondCorner());
    }

    private Cuboid deserializeCuboid(Society society, ConfigurationNode node) throws ObjectMappingException {
        return new CuboidImpl(societies, society, node.getNode(CORNER1_KEY).getValue(VECTOR3I_TOKEN), node.getNode(CORNER2_KEY).getValue(VECTOR3I_TOKEN));
    }

    private void serializeSubSociety(SubSociety subSociety, ConfigurationNode node) {
        try {
            node.getNode(ID_KEY).setValue(subSociety.toSociety().getIdentifier());
            serializeTaxable(subSociety, node);
            serializePermissions(subSociety, node.getNode("permissions"), SocietyPermission.values());
        } catch (ObjectMappingException e) {
            logger.warn("Failed to serialize sub-society", e);
        }
    }

    private void deserializeSubSociety(Society society, ConfigurationNode node) {
        SubSociety subSociety = new SubSocietyImpl(societies, society, node.getNode(ID_KEY).getString());
        try {
            deserializeTaxable(subSociety, node);
            deserializePermissions(subSociety, node, SocietyPermission::valueOf);
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize sub-society", e);
            //noinspection SuspiciousMethodCalls
            society.getSubSocieties().remove(subSociety);
        }
    }

    private void serializeMember(Member member, ConfigurationNode node) {
        try {
            node.getNode(UUID_KEY).setValue(UUID_TOKEN, member.getUser().getUniqueId());
            node.getNode(RANK_KEY).setValue(member.getRank().getIdentifier());
            if (member.getTitle() == member.getRank().getTitle()) {
                node.getNode(TITLE_KEY).setValue(TEXT_TOKEN, member.getTitle());
            }
            serializeTaxable(member, node);
            serializePermissions(member, node.getNode(PERMISSIONS_KEY), MemberPermission.values());
            if (member.getSociety().getLeaders().containsKey(member.getUser().getUniqueId())) {
                node.getNode(LEADER_KEY).setValue(true);
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to serialize member", e);
        }
    }

    private void deserializeMember(Society society, ConfigurationNode node) {
        Member member = null;
        try {
            UUID uuid = node.getNode(UUID_KEY).getValue(UUID_TOKEN);
            User user = Sponge.getGame().getServiceManager().provideUnchecked(UserStorageService.class).get(uuid).orElse(null);
            if (user == null) {
                logger.warn("Failed to get User for {}", uuid.toString());
                return;
            }
            member = new MemberImpl(societies, society, user, society.getRanks().get(node.getNode(RANK_KEY).getString()), null);
            Text buffer = node.getNode(TITLE_KEY).getValue(TEXT_TOKEN);
            if (buffer != null) {
                member.setTitle(buffer, null);
            }
            deserializeTaxable(member, node);
            deserializePermissions(member, node.getNode(PERMISSIONS_KEY), MemberPermission::valueOf);
            if (node.getNode(LEADER_KEY).getBoolean()) {
                society.getLeaders().put(uuid, member);
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize member", e);
            if (member != null) {
                society.getMembers().remove(member.getUser().getUniqueId());
            }
        }
    }

    private void serializeRank(MemberRank rank, final ConfigurationNode node) {
        try {
            rank.getParent().ifPresent(r -> node.getNode(PARENT_KEY).setValue(r.getIdentifier()));
            node.getNode(TITLE_KEY).setValue(TEXT_TOKEN, rank.getTitle());
            node.getNode(DESCRIPTION_KEY).setValue(TEXT_TOKEN, rank.getDescription());
            serializeTaxable(rank, node);
            serializePermissions(rank, node.getNode(PERMISSIONS_KEY), MemberPermission.values());
        } catch (ObjectMappingException e) {
            logger.warn("Failed to serialize member rank", e);
        }
    }

    private void deserializeRank(Society society, ConfigurationNode node) {
        MemberRank rank = null;
        try {
            rank = new MemberRankImpl(societies, society, null, node.getNode(TITLE_KEY).getValue(TEXT_TOKEN), null);
            rank.setDescription(node.getNode(DESCRIPTION_KEY).getValue(TEXT_TOKEN), null);
            deserializeTaxable(rank, node);
            deserializePermissions(rank, node.getNode(PERMISSIONS_KEY), MemberPermission::valueOf);
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize member rank", e);
            if (rank != null) {
                society.getRanks().remove(rank.getTitle().toPlain());
            }
        }
    }

    private void serializeTaxable(Taxable taxable, ConfigurationNode node) throws ObjectMappingException {
        node.getNode(FIXED_TAX_KEY).setValue(BIG_DECIMAL_TOKEN, taxable.getFixedTax());
        node.getNode(SALARY_KEY).setValue(BIG_DECIMAL_TOKEN, taxable.getSalary());
    }

    private void deserializeTaxable(Taxable taxable, ConfigurationNode node) throws ObjectMappingException {
        taxable.setSalary(node.getNode(SALARY_KEY).getValue(BIG_DECIMAL_TOKEN), null);
        taxable.setFixedTax(node.getNode(FIXED_TAX_KEY).getValue(BIG_DECIMAL_TOKEN), null);
    }

    private <T extends Enum<T>> void serializePermissions(PermissionHolder<T> holder, ConfigurationNode node, T[] values) {
        for (T value : values) {
            switch (holder.getPermission(value)) {
                case TRUE:
                    node.getNode(value.name()).setValue(1);
                    break;
                case FALSE:
                    node.getNode(value.name()).setValue(-1);
                    break;
            }
        }
    }

    private <T extends Enum<T>> void deserializePermissions(final PermissionHolder<T> holder, ConfigurationNode node, final Function<String, T> getter) {
        node.getChildrenList().stream().map(ConfigurationNode::getString).map(getter).forEach(perm -> holder.setPermission(perm, PermissionState.TRUE, null));
    }
}
