package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;

import java.util.function.Function;

public abstract class AbstractContract implements Contract {
    protected Societies societies;
    private String name;
    private String currency;
    private long interval;
    private Function<Cause, Boolean> onDestroy;

    public AbstractContract(Societies societies, String name, String currency, long interval, Function<Cause, Boolean> onDestroy) {
        this.societies = societies;
        this.name = name;
        this.currency = currency;
        this.interval = interval;
        this.onDestroy = onDestroy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Currency getCurrency() {
        return societies.getEconomyService().getCurrencies().stream().filter(c -> c.getId().equalsIgnoreCase(currency)).findAny().orElseThrow(() -> new IllegalStateException("no currency with id " + currency));
    }

    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public boolean destroy(Cause cause) {
        return onDestroy.apply(cause);
    }
}
