package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.SocietiesService;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.event.SocietyChangeEventImpl;
import com.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SocietyImpl implements Society {
    private Societies societies;

    private String id;
    private Text name;
    private Text abbreviatedName;
    private String accountName;

    private Map<UUID, Member> leaders;
    private Map<UUID, Member> members;
    private Set<Claim> claims;
    private Map<String, MemberRank> ranks;
    private Map<String, SubSociety> subSocieties;

    public SocietyImpl(Societies societies, Text name, Text abbreviatedName, Cause cause) {
        if (!CommonMethods.isValidName(name.toPlain())) {
            throw new IllegalArgumentException("illegal society name: " + name.toPlain());
        } else if (!CommonMethods.isValidName(abbreviatedName.toPlain()) || abbreviatedName.toPlain().indexOf(' ') != -1) {
            throw new IllegalArgumentException("illegal society abbreviated name: " + abbreviatedName.toPlain());
        }
        this.societies = societies;
        this.name = name;
        this.abbreviatedName = abbreviatedName;
        accountName = String.format("%s:%s", Societies.PLUGIN_ID, id = CommonMethods.nameToID(name));
        leaders = new HashMap<>();
        members = new HashMap<>();
        claims = new HashSet<>();
        ranks = new HashMap<>();
        subSocieties = new HashMap<>();
        if (societies.queueEvent(new SocietyChangeEventImpl.Create(cause, this))) {
            SocietiesService societiesService = societies.getSocietiesService();
            societiesService.getSocieties().put(id, this);
            societiesService.getAllSocieties().put(id, this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public Text getAbbreviatedName() {
        return abbreviatedName;
    }

    @Override
    public Map<UUID, Member> getLeaders() {
        return leaders;
    }

    @Override
    public Map<UUID, Member> getMembers() {
        return members;
    }

    @Override
    public Set<Claim> getClaims() {
        return claims;
    }

    @Override
    public Map<String, MemberRank> getRanks() {
        return ranks;
    }

    @Override
    public Map<String, SubSociety> getSubSocieties() {
        return subSocieties;
    }

    @Override
    public Account getAccount() {
        return societies.getEconomyService().getOrCreateAccount(accountName).orElseThrow(IllegalStateException::new);
    }
}
