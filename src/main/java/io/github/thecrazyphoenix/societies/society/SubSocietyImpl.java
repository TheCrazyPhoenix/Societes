package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SubSociety;
import io.github.thecrazyphoenix.societies.event.SubSocietyChangeEventImpl;
import io.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SubSocietyImpl extends AbstractTaxable<SocietyPermission> implements SubSociety {
    private static final Set<SubSocietyImpl> NEEDS_UPDATE = new HashSet<>();

    private Society society;
    private String societyID;

    public SubSocietyImpl(Societies societies, Society owner, Society subSociety, Cause cause) {
        this(cause, societies, owner, subSociety.getIdentifier());
        society = subSociety;
        if (!societies.queueEvent(new SubSocietyChangeEventImpl.Create(cause, this))) {
            owner.getSubSocieties().put(societyID = subSociety.getAccount().getIdentifier(), this);
            societies.getSocietiesService().getSocieties(society.getWorldUUID()).remove(subSociety.getIdentifier());
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    public SubSocietyImpl(Societies societies, Society owner, String subSocietyID, Cause cause) {
        this(cause, societies, owner, subSocietyID);
        societyID = subSocietyID;
        NEEDS_UPDATE.add(this);
    }

    private SubSocietyImpl(Cause cause, Societies societies, Society owner, String id) {
        super(societies, owner, new DefaultTaxable<>(societies, owner), PowerlessPermissionHolder.SOCIETY);
        if (societies.queueEvent(new SubSocietyChangeEventImpl.Create(cause, this))) {
            owner.getSubSocieties().put(id, this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    public static void updateNeeded(Map<UUID, Map<String, Society>> societies) {
        NEEDS_UPDATE.forEach(s -> s.society = societies.get(s.getSociety().getWorldUUID()).get(s.societyID));
        NEEDS_UPDATE.clear();
    }

    @Override
    public Society toSociety() {
        return society;
    }

    @Override
    public Account getAccount() {
        return society.getAccount();
    }
}
