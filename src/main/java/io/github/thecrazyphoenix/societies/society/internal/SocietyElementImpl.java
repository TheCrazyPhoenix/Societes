package io.github.thecrazyphoenix.societies.society.internal;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SocietyElement;
import io.github.thecrazyphoenix.societies.society.SocietyImpl;

public class SocietyElementImpl implements SocietyElement {
    protected Societies societies;
    protected SocietyImpl society;

    protected SocietyElementImpl(Societies societies, SocietyImpl society) {
        this.societies = societies;
        this.society = society;
    }

    @Override
    public SocietyImpl getSociety() {
        return society;
    }
}
