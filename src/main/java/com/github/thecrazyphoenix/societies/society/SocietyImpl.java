package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SocietyImpl implements Society {
    private static final Pattern NAME_TO_ACCOUNT_PATTERN = Pattern.compile("\\s");
    private static final Text DEFAULT_FOUNDER_TITLE = Text.of("Founder");

    private EconomyService economy;

    private Text name;
    private Text abbreviatedName;
    private String accountName;

    private Set<Member> leaders;
    private Set<Member> members;
    private Set<SubSociety> subSocieties;
    private Set<Claim> claims;
    private Map<String, MemberRank> ranks;

    public SocietyImpl(EconomyService economy, Text name, Text abbreviatedName, User founder) {
        if (!CommonMethods.isValidName(name.toPlain())) {
            throw new IllegalArgumentException("illegal society name: " + name.toPlain());
        } else if (!CommonMethods.isValidName(abbreviatedName.toPlain()) || abbreviatedName.toPlain().indexOf(' ') != -1) {
            throw new IllegalArgumentException("illegal society abbreviated name: " + abbreviatedName.toPlain());
        }
        this.economy = economy;
        this.name = name;
        this.abbreviatedName = abbreviatedName;
        accountName = NAME_TO_ACCOUNT_PATTERN.matcher(name.toPlain()).replaceAll("_");      // Slightly faster than String#replaceAll in the long run.
        leaders.add(new MemberImpl(this, economy, founder, new MemberRankImpl(this, null, DEFAULT_FOUNDER_TITLE)));
        leaders = new HashSet<>();
        members = new HashSet<>();
        subSocieties = new HashSet<>();
        claims = new HashSet<>();
        ranks = new HashMap<>();
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
    public Set<SubSociety> getSubSocieties() {
        return subSocieties;
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
    public Account getAccount() {
        return economy.getOrCreateAccount(accountName).orElseThrow(IllegalStateException::new);
    }
}
