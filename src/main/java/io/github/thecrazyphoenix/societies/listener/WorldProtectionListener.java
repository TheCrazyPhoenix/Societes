package io.github.thecrazyphoenix.societies.listener;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.society.Member;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.AttackEntityEvent;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class WorldProtectionListener {
    private Societies societies;

    public WorldProtectionListener(Societies societies) {
        this.societies = societies;
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onChangeBlock(ChangeBlockEvent.Post event, @First Player player) {
        boolean preserve = false;
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            if (!transaction.isValid()) {
                continue;
            }
            transaction.getFinal().getLocation().ifPresent(location -> checkLocation(player.getUniqueId(), location, ph -> ph.hasPermission(ClaimPermission.BUILD), () -> transaction.setValid(false)));
            preserve = preserve || transaction.isValid();
        }
        event.setCancelled(!preserve);
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onInteractBlock(InteractBlockEvent.Secondary event, @First Player player) {
        if (player.getItemInHand(event.getHandType()).map(item -> item.getType().getBlock()).isPresent()) {
            return;     // Allow block placing without INTERACT permission
        }
        event.getTargetBlock().getLocation().ifPresent(location -> checkLocation(player.getUniqueId(), location, ph -> ph.hasPermission(ClaimPermission.INTERACT), () -> event.setUseBlockResult(Tristate.FALSE)));
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onInteractEntity(InteractEntityEvent event, @First Entity source) {
        if (event.getTargetEntity() instanceof Hostile) {
            return;
        }
        toPlayer(source).ifPresent(player -> checkLocation(player.getUniqueId(), event.getTargetEntity().getLocation(), ph -> ph.hasPermission(ClaimPermission.INTERACT), () -> event.setCancelled(true)));
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onAttackEntity(AttackEntityEvent event, @First EntityDamageSource source) {
        if (event.getTargetEntity() instanceof Hostile) {
            return;
        }
        toPlayer(source).ifPresent(player -> checkLocation(player.getUniqueId(), event.getTargetEntity().getLocation(), ph -> ph.hasPermission(ClaimPermission.DAMAGE), () -> event.setCancelled(true)));
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onDamageEntity(DamageEntityEvent event, @First EntityDamageSource source) {
        if (event.getTargetEntity() instanceof Hostile) {
            return;
        }
        toPlayer(source).ifPresent(player -> checkLocation(player.getUniqueId(), event.getTargetEntity().getLocation(), ph -> ph.hasPermission(ClaimPermission.DAMAGE), () -> event.setCancelled(true)));
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onCollideEntityImpact(CollideEntityEvent.Impact event, @First Entity source) {      // Necessary to protect item frames.
        if (event.getEntities().stream().allMatch(entity -> entity instanceof Monster)) {
            return;
        }
        toPlayer(source).ifPresent(player -> checkLocation(player.getUniqueId(), event.getEntities().get(0).getLocation(), ph -> ph.hasPermission(ClaimPermission.BUILD), () -> event.setCancelled(true)));
    }

    private void checkLocation(UUID uuid, Location<World> location, Function<PermissionHolder<ClaimPermission>, Boolean> hasPermission, Runnable cancel) {
        Claim claim = null;
        Optional<Claim> opt = societies.getSocietiesService().getSocieties(location.getExtent().getUniqueId()).values().stream().flatMap(s -> s.getClaims().stream()).filter(c -> c.isClaimed(location.getBlockPosition())).findAny();
        while (opt.isPresent()) {
            claim = opt.get();
            opt = claim.getSociety().getSubSocieties().values().stream().flatMap(s -> s.toSociety().getClaims().stream()).filter(c -> c.isClaimed(location.getBlockPosition())).findAny();
        }
        if (claim != null) {
            Member member = claim.getSociety().getMembers().get(uuid);
            if (member == null) {
                cancel.run();
                return;
            }
            Optional<MemberClaim> memberClaim = claim.getMemberClaims().stream().filter(c -> c.isClaimed(location.getBlockPosition())).findAny();
            if ((memberClaim.isPresent() && !memberClaim.get().getPermissions(member).map(hasPermission).orElse(false)) || !memberClaim.isPresent() && !claim.getPermissions(member.getRank()).map(ph -> ph.hasPermission(ClaimPermission.BUILD)).orElse(false)) {
                cancel.run();
            }
        }
    }

    private Optional<Player> toPlayer(Entity entity) {
        if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Player) {
            return Optional.of((Player) ((Projectile) entity).getShooter());
        } else if (entity instanceof Player) {
            return Optional.of((Player) entity);
        } else {
            return entity.getCreator().flatMap(societies::getPlayer);
        }
    }

    private Optional<Player> toPlayer(EntityDamageSource source) {
        Entity temp;
        if (source instanceof IndirectEntityDamageSource) {
            temp = ((IndirectEntityDamageSource) source).getIndirectSource();
            if (temp instanceof Player) {
                return Optional.of((Player) temp);
            }
        }
        temp = source.getSource();
        if (temp instanceof Player) {
            return Optional.of((Player) temp);
        }
        return Optional.empty();
    }
}
