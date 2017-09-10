package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.ChangePermissionEvent;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.event.ChangeMemberEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbstractPermissionHolder;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

public class MemberImpl extends AbstractPermissionHolder<MemberPermission> implements Member {
    private UUID user;
    private MemberRankImpl rank;
    private Text title;

    private MemberImpl(Builder builder) {
        super(builder);
        user = builder.user;
        rank = builder.rank;
        title = builder.title;
    }

    @Override
    public UUID getUser() {
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
        if (!societies.queueEvent(new ChangeMemberEventImpl.ChangeTitle(cause, this, newTitle))) {
            title = newTitle;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public Account getAccount() {
        return societies.getEconomyService().getOrCreateAccount(user).orElseThrow(IllegalStateException::new);
    }

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new ChangeMemberEventImpl.Destroy(cause, this))) {
            society.getMembersRaw().remove(user);
            rank.getMembersRaw().remove(user);
            return true;
        }
        return false;
    }

    @Override
    protected ChangePermissionEvent createEvent(MemberPermission permission, PermissionState newState, Cause cause) {
        return new ChangeMemberEventImpl.ChangePermission(cause, this, permission, newState);
    }

    public static class Builder extends AbstractPermissionHolder.Builder<Builder, MemberPermission> implements Member.Builder {
        private final Societies societies;
        private final MemberRankImpl rank;
        private UUID user;
        private Text title;

        Builder(Societies societies, MemberRankImpl rank) {
            super(societies, rank.getSociety(), rank);
            this.societies = societies;
            this.rank = rank;
        }

        @Override
        public Builder user(UUID user) {
            this.user = user;
            return this;
        }

        @Override
        public Builder title(Text title) {
            this.title = title;
            return this;
        }

        @Override
        public Optional<MemberImpl> build(Cause cause) {
            CommonMethods.checkNotNullState(user, "user is mandatory");
            MemberImpl member = new MemberImpl(this);
            if (!societies.queueEvent(new ChangeMemberEventImpl.Create(cause, member))) {
                rank.getSociety().getMembersRaw().put(user, member);
                rank.getMembersRaw().put(user, member);
                societies.onSocietyModified();
                return Optional.of(member);
            }
            return Optional.empty();
        }
    }
}
