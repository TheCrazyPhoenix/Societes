package com.github.thecrazyphoenix.societies.config;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.land.Cuboid;
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
import com.github.thecrazyphoenix.societies.permission.PermissionHolderImpl;
import com.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import com.github.thecrazyphoenix.societies.society.MemberImpl;
import com.github.thecrazyphoenix.societies.society.MemberRankImpl;
import com.github.thecrazyphoenix.societies.society.SocietyImpl;
import com.github.thecrazyphoenix.societies.society.SubSocietyImpl;
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

    private static final String DEFAULT_PERMISSIONS_KEY = "default-permissions";
    private static final String MEMBER_PERMISSIONS_KEY = "member-permissions";
    private static final String SOCIETY_PERMISSIONS_KEY = "society-permissions";
    private static final String LAND_TAX_KEY = "land-tax";
    private static final String LAND_VALUE_KEY = "land-value";

    private static final TypeToken<Text> TEXT_TOKEN = TypeToken.of(Text.class);
    private static final TypeToken<UUID> UUID_TOKEN = TypeToken.of(UUID.class);
    private static final TypeToken<BigDecimal> BIG_DECIMAL_TOKEN = TypeToken.of(BigDecimal.class);
    private static final TypeToken<Vector3i> VECTOR3I_TOKEN = TypeToken.of(Vector3i.class);

    private Logger logger;
    private Societies societies;

    public SocietySerializer(Logger logger, Societies societies) {
        this.societies = societies;
    }

    public void deserializeSocieties(ConfigurationNode rootNode, Map<String, Society> societies, Map<String, Society> allSocieties) {
        rootNode.getChildrenList().stream().map(this::deserializeSociety).forEach(s -> allSocieties.put(s.getIdentifier(), s));
        societies.putAll(allSocieties);
        allSocieties.values().stream().flatMap(s -> s.getSubSocieties().values().stream()).map(SubSociety::toSociety).map(Society::getIdentifier).forEach(societies::remove);
        SubSocietyImpl.updateAll(allSocieties);
    }

    private Society deserializeSociety(ConfigurationNode value) {
        try {
            final Society society = new SocietyImpl(societies, value.getNode(NAME_KEY).getValue(TEXT_TOKEN), value.getNode(ABBREVIATED_NAME_KEY).getValue(TEXT_TOKEN));
            value.getNode(RANKS_KEY).getChildrenList().forEach(node -> deserializeRank(society, node));
            society.getRanks().values().forEach(rank -> rank.setParent(society.getRanks().get(value.getNode(RANKS_KEY, PARENT_KEY).getString()), null));
            value.getNode(MEMBERS_KEY).getChildrenList().forEach(node -> deserializeMember(society, node));
            value.getNode(SUB_SOCIETIES_KEY).getChildrenList().forEach(node -> deserializeSubSociety(society, node));
            value.getNode(CLAIMS_KEY).getChildrenList().forEach(node -> deserializeClaims(society, node));
            return society;
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize society", e);
        }
        return null;
    }

    private void deserializeClaims(Society society, ConfigurationNode node) {
        Claim claim = new ClaimImpl(societies, society, new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM));
        try {
            for (ConfigurationNode child : node.getChildrenList()) {
                claim.getClaimCuboids().add(deserializeCuboid(society, child));
            }
            BigDecimal buffer = node.getNode(LAND_TAX_KEY).getValue(BIG_DECIMAL_TOKEN);
            if (buffer != null) {
                claim.setLandTax(buffer, null);
            }
            buffer = node.getNode(LAND_VALUE_KEY).getValue(BIG_DECIMAL_TOKEN);
            if (buffer != null) {
                claim.setLandValue(buffer, null);
            }
            deserializePermissions(claim.getDefaultPermissions(), node.getNode(DEFAULT_PERMISSIONS_KEY), ClaimPermission::valueOf);
            PermissionHolder<ClaimPermission> buffer2;
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(MEMBER_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
                //noinspection SuspiciousMethodCalls
                claim.setPermissions(society.getRanks().get(entry.getKey()), buffer2 = new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM));
                deserializePermissions(buffer2, entry.getValue(), ClaimPermission::valueOf);
            }
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getNode(SOCIETY_PERMISSIONS_KEY).getChildrenMap().entrySet()) {
                //noinspection SuspiciousMethodCalls
                claim.setPermissions(society.getSubSocieties().get(entry.getKey()), buffer2 = new PermissionHolderImpl<>(societies, society, PowerlessPermissionHolder.CLAIM));
                deserializePermissions(buffer2, entry.getValue(), ClaimPermission::valueOf);
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize claim", e);
            society.getClaims().remove(claim);
        }
    }

    private Cuboid deserializeCuboid(Society society, ConfigurationNode node) throws ObjectMappingException {
        return new CuboidImpl(societies, society, node.getNode("c1").getValue(VECTOR3I_TOKEN), node.getNode("c2").getValue(VECTOR3I_TOKEN));
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

    private void deserializeMember(Society society, ConfigurationNode node) {
        Member member = null;
        try {
            UUID uuid = node.getNode(UUID_KEY).getValue(UUID_TOKEN);
            User user = Sponge.getGame().getServiceManager().provideUnchecked(UserStorageService.class).get(uuid).orElse(null);
            if (user == null) {
                logger.warn("Failed to get User for {}", uuid.toString());
                return;
            }
            member = new MemberImpl(societies, society, user, society.getRanks().get(node.getNode(RANK_KEY).getString()));
            Text buffer = node.getNode(TITLE_KEY).getValue(TEXT_TOKEN);
            if (buffer != null) {
                member.setTitle(buffer, null);
            }
            deserializeTaxable(member, node);
            deserializePermissions(member, node.getNode(PERMISSIONS_KEY), MemberPermission::valueOf);
            if (node.getNode(LEADER_KEY).getBoolean()) {
                society.getLeaders().add(member);
            }
        } catch (ObjectMappingException e) {
            logger.warn("Failed to deserialize member", e);
            if (member != null) {
                society.getMembers().remove(member);
            }
        }
    }

    private void deserializeRank(Society society, ConfigurationNode node) {
        MemberRank rank = null;
        try {
            rank = new MemberRankImpl(societies, society, null, node.getNode(TITLE_KEY).getValue(TEXT_TOKEN));
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

    private void deserializeTaxable(Taxable taxable, ConfigurationNode node) throws ObjectMappingException {
        BigDecimal buffer = node.getNode(SALARY_KEY).getValue(BIG_DECIMAL_TOKEN);
        if (buffer != null) {
            taxable.setSalary(buffer, null);
        }
        buffer = node.getNode(FIXED_TAX_KEY).getValue(BIG_DECIMAL_TOKEN);
        if (buffer != null) {
            taxable.setFixedTax(buffer, null);
        }
    }

    private <T extends Enum<T>> void deserializePermissions(final PermissionHolder<T> holder, ConfigurationNode node, final Function<String, T> getter) {
        node.getChildrenList().stream().map(ConfigurationNode::getString).map(getter).forEach(perm -> holder.setPermission(perm, PermissionState.TRUE, null));
    }
}
