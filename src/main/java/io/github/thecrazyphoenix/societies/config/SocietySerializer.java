package io.github.thecrazyphoenix.societies.config;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.Cuboid;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.society.ClaimImpl;
import io.github.thecrazyphoenix.societies.society.CuboidImpl;
import io.github.thecrazyphoenix.societies.society.MemberClaimImpl;
import io.github.thecrazyphoenix.societies.society.MemberImpl;
import io.github.thecrazyphoenix.societies.society.MemberRankImpl;
import io.github.thecrazyphoenix.societies.society.SocietyImpl;
import io.github.thecrazyphoenix.societies.society.SubSocietyImpl;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("ConstantConditions")
public class SocietySerializer {
    private static final String WORLD_KEY = "world";
    private static final String NAME_KEY = "name";
    private static final String ABBREVIATED_NAME_KEY = "abbreviated-name";
    private static final String RANKS_KEY = "ranks";
    private static final String MEMBERS_KEY = "members";
    private static final String SUB_SOCIETIES_KEY = "sub-societies";
    private static final String CLAIMS_KEY = "claims";
    private static final String SOCIETY_KEY = "society";

    private static final String TITLE_KEY = "title";
    private static final String DESCRIPTION_KEY = "description";
    private static final String CURRENCY_KEY = "currency";
    private static final String AMOUNT_KEY = "amount";
    private static final String INTERVAL_KEY = "interval";
    private static final String CHILDREN_KEY = "children";

    private static final String UUID_KEY = "uuid";
    private static final String CONTRACTS_KEY = "contracts";
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
    private Societies.SocietiesServiceImpl societies;

    public SocietySerializer(Logger logger, Societies.SocietiesServiceImpl societies) {
        this.logger = logger;
        this.societies = societies;
    }

    public void serializeSocieties(ConfigurationNode node, Stream<Society> societies) {
        Map<Society, ConfigurationNode> incomplete = new HashMap<>();
        Map<Society, ConfigurationNode> buffer = new HashMap<>();
        societies.forEach(s -> serializeSocietyFull(s, node.getAppendedNode(), incomplete));
        while (incomplete.size() > 0) {
            incomplete.forEach((s, n) -> serializeSocietyFull(s, n, buffer));
            incomplete.clear();
            incomplete.putAll(buffer);
            buffer.clear();
        }
    }

    public void deserializeSocieties(ConfigurationNode node) {
        Map<SocietyImpl, ConfigurationNode> incomplete = new HashMap<>();
        Map<SocietyImpl, ConfigurationNode> buffer = new HashMap<>();
        node.getChildrenList().forEach(n -> deserializeSocietyFull(n, incomplete));
        while (incomplete.size() > 0) {
            incomplete.forEach((s, n) -> deserializeSocietyFinish(s, n, buffer));
            incomplete.clear();
            incomplete.putAll(buffer);
            buffer.clear();
        }
    }

