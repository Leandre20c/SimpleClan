package org.simpleclan.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.simpleclan.SimpleClan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final SimpleClan plugin;
    private final File messageFile;
    private FileConfiguration config;
    private final Map<String, String> cache = new HashMap<>();

    public MessageManager(SimpleClan plugin) {
        this.plugin = plugin;
        this.messageFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messageFile.exists()) {
            plugin.saveResource("messages.yml", true);
        }

        this.config = YamlConfiguration.loadConfiguration(messageFile);
        loadAll();
    }

    private void loadAll() {
        for (String key : config.getKeys(true)) {
            cache.put(key, config.getString(key));
        }
    }

    public void reload() {
        cache.clear();
        this.config = YamlConfiguration.loadConfiguration(messageFile);
        loadAll();
    }

    public String get(String key) {
        return get(key, Map.of());
    }

    public String get(String key, Map<String, String> placeholders) {
        String msg = cache.getOrDefault(key, "&cMessage not found: " + key);
        String prefix = cache.getOrDefault("prefix", "&7[Clan] ");

        msg = msg.replace("{prefix}", prefix);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
