package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

public class MemberImpl extends AbstractTaxable<MemberPermission> implements Member {
    private User user;
    private MemberRank rank;
    private Text title;

    private EconomyService economy;

    public MemberImpl(Society society, EconomyService economy, User user, MemberRank rank) {
        super(society, rank, rank);
        this.economy = economy;
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
    public void setTitle(Text newTitle) {
        title = newTitle;
    }

    @Override
    public Account getAccount() {
        return economy.getOrCreateAccount(user.getUniqueId()).orElseThrow(IllegalStateException::new);
    }
}