    private void serializeSocietyFull(Society society, ConfigurationNode node, Map<Society, ConfigurationNode> incomplete) {
        try {
            serializeSociety(society, node);
            Deque<MemberRank> ranks = new LinkedList<>();
            Deque<ConfigurationNode> nodes = new LinkedList<>();
            for (MemberRank rank : society.getRanks().values()) {
                ranks.add(rank);
                nodes.add(node.getNode(RANKS_KEY).getAppendedNode());
            }
            while (ranks.size() > 0) {
                MemberRank rank = ranks.pop();
                ConfigurationNode current = nodes.pop();
                serializeRank(rank, current);
                for (MemberRank child : rank.getChildren().values()) {
                    ranks.add(child);
                    nodes.add(current.getNode(CHILDREN_KEY).getAppendedNode());
                }
                for (Member member : rank.getMembers().values()) {
                    serializeMember(member, current.getNode(MEMBERS_KEY).getAppendedNode());
                }
            }
            for (SubSociety subSociety : society.getSubSocieties().values()) {
                serializeSubSociety(subSociety, node.getNode(SUB_SOCIETIES_KEY).getAppendedNode(), incomplete);
            }
            for (Claim claim : society.getClaims()) {
                serializeClaim(claim, node.getNode(CLAIMS_KEY).getAppendedNode());
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to serialize society", e);
        }
    }

    private void deserializeSocietyFull(ConfigurationNode node, Map<SocietyImpl, ConfigurationNode> incomplete) {
        try {
            SocietyImpl.Builder builder = societies.societyBuilder();
            deserializeSociety(builder, node);
            SocietyImpl society = builder.build(null).get();
            deserializeSocietyFinish(society, node, incomplete);
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize society", e);
        }
    }

    private void deserializeSocietyFinish(SocietyImpl society, ConfigurationNode node, Map<SocietyImpl, ConfigurationNode> incomplete) {
        try {
            Deque<MemberRankImpl> ranks = new LinkedList<>();
            Deque<ConfigurationNode> nodes = new LinkedList<>();
            for (ConfigurationNode child : node.getNode(RANKS_KEY).getChildrenList()) {
                MemberRankImpl.Builder rankBuilder = society.rankBuilder();
                deserializeRank(rankBuilder, child);
                ranks.add(rankBuilder.build(null).get());
                nodes.add(child);
            }
            while (ranks.size() > 0) {
                MemberRankImpl rank = ranks.pop();
                ConfigurationNode current = nodes.pop();
                for (ConfigurationNode child : current.getNode(CHILDREN_KEY).getChildrenList()) {
                    MemberRankImpl.Builder rankBuilder = society.rankBuilder();
                    deserializeRank(rankBuilder, child);
                    ranks.add(rankBuilder.build(null).get());
                    nodes.add(child);
                }
                for (ConfigurationNode child : current.getNode(MEMBERS_KEY).getChildrenList()) {
                    MemberImpl.Builder memberBuilder = rank.memberBuilder();
                    deserializeMember(memberBuilder, child);
                    memberBuilder.build(null);
                }
            }
            for (ConfigurationNode child : node.getNode(SUB_SOCIETIES_KEY).getChildrenList()) {
                SubSocietyImpl.Builder subSocietyBuilder = society.subSocietyBuilder();
                deserializeSubSociety(subSocietyBuilder, child, incomplete);
                subSocietyBuilder.build(null);
            }
            for (ConfigurationNode child : node.getNode(CLAIMS_KEY).getChildrenList()) {
                ClaimImpl.Builder claimBuilder = society.claimBuilder();
                deserializeClaim(claimBuilder, child);
                ClaimImpl claim = claimBuilder.build(null).get();
                for (ConfigurationNode grandchild : child.getNode(CUBOIDS_KEY).getChildrenList()) {
                    CuboidImpl.Builder cuboidBuilder = claim.cuboidBuilder();
                    deserializeCuboid(cuboidBuilder, grandchild);
                    cuboidBuilder.build(null);
                }
                for (ConfigurationNode grandchild : child.getNode(MEMBER_CLAIMS_KEY).getChildrenList()) {
                    MemberClaimImpl.Builder memberClaimBuilder = claim.memberClaimBuilder();
                    deserializeMemberClaim(memberClaimBuilder, grandchild);
                    memberClaimBuilder.build(null);
                }
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize society", e);
        }
    }

    private void serializeSociety(Society society, ConfigurationNode node) throws ObjectMappingException {
        node.getNode(WORLD_KEY).setValue(UUID_TOKEN, society.getWorldUUID());
        node.getNode(NAME_KEY).setValue(TEXT_TOKEN, society.getName());
        node.getNode(ABBREVIATED_NAME_KEY).setValue(TEXT_TOKEN, society.getAbbreviatedName());
    }

    private void deserializeSociety(SocietyImpl.Builder builder, ConfigurationNode node) throws ObjectMappingException {
        builder.world(node.getNode(WORLD_KEY).getValue(UUID_TOKEN)).name(node.getNode(NAME_KEY).getValue(TEXT_TOKEN)).abbreviatedName(node.getNode(ABBREVIATED_NAME_KEY).getValue(TEXT_TOKEN));
    }

    private void serializeSubSociety(SubSociety subSociety, ConfigurationNode node, Map<Society, ConfigurationNode> incomplete) throws ObjectMappingException {
        serializePermissions(subSociety, node.getNode("permissions"), SocietyPermission.values());
        incomplete.put(subSociety.toSociety(), node.getNode(SOCIETY_KEY));
    }

    private void deserializeSubSociety(SubSocietyImpl.Builder builder, ConfigurationNode node, Map<SocietyImpl, ConfigurationNode> incomplete) throws ObjectMappingException {
        SocietyImpl.Builder societyBuilder = societies.societyBuilder();
        deserializeSociety(societyBuilder, node.getNode(SOCIETY_KEY));
        SocietyImpl society = societyBuilder.build(null).get();
        incomplete.put(society, node.getNode(SOCIETY_KEY));
        builder.subSociety(society);
        deserializePermissions(builder::permission, node.getNode(PERMISSIONS_KEY), SocietyPermission::valueOf);
    }

    private void serializeClaim(Claim claim, ConfigurationNode node) throws ObjectMappingException {
        for (Map.Entry<? extends Currency, ? extends BigDecimal> entry : claim.getLandTax().entrySet()) {
            node.getNode(LAND_TAX_KEY, entry.getKey().getId()).setValue(BIG_DECIMAL_TOKEN, entry.getValue());
        }
        for (Map.Entry<? extends Currency, ? extends BigDecimal> entry : claim.getLandValue().entrySet()) {
            node.getNode(LAND_VALUE_KEY, entry.getKey().getId()).setValue(BIG_DECIMAL_TOKEN, entry.getValue());
        }
        serializePermissions(claim.getDefaultPermissions(), node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission.values());
        for (Cuboid cuboid : claim.getClaimCuboids()) {
            serializeCuboid(cuboid, node.getNode(CUBOIDS_KEY).getAppendedNode());
        }
        for (MemberClaim memberClaim : claim.getMemberClaims()) {
            serializeMemberClaim(memberClaim, node.getNode(MEMBER_CLAIMS_KEY).getAppendedNode());
        }
        for (Map.Entry<? extends MemberRank, ? extends PermissionHolder<ClaimPermission>> entry : claim.getMemberRankPermissions().entrySet()) {
            serializePermissions(entry.getValue(), node.getNode(MEMBER_PERMISSIONS_KEY, entry.getKey().getIdentifier()), ClaimPermission.values());
        }
        for (Map.Entry<? extends SubSociety, ? extends PermissionHolder<ClaimPermission>> entry : claim.getSubSocietyPermissions().entrySet()) {
            serializePermissions(entry.getValue(), node.getNode(SOCIETY_PERMISSIONS_KEY, entry.getKey().toSociety().getIdentifier()), ClaimPermission.values());
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void deserializeClaim(ClaimImpl.Builder builder, ConfigurationNode node) throws ObjectMappingException {
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(LAND_TAX_KEY).getChildrenMap().entrySet()) {
            builder.landTax((String) entry.getKey(), entry.getValue().getValue(BIG_DECIMAL_TOKEN));
        }
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(LAND_VALUE_KEY).getChildrenMap().entrySet()) {
            builder.landValue((String) entry.getKey(), entry.getValue().getValue(BIG_DECIMAL_TOKEN));
        }
        deserializePermissions(builder::defaultPermission, node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission::valueOf);
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(MEMBER_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
            MemberRank rank = builder.getSociety().getRanks().get(entry.getKey());
            deserializePermissions((p, v) -> builder.memberRankPermission(rank, p, v), entry.getValue(), ClaimPermission::valueOf);
        }
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(SOCIETY_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
            SubSociety subSociety = builder.getSociety().getSubSocieties().get(entry.getKey());
            deserializePermissions((p, v) -> builder.subSocietyPermission(subSociety, p, v), entry.getValue(), ClaimPermission::valueOf);
        }
    }

    private void serializeMemberClaim(MemberClaim memberClaim, ConfigurationNode node) throws ObjectMappingException {
        Optional<? extends Member> owner = memberClaim.getOwner();
        if (owner.isPresent()) {
            node.getNode(OWNER_KEY).setValue(UUID_TOKEN, owner.get().getUser());
        }
        serializeCuboid(memberClaim, node);
        serializePermissions(memberClaim.getDefaultPermissions(), node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission.values());
        for (Map.Entry<? extends Member, ? extends PermissionHolder<ClaimPermission>> entry : memberClaim.getMemberPermissions().entrySet()) {
            serializePermissions(entry.getValue(), node.getNode(MEMBER_PERMISSIONS_KEY, entry.getKey().getUser()), ClaimPermission.values());
        }
    }

    private void deserializeMemberClaim(MemberClaimImpl.Builder builder, ConfigurationNode node) throws ObjectMappingException {
        deserializeCuboid(builder, node);
        deserializePermissions(builder::defaultPermission, node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission::valueOf);
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(MEMBER_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
            Member member = builder.getSociety().getMembers().get(UUID.fromString((String) entry.getKey()));
            deserializePermissions((p, v) -> builder.memberPermission(member, p, v), entry.getValue(), ClaimPermission::valueOf);
        }
    }

    private void serializeCuboid(Cuboid cuboid, ConfigurationNode node) throws ObjectMappingException {
        node.getNode(CORNER1_KEY).setValue(VECTOR3I_TOKEN, cuboid.getFirstCorner());
        node.getNode(CORNER2_KEY).setValue(VECTOR3I_TOKEN, cuboid.getSecondCorner());
    }

    private void deserializeCuboid(CuboidImpl.Builder builder, ConfigurationNode node) throws ObjectMappingException {
        builder.corners(node.getNode(CORNER1_KEY).getValue(VECTOR3I_TOKEN), node.getNode(CORNER2_KEY).getValue(VECTOR3I_TOKEN));
    }

    private void serializeRank(MemberRank rank, final ConfigurationNode node) throws ObjectMappingException {
        node.getNode(TITLE_KEY).setValue(TEXT_TOKEN, rank.getTitle());
        node.getNode(DESCRIPTION_KEY).setValue(TEXT_TOKEN, rank.getDescription());
        for (MemberRank.RankContract contract : rank.getContracts()) {
            ConfigurationNode child = node.getNode(CONTRACTS_KEY).getAppendedNode();
            child.getNode(NAME_KEY).setValue(contract.getName());
            child.getNode(CURRENCY_KEY).setValue(contract.getCurrency().getId());
            child.getNode(AMOUNT_KEY).setValue(BIG_DECIMAL_TOKEN, contract.getAmount());
            child.getNode(INTERVAL_KEY).setValue(contract.getInterval());
        }
        serializePermissions(rank, node.getNode(PERMISSIONS_KEY), MemberPermission.values());
    }

    private void deserializeRank(MemberRankImpl.Builder builder, ConfigurationNode node) throws ObjectMappingException {
        builder.title(node.getNode(TITLE_KEY).getValue(TEXT_TOKEN)).description(node.getNode(DESCRIPTION_KEY).getValue(TEXT_TOKEN));
        for (ConfigurationNode child : node.getNode(CONTRACTS_KEY).getChildrenList()) {
            builder.addPayment(child.getNode(NAME_KEY).getString(), child.getNode(CURRENCY_KEY).getString(), child.getNode(AMOUNT_KEY).getValue(BIG_DECIMAL_TOKEN), child.getNode(INTERVAL_KEY).getLong(), TimeUnit.MILLISECONDS);
        }
        deserializePermissions(builder::permission, node.getNode(PERMISSIONS_KEY), MemberPermission::valueOf);
    }

    private void serializeMember(Member member, ConfigurationNode node) throws ObjectMappingException {
        node.getNode(UUID_KEY).setValue(UUID_TOKEN, member.getUser());
        if (member.getTitle() == member.getRank().getTitle()) {
            node.getNode(TITLE_KEY).setValue(TEXT_TOKEN, member.getTitle());
        }
        serializePermissions(member, node.getNode(PERMISSIONS_KEY), MemberPermission.values());
    }

    private void deserializeMember(MemberImpl.Builder builder, ConfigurationNode node) throws ObjectMappingException {
        builder.user(node.getNode(UUID_KEY).getValue(UUID_TOKEN)).title(node.getNode(TITLE_KEY).getValue(TEXT_TOKEN));
        deserializePermissions(builder::permission, node.getNode(PERMISSIONS_KEY), MemberPermission::valueOf);
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

    private <T extends Enum<T>> void deserializePermissions(BiConsumer<T, PermissionState> consumer, ConfigurationNode node, Function<? super String, T> getter) {
        node.getChildrenMap().forEach((Object k, ConfigurationNode n) -> consumer.accept(getter.apply((String) k), CommonMethods.intToState(node.getInt())));
    }
}
