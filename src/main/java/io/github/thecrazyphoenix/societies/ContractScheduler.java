package io.github.thecrazyphoenix.societies;

import io.github.thecrazyphoenix.societies.api.event.SocietiesEventContextKeys;
import io.github.thecrazyphoenix.societies.api.society.economy.Contract;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ContractScheduler {
    private Societies societies;
    private Map<ContractAuthority, Set<SpongeExecutorService.SpongeFuture<?>>> authorities;

    public ContractScheduler(Societies societies) {
        this.societies = societies;
        authorities = new HashMap<>();
    }

    public void addAuthority(ContractAuthority authority) {
        if (!authorities.containsKey(authority)) {
            authorities.put(authority, authority.getContracts().stream().map(c -> societies.getExecutor().scheduleAtFixedRate(() -> runContract(c), 0L, c.getInterval(), TimeUnit.MILLISECONDS)).collect(Collectors.toSet()));
        }
    }

    public void removeAuthority(ContractAuthority authority) {
        if (authorities.containsKey(authority)) {
            authorities.remove(authority).forEach(f -> f.cancel(false));
        }
    }

    private void runContract(Contract contract) {
        Cause cause = Cause.of(EventContext.builder().add(SocietiesEventContextKeys.CONTRACT, contract).build(), societies);
        contract.getApplicablePairs().forEach(p -> p.getLeft().getAccount().transfer(p.getRight().getAccount(), contract.getCurrency(), contract.getAmount(p.getLeft(), p.getRight()), cause));
    }
}
