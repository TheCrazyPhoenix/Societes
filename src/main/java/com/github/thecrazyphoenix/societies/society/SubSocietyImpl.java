package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.event.SubSocietyChangeEvent;
import com.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.event.SubSocietyChangeEventImpl;
import com.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubSocietyImpl extends AbstractTaxable<SocietyPermission> implements SubSociety {
    private static final Set<SubSocietyImpl> NEEDS_UPDATE = new HashSet<>();

    private Society society;
    private String societyID;

    public SubSocietyImpl(Societies societies, Society owner, Society subSociety, Cause cause) {
        super(societies, owner, new DefaultTaxable<>(societies, owner), PowerlessPermissionHolder.SOCIETY);
        society = subSociety;
        if (societies.queueEvent(new SubSocietyChangeEventImpl.Create(cause, this))) {
            owner.getSubSocieties().put(societyID = subSociety.getAccount().getIdentifier(), this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    public SubSocietyImpl(Societies societies, Society owner, String subSocietyID) {
        super(societies, owner, new DefaultTaxable<>(societies, owner), PowerlessPermissionHolder.SOCIETY);
        owner.getSubSocieties().put(subSocietyID, this);
        societyID = subSocietyID;
        NEEDS_UPDATE.add(this);
    }

    public static void updateAll(Map<String, Society> societies) {
        NEEDS_UPDATE.forEach(s -> s.society = societies.get(s.societyID));
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
