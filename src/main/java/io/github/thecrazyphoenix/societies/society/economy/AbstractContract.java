package io.github.thecrazyphoenix.societies.society.economy;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import org.spongepowered.api.service.economy.Currency;

public abstract class AbstractContract implements Contract {
    protected Societies societies;
    protected String name;
    protected String currency;
    protected long interval;

    public AbstractContract(Societies societies, String name, String currency, long interval) {
        this.societies = societies;
        this.name = name;
        this.currency = currency;
        this.interval = interval;
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
}
