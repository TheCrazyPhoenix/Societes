package com.github.thecrazyphoenix.societies.society;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.society.SocietyElement;

public class SocietyElementImpl implements SocietyElement {
    protected Society society;

    public SocietyElementImpl(Society society) {
        this.society = society;
    }

    @Override
    public Society getSociety() {
        return society;
    }
}
