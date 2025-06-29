package org.classyclan.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final ClassyClan plugin;
    private final File messageFile;
    private FileConfiguration config;
    private final Map<String, String> cache = new HashMap<>();

    public MessageManager(ClassyClan plugin) {
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
        String prefix = cache.getOrDefault("prefix", "&cᴄʟᴀѕѕʏ ᴄʟᴀɴѕ &7→ ");

        msg = msg.replace("{prefix}", prefix);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }

    public static void sendClickableMessage(Player player,
                                            String prefixWithCodes,
                                            String buttonWithCodes,
                                            String command) {
        // 1) Parse le préfixe et ajoute un espace à la fin
        BaseComponent[] prefix = TextComponent.fromLegacyText(prefixWithCodes + " ");

        // 2) Parse le bouton (ex : "§l§a[JOIN]")
        BaseComponent[] button = TextComponent.fromLegacyText(buttonWithCodes);

        // 3) Affecte l’événement CLICK à chaque composante du bouton
        for (BaseComponent comp : button) {
            if (comp instanceof TextComponent tc) {
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
            }
        }

        // 4) On crée un conteneur TextComponent vide
        TextComponent message = new TextComponent();

        // 5) On y colle le préfixe puis le bouton
        message.addExtra(Arrays.toString(prefix));
        message.addExtra(Arrays.toString(button));

        // 6) Envoi dans le chat du joueur
        player.spigot().sendMessage(message);
    }

    /**
     * Envoie seulement un bouton cliquable isolé, avec codes §.
     */
    public static void sendClickableButton(Player player,
                                           String buttonWithCodes,
                                           String command) {
        sendClickableMessage(player, "", buttonWithCodes, command);
    }
}
