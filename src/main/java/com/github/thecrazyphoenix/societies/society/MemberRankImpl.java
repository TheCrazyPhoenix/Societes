package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.permission.PowerlessPermissionHolder;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class MemberRankImpl extends AbstractTaxable<MemberPermission> implements MemberRank {
    private MemberRank parent;
    private Text title;
    private Text description;

    public MemberRankImpl(Society society, MemberRank parent, Text title) {
        super(society, new DefaultTaxable<>(society), PowerlessPermissionHolder.MEMBER);
        this.parent = parent;
        this.title = title;
        description = Text.EMPTY;
        if (society.getRanks().putIfAbsent(title.toPlain(), this) != null) {
            throw new IllegalArgumentException("title must be unique");
        }
    }

    @Override
    public Optional<MemberRank> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public void setParent(MemberRank newParent) {
        parent = newParent;
    }

    @Override
    public Text getTitle() {
        return title;
    }

    @Override
    public void setTitle(Text newTitle) {
        title = newTitle;
        // TODO Validate this value.
    }

    @Override
    public Text getDescription() {
        return description;
    }

    @Override
    public void setDescription(Text newDescription) {
        description = newDescription;
    }
}
