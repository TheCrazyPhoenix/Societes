package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.event.SocietyChangeEventImpl;
import com.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SocietyImpl implements Society {
    private static final Text DEFAULT_FOUNDER_TITLE = Text.of("Founder");

    private Societies societies;

    private String id;
    private Text name;
    private Text abbreviatedName;
    private String accountName;

    private Set<Member> leaders;
    private Set<Member> members;
    private Set<Claim> claims;
    private Map<String, MemberRank> ranks;
    private Map<String, SubSociety> subSocieties;

    public SocietyImpl(Societies societies, Text name, Text abbreviatedName, User founder) {
        this(societies, name, abbreviatedName);
        leaders.add(new MemberImpl(societies, this, founder, new MemberRankImpl(societies, this, null, DEFAULT_FOUNDER_TITLE)));
    }

    public SocietyImpl(Societies societies, Text name, Text abbreviatedName) {
        if (!CommonMethods.isValidName(name.toPlain())) {
            throw new IllegalArgumentException("illegal society name: " + name.toPlain());
        } else if (!CommonMethods.isValidName(abbreviatedName.toPlain()) || abbreviatedName.toPlain().indexOf(' ') != -1) {
            throw new IllegalArgumentException("illegal society abbreviated name: " + abbreviatedName.toPlain());
        }
        this.societies = societies;
        this.name = name;
        this.abbreviatedName = abbreviatedName;
        accountName = String.format("%s:%s", Societies.PLUGIN_ID, id = CommonMethods.nameToID(name));      // Slightly faster than String#replaceAll in the long run.
        leaders = new HashSet<>();
        members = new HashSet<>();
        claims = new HashSet<>();
        ranks = new HashMap<>();
        subSocieties = new HashMap<>();
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
    public Set<Member> getLeaders() {
        return leaders;
    }

    @Override
    public Set<Member> getMembers() {
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
