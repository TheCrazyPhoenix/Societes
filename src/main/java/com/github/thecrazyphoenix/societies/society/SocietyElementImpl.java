package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SocietyElement;

public class SocietyElementImpl implements SocietyElement {
    protected Societies societies;
    protected Society society;

    public SocietyElementImpl(Societies societies, Society society) {
        this.societies = societies;
        this.society = society;
    }

    @Override
    public Society getSociety() {
        return society;
    }
}
