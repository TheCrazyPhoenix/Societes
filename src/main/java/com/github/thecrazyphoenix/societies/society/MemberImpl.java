package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.event.MemberChangeEventImpl;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

public class MemberImpl extends AbstractTaxable<MemberPermission> implements Member {
    private User user;
    private MemberRank rank;
    private Text title;

    public MemberImpl(Societies societies, Society society, User user, MemberRank rank) {
        super(societies, society, rank, rank);
        this.user = user;
        this.rank = rank;
        society.getMembers().add(this);
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
        if (!societies.queueEvent(new MemberChangeEventImpl.ChangeTitle(cause, society, this, newTitle))) {
            title = newTitle;
            return true;
        }
        return false;
    }

    @Override
    public Account getAccount() {
        return societies.getEconomyService().getOrCreateAccount(user.getUniqueId()).orElseThrow(IllegalStateException::new);
    }
}
