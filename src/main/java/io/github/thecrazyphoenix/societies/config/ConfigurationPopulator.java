package io.github.thecrazyphoenix.societies.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.util.ArrayList;

public class ConfigurationPopulator {
    public static void populate(CommentedConfigurationNode node, Key key) {
        switch (key) {
            case PLUGIN:
                node.getNode("continue-on-critical-error").setComment("Continue running the server on critical error? If true, the plugin will attempt to unload itself. (ignored if the plugin fails to load this configuration)").setValue(false);
                break;
            case SOCIETIES_DATA:
                node.getNode("societies").setValue(new ArrayList<>());
                break;
            default:
                throw new IllegalArgumentException("attempt to populate using unknown key");
        }
    }

    public enum Key {
        PLUGIN, SOCIETIES_DATA
    }
}
