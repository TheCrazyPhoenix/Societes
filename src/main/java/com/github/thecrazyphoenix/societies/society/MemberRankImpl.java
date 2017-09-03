package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.event.MemberRankChangeEventImpl;
import com.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import com.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class MemberRankImpl extends AbstractTaxable<MemberPermission> implements MemberRank {
    private String id;
    private MemberRank parent;
    private Text title;
    private Text description;

    public MemberRankImpl(Societies societies, Society society, MemberRank parent, Text title, Cause cause) {
        super(societies, society, new DefaultTaxable<>(societies, society), PowerlessPermissionHolder.MEMBER);
        this.parent = parent;
        this.title = title;
        description = Text.EMPTY;
        if (!society.getRanks().containsKey(id = CommonMethods.nameToID(title))) {
            throw new IllegalArgumentException("title must be unique");
        }
        if (societies.queueEvent(new MemberRankChangeEventImpl.Create(cause, this))) {
            society.getRanks().put(id, this);
        } else {
            throw new UnsupportedOperationException("create event cancelled");
        }
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public Optional<MemberRank> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean setParent(MemberRank newParent, Cause cause) {
        if (!societies.queueEvent(new MemberRankChangeEventImpl.ChangeParent(cause, this, newParent))) {
            parent = newParent;
            return true;
        }
        return false;
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
            return true;
        }
        return false;
    }
}
