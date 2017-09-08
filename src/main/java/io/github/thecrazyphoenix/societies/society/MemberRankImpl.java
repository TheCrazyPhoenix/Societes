package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.event.MemberRankChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.AbsolutePermissionHolder;
import io.github.thecrazyphoenix.societies.permission.AbstractPermissionHolder;
import io.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import io.github.thecrazyphoenix.societies.society.economy.FixedContract;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MemberRankImpl extends AbstractPermissionHolder<MemberPermission> implements MemberRank {
    private MemberRankImpl parent;
    private String id;
    private Text title;
    private Text description;

    private Map<String, MemberRank> children;
    private Map<UUID, Member> members;
    private Map<String, MemberRank> viewChildren;
    private Map<UUID, Member> viewMembers;
    private Set<Contract> contracts;
    private Set<Contract> viewContracts;

    private MemberRankImpl(Builder builder) {
        super(builder);
        parent = builder.parent;
        id = CommonMethods.nameToID(title = builder.title);
        description = builder.description;
        viewChildren = Collections.unmodifiableMap(children = new HashMap<>());
        viewMembers = Collections.unmodifiableMap(members = new HashMap<>());
        viewContracts = Collections.unmodifiableSet(contracts = builder.contracts.stream().map(d -> new FixedContract(societies, society, d.name, d.currency, d.interval, d.amount, members.values(), cause -> false)).collect(Collectors.toSet()));
    }

    @Override
    public Optional<MemberRank> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Map<String, MemberRank> getChildren() {
        return viewChildren;
    }

    @Override
    public Map<UUID, Member> getMembers() {
        return viewMembers;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Text getTitle() {
        return title;
    }

    @Override
    public boolean setTitle(Text newTitle, Cause cause) {
        if (!CommonMethods.isValidName(newTitle.toPlain())) {
            throw new IllegalArgumentException("illegal title: " + newTitle.toPlain());
        } else if (!societies.queueEvent(new MemberRankChangeEventImpl.ChangeTitle(cause, this, newTitle))) {
            title = newTitle;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public Text getDescription() {
        return description;
    }

    @Override
    public boolean setDescription(Text newDescription, Cause cause) {
        if (!societies.queueEvent(new MemberRankChangeEventImpl.ChangeDescription(cause, this, newDescription))) {
            description = newDescription;
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public Builder rankBuilder() {
        return new Builder(societies, society, this);
    }

    @Override
    public MemberImpl.Builder memberBuilder() {
        return new MemberImpl.Builder(societies, this);
    }

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new MemberRankChangeEventImpl.Destroy(cause, this))) {
            society.getRanksRaw().remove(id);
            if (parent != null) {
                parent.getChildrenRaw().remove(id);
            }
            return true;
        }
        return false;
    }

    @Override
    protected PermissionChangeEvent createEvent(MemberPermission permission, PermissionState newState, Cause cause) {
        return new MemberRankChangeEventImpl.ChangePermission(cause, this, permission, newState);
    }

    Map<String, MemberRank> getChildrenRaw() {
        return children;
    }

    Map<UUID, Member> getMembersRaw() {
        return members;
    }

    @Override
    public Set<Contract> getContracts() {
        return viewContracts;
    }

    public static class Builder extends AbstractPermissionHolder.Builder<Builder, MemberPermission> implements MemberRank.Builder {
        private final Societies societies;
        private final SocietyImpl society;
        private final MemberRankImpl parent;
        private Text title;
        private Text description;
        private Collection<ContractData> contracts;

        Builder(Societies societies, SocietyImpl society, MemberRankImpl parent) {
            super(societies, society, parent == null ? AbsolutePermissionHolder.MEMBER : PowerlessPermissionHolder.MEMBER);
            this.societies = societies;
            this.society = society;
            this.parent = parent;
            contracts = new ArrayList<>();
        }

        @Override
        public Builder title(Text title) {
            this.title = title;
            return this;
        }

        @Override
        public Builder description(Text description) {
            this.description = description;
            return this;
        }

        @Override
        public MemberRank.Builder addPayment(String name, Currency currency, BigDecimal amount, long interval, TimeUnit unit) {
            addPayment(name, currency.getId(), amount, interval, unit);
            return this;
        }

        public MemberRank.Builder addPayment(String name, String currency, BigDecimal amount, long interval, TimeUnit unit) {
            contracts.add(new ContractData(name, currency, amount, interval, unit));
            return this;
        }

        @Override
        public Optional<MemberRankImpl> build(Cause cause) {
            CommonMethods.checkNotNullState(title, "title is mandatory");
            description = CommonMethods.orDefault(description, Text.EMPTY);
            MemberRankImpl memberRank = new MemberRankImpl(this);
            if (!societies.queueEvent(new MemberRankChangeEventImpl.Create(cause, memberRank))) {
                society.getRanksRaw().put(memberRank.getIdentifier(), memberRank);
                if (parent != null) {
                    parent.getChildrenRaw().put(memberRank.getIdentifier(), memberRank);
                }
                societies.onSocietyModified();
                return Optional.of(memberRank);
            }
            return Optional.empty();
        }
    }

    private static class ContractData {
        private String name;
        private String currency;
        private BigDecimal amount;
        private long interval;

        private ContractData(String name, String currency, BigDecimal amount, long interval, TimeUnit intervalUnit) {
            this.name = name;
            this.currency = currency;
            this.amount = amount;
            this.interval = intervalUnit.toMillis(interval);
        }
    }
}
