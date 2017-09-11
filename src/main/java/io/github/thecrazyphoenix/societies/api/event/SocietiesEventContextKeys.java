package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import org.spongepowered.api.event.cause.EventContextKey;

public enum SocietiesEventContextKeys {
    ;
    public static final EventContextKey<Contract> CONTRACT = EventContextKey.builder(Contract.class).id("societies:contract").name("Contract").build();
}
