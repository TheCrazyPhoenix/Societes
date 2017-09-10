package io.github.thecrazyphoenix.societies;

import com.google.inject.Inject;
import io.github.thecrazyphoenix.societies.api.SocietiesService;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;
import io.github.thecrazyphoenix.societies.config.ConfigurationPopulator;
import io.github.thecrazyphoenix.societies.config.SocietySerializer;
import io.github.thecrazyphoenix.societies.listener.WorldProtectionListener;
import io.github.thecrazyphoenix.societies.society.SocietyImpl;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.AsynchronousExecutor;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Plugin(id = Societies.PLUGIN_ID)
public class Societies {
    public static final String PLUGIN_ID = "societies";

    private static final Path PLUGIN_CONFIG = Paths.get(PLUGIN_ID + ".conf");
    private static final Path SOCIETIES_CONFIG = Paths.get("societies-data.conf");

    @Inject
    private Game game;
    @Inject
    private Logger logger;
    @Inject
    @AsynchronousExecutor
    private SpongeExecutorService executor;
    private SocietiesService societiesService;
    private EconomyService economyService;

    private ConfigurationLoader<CommentedConfigurationNode> pluginDataLoader;
    private ConfigurationLoader<CommentedConfigurationNode> societiesDataLoader;
    private SocietySerializer societySerializer;

    private Map<UUID, Map<String, Society>> societies;     // TODO Add more saving options
    private Map<UUID, Map<String, Society>> allSocieties;
    private Set<ContractAuthority> authorities;
    private boolean continueAfterFailure;

    private boolean societiesLoaded;

    public Societies() {
        societiesService = new SocietiesServiceImpl();
        societies = new HashMap<>();
        allSocieties = new HashMap<>();
        authorities = new HashSet<>();
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        try {
            ConfigurationNode node = pluginDataLoader.load();
            continueAfterFailure = node.getNode("continue-on-critical-error").getBoolean();

            node = societiesDataLoader.load();
            societies.clear();
            allSocieties.clear();
            societySerializer.deserializeSocieties(node);
        } catch (IOException e) {
            onFailure("Failed to load configuration, {}", e);
        }
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        societiesLoaded = true;
        game.getServiceManager().setProvider(this, SocietiesService.class, new SocietiesServiceImpl());
        game.getEventManager().registerListeners(this, new WorldProtectionListener(this));
    }

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event, @Getter("getNewProvider") EconomyService economyService) {
        this.economyService = economyService;
    }

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event, @Getter("getNewProvider") SocietiesService societiesService) {
        this.societiesService = societiesService;
    }

    public void onSocietyModified() {
        if (societiesLoaded) {
            executor.submit(this::saveSocieties);
        }
    }

    public Optional<Player> getPlayer(UUID uuid) {
        return game.getServer().getPlayer(uuid);
    }

    public boolean queueEvent(Event event) {
        return societiesLoaded && game.getEventManager().post(event);
    }

    public SocietiesService getSocietiesService() {
        return societiesService;
    }

    public EconomyService getEconomyService() {
        return economyService;
    }

    public Map<String, Society> getRootSocieties(UUID world) {
        return societies.computeIfAbsent(world, k -> new HashMap<>());
    }

    public Map<String, Society> getAllSocieties(UUID world) {
        return allSocieties.computeIfAbsent(world, k -> new HashMap<>());
    }

    public Collection<ContractAuthority> getAuthorities() {
        return authorities;
    }

    private void onFailure(String log, final Exception e) {
        game.getEventManager().unregisterPluginListeners(this);
        game.getCommandManager().getOwnedBy(this).forEach(game.getCommandManager()::removeMapping);
        game.getScheduler().getScheduledTasks(this).forEach(Task::cancel);
        if (continueAfterFailure) {
            logger.error(log, "disabling self", e);
        } else if (game.isServerAvailable()) {
            logger.error(log, "shutting down server", e);
            game.getServer().shutdown(Text.of("Fatal exception in plugin societies: ", e.getMessage()));
        } else {
            logger.error(log, "shutting down server once possible", e);
            game.getEventManager().registerListener(this, GameAboutToStartServerEvent.class, event -> game.getServer().shutdown(Text.of("Critical exception in plugin societies: ", e.getMessage())));
        }
    }

    private void saveSocieties() {
        ConfigurationNode node = societiesDataLoader.createEmptyNode();
        societySerializer.serializeSocieties(node, societies.values().stream().flatMap(m -> m.values().stream()));
        try {
            societiesDataLoader.save(node);
        } catch (IOException e) {
            logger.error("Failed to save societies", e);
        }
    }

    @Inject
    private void injectConfigDir(@ConfigDir(sharedRoot = false) Path configDir) {
        pluginDataLoader = HoconConfigurationLoader.builder().setPath(configDir.resolve(PLUGIN_CONFIG)).build();
        societiesDataLoader = HoconConfigurationLoader.builder().setPath(configDir.resolve(SOCIETIES_CONFIG)).build();
        societySerializer = new SocietySerializer(logger, (SocietiesServiceImpl) societiesService);
        try {
            Files.createDirectories(configDir);

            if (!Files.exists(configDir.resolve(PLUGIN_CONFIG))) {
                CommentedConfigurationNode node = pluginDataLoader.createEmptyNode();
                ConfigurationPopulator.populate(node, ConfigurationPopulator.Key.PLUGIN);
                pluginDataLoader.save(node);
            }

            if (!Files.exists(configDir.resolve(SOCIETIES_CONFIG))) {
                CommentedConfigurationNode node = societiesDataLoader.createEmptyNode();
                ConfigurationPopulator.populate(node, ConfigurationPopulator.Key.SOCIETIES_DATA);
                societiesDataLoader.save(node);
            }
        } catch (IOException e) {
            logger.warn("Failed to initialize configuration directory, this could potentially be fatal", e);
        }
    }

    public class SocietiesServiceImpl implements SocietiesService {

        private SocietiesServiceImpl() {
            authorities = new HashSet<>();
        }

        @Override
        public Map<String, Society> getSocieties(UUID worldUUID) {
            return Collections.unmodifiableMap(societies.computeIfAbsent(worldUUID, k -> new HashMap<>()));
        }

        @Override
        public Map<String, Society> getAllSocieties(UUID worldUUID) {
            return Collections.unmodifiableMap(allSocieties.computeIfAbsent(worldUUID, k -> new HashMap<>()));
        }

        @Override
        public void addAuthority(ContractAuthority authority) {
            authorities.add(authority);
        }

        @Override
        public void removeAuthority(ContractAuthority authority) {
            authorities.remove(authority);
        }

        @Override
        public SocietyImpl.Builder societyBuilder() {
            return new SocietyImpl.Builder(Societies.this);
        }
    }
}
