package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.AccountHolderChangeEvent;
import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public class AccountHolderChangeEventImpl extends AbstractEvent implements AccountHolderChangeEvent {
    private boolean cancelled;
    private Cause cause;
    private AccountHolder accountHolder;

    public AccountHolderChangeEventImpl(Cause cause, AccountHolder accountHolder) {
        this.cause = cause;
        this.accountHolder = accountHolder;
        cancelled = false;
    }

    @Override
    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public static class Create extends AccountHolderChangeEventImpl implements AccountHolderChangeEvent.Create {
        public Create(Cause cause, AccountHolder accountHolder) {
            super(cause, accountHolder);
        }
    }

    public static class Destroy extends AccountHolderChangeEventImpl implements AccountHolderChangeEvent.Destroy {
        public Destroy(Cause cause, AccountHolder accountHolder) {
            super(cause, accountHolder);
        }
    }
}
