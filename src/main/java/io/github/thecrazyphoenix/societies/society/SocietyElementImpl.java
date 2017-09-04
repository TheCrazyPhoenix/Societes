package io.github.thecrazyphoenix.societies.society;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.SocietyElement;

public class SocietyElementImpl implements SocietyElement {
    protected Societies societies;
    protected Society society;

    protected SocietyElementImpl(Societies societies, Society society) {
        this.societies = societies;
        this.society = society;
    }

    @Override
    public Society getSociety() {
        return society;
    }
}
