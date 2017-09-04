package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.event.MemberChangeEventImpl;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MemberImpl extends AbstractTaxable<MemberPermission> implements Member {
    private static final Set<MemberImpl> NEEDS_UPDATE = new HashSet<>();

    private User user;
    private UUID uuid;
    private MemberRank rank;
    private Text title;

    public MemberImpl(Societies societies, Society society, User user, MemberRank rank, Cause cause) {
        this(cause, societies, society, user.getUniqueId(), rank);
        this.user = user;
    }

    public MemberImpl(Societies societies, Society society, UUID uuid, MemberRank rank, Cause cause) {
        this(cause, societies, society, uuid, rank);
        this.uuid = uuid;
        NEEDS_UPDATE.add(this);
    }

    private MemberImpl(Cause cause, Societies societies, Society society, UUID uuid, MemberRank rank) {
        super(societies, society, rank, rank);
        this.rank = rank;
        if (!societies.queueEvent(new MemberChangeEventImpl.Create(cause, this))) {
            society.getMembers().put(uuid, this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }

    }

    public static void updateNeeded(UserStorageService userStorageService) {
        NEEDS_UPDATE.forEach(m -> m.user = userStorageService.get(m.uuid).orElse(null));
        NEEDS_UPDATE.clear();
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public MemberRank getRank() {
        return rank;
    }

    @Override
    public Text getTitle() {
        return title == null ? rank.getTitle() : title;
    }

    @Override
    public boolean setTitle(Text newTitle, Cause cause) {
        if (!societies.queueEvent(new MemberChangeEventImpl.ChangeTitle(cause, this, newTitle))) {
            title = newTitle;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public Account getAccount() {
        return societies.getEconomyService().getOrCreateAccount(user.getUniqueId()).orElseThrow(IllegalStateException::new);
    }
}
