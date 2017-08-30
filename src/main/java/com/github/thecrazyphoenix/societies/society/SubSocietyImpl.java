package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SubSociety;
import com.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import org.spongepowered.api.service.economy.account.Account;

public class SubSocietyImpl extends AbstractTaxable<SocietyPermission> implements SubSociety {
    private Society society;

    public SubSocietyImpl(Society owner, Society subSociety) {
        super(owner, new DefaultTaxable<>(owner), PowerlessPermissionHolder.SOCIETY);
        society = subSociety;
        owner.getSubSocieties().add(this);
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
