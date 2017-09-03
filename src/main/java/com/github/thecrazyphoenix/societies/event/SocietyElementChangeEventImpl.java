package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.event.SocietyElementChangeEvent;
import com.github.thecrazyphoenix.societies.api.society.SocietyElement;
import org.spongepowered.api.event.cause.Cause;

public class SocietyElementChangeEventImpl extends SocietyChangeEventImpl implements SocietyElementChangeEvent {
    private SocietyElement element;

    public SocietyElementChangeEventImpl(Cause cause, SocietyElement element) {
        super(cause, element.getSociety());
        this.element = element;
    }

    @Override
    public SocietyElement getElement() {
        return element;
    }
}
