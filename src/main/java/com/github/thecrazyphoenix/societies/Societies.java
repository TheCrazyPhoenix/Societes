package com.github.thecrazyphoenix.societies;

import com.github.thecrazyphoenix.societies.api.SocietiesService;
import com.github.thecrazyphoenix.societies.api.event.SocietyChangeEvent;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.config.ConfigurationPopulator;
import com.github.thecrazyphoenix.societies.config.SocietySerializer;
import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Plugin(id = Societies.PLUGIN_ID)
public class Societies {
    public static final String PLUGIN_ID = "societies";

    private static final Path PLUGIN_CONFIG = Paths.get(PLUGIN_ID + ".conf");
    private static final Path SOCIETIES_CONFIG = Paths.get("societies-data.conf");

    @Inject
    private Game game;
    @Inject
    private Logger logger;
    private EconomyService economy;

    private ConfigurationLoader<CommentedConfigurationNode> pluginDataLoader;
    private ConfigurationLoader<CommentedConfigurationNode> societiesDataLoader;    // TODO Actively store societies as they change
    private SocietySerializer societySerializer;

    private Map<String, Society> societies;
    private Map<String, Society> allSocieties;
    private boolean continueAfterFailure;

    private boolean queueEvents;

    public Societies() {
        societies = new HashMap<>();
        allSocieties = new HashMap<>();
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        try {
            ConfigurationNode node = pluginDataLoader.load();
            continueAfterFailure = node.getNode("continue-on-critical-error").getBoolean();

            node = societiesDataLoader.load().getNode("societies");
            societies.clear();
            allSocieties.clear();
            societySerializer.deserializeSocieties(node, societies, allSocieties);
        } catch (IOException e) {
            onFailure("Failed to load configuration, {}.", e);
        }
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        game.getServiceManager().setProvider(this, SocietiesService.class, new SocietiesServiceImpl());
        queueEvents = true;
    }

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event, @Getter("getNewProvider") EconomyService economy) {
        this.economy = economy;
    }

    public void onFailure(String log, final Exception e) {
        game.getEventManager().unregisterPluginListeners(this);
        game.getCommandManager().getOwnedBy(this).forEach(game.getCommandManager()::removeMapping);
        game.getScheduler().getScheduledTasks(this).forEach(Task::cancel);
        if (continueAfterFailure) {
            logger.error(log, "disabling self", e);
        } else if (game.isServerAvailable()) {
            logger.error(log, "shutting down server", e);
            game.getServer().shutdown(Text.of("Critical exception in plugin societies: ", e.getMessage()));
        } else {
            logger.error(log, "shutting down server once possible", e);
            game.getEventManager().registerListener(this, GameAboutToStartServerEvent.class, event -> game.getServer().shutdown(Text.of("Critical exception in plugin societies: ", e.getMessage())));
        }
    }

    public boolean queueEvent(SocietyChangeEvent event) {
        return !queueEvents || game.getEventManager().post(event);
    }

    public EconomyService getEconomyService() {
        return economy;
    }

    @Inject
    private void injectConfigDir(@ConfigDir(sharedRoot = false) Path configDir) {
        // TODO Add non-global serializers
        pluginDataLoader = HoconConfigurationLoader.builder().setPath(configDir.resolve(PLUGIN_CONFIG)).build();
        societiesDataLoader = HoconConfigurationLoader.builder().setPath(configDir.resolve(SOCIETIES_CONFIG)).build();
        societySerializer = new SocietySerializer(logger, this);
        try {
            Files.createDirectories(configDir);

            CommentedConfigurationNode node = pluginDataLoader.createEmptyNode();
            ConfigurationPopulator.populate(node, ConfigurationPopulator.Key.PLUGIN);
            pluginDataLoader.save(node);

            node = societiesDataLoader.createEmptyNode();
            ConfigurationPopulator.populate(node, ConfigurationPopulator.Key.SOCIETIES_DATA);
            societiesDataLoader.save(node);
        } catch (IOException e) {
            logger.warn("Failed to initialize configuration directory", e);
        }
    }

    private class SocietiesServiceImpl implements SocietiesService {
        @Override
        public Map<String, Society> getSocieties() {
            return societies;
        }

        @Override
        public Map<String, Society> getAllSocieties() {
            return societies;
        }
    }
}
