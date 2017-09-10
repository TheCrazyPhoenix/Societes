package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ChangeSocietyElementEvent;
import io.github.thecrazyphoenix.societies.api.society.SocietyElement;
import org.spongepowered.api.event.cause.Cause;

public class ChangeSocietyElementEventImpl extends ChangeSocietyEventImpl implements ChangeSocietyElementEvent {
    private SocietyElement element;

    public ChangeSocietyElementEventImpl(Cause cause, SocietyElement element) {
        super(cause, element.getSociety());
        this.element = element;
    }

    @Override
    public SocietyElement getElement() {
        return element;
    }
}
