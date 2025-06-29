package org.classyclan.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.classyclan.ClassyClan;
import org.classyclan.banner.BannerUtils;
import org.classyclan.clan.Clan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiManager {

    private final ClassyClan plugin;
    private final File guiFile;
    private FileConfiguration config;
    private final Map<String, Object> cache = new HashMap<>();

    /** Ce champ est utilisé par getGuiItem(...) pour CLAN_BANNER. */
    private Clan contextClan;

    public GuiManager(ClassyClan plugin) {
        this.plugin = plugin;
        this.guiFile = new File(plugin.getDataFolder(), "guis.yml");

        if (!guiFile.exists()) {
            plugin.saveResource("guis.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(guiFile);
        loadAll();
    }

    private void loadAll() {
        cache.clear();
        for (String key : config.getKeys(true)) {
            cache.put(key, config.get(key));
        }
    }

    public void reload() {
        cache.clear();
        this.config = YamlConfiguration.loadConfiguration(guiFile);
        loadAll();
    }

    /**
     * Crée un ItemStack selon la clé dans guis.yml, en appliquant
     * éventuels placeholders et le contexte de clan pour CLAN_BANNER.
     *
     * @param key          La clé sous items.<key> dans guis.yml
     * @param placeholders Les {placeholders} à remplacer dans name & lore
     */
    @SuppressWarnings("unchecked")
    public ItemStack getGuiItem(String key, Map<String, String> placeholders) {
        String base = "items." + key + ".";
        String matName = (String) cache.getOrDefault(base + "material", "STONE");
        ItemStack item;

        // — Cas spécial #1 : tête de joueur
        if (matName.equalsIgnoreCase("PLAYERSKULL")) {
            // on crée un PLAYER_HEAD
            item = new ItemStack(Material.PLAYER_HEAD);
            // récupère le nom ou l’UUID passé en placeholder
            String ownerName = placeholders.getOrDefault("skullOwner", "");
            if (!ownerName.isEmpty()) {
                // prend l’OfflinePlayer (marche même offline)
                OfflinePlayer off = Bukkit.getOfflinePlayer(ownerName);
                SkullMeta sm = (SkullMeta) item.getItemMeta();
                // setOwner fonctionne pour offline et online
                sm.setOwner(off.getName());
                item.setItemMeta(sm);
            }

            // — Cas spécial #2 : bannière de clan
        } else if (matName.equalsIgnoreCase("CLAN_BANNER")) {
            Clan clan = contextClan;
            // Si aucun contexte, on retombe sur placeholder clanName
            if (clan == null) {
                String clanName = placeholders.get("clanName");
                if (clanName != null && !clanName.isBlank()) {
                    clan = ClassyClan.getInstance()
                            .getClanManager()
                            .getClanByName(clanName);
                }
            }
            if (clan != null && clan.getBanner() != null) {
                item = clan.getBanner().clone();
                BannerMeta bm = (BannerMeta) item.getItemMeta();
                item.setItemMeta(bm);
            } else {
                item = BannerUtils.getDefaultBanner().clone();
            }

            // — Cas par défaut : Material standard
        } else {
            Material mat = Material.valueOf(matName);
            item = new ItemStack(mat);
        }

        // —— Applique displayName & lore depuis la config ——
        ItemMeta meta = item.getItemMeta();

        // displayName
        String rawName = (String) cache.getOrDefault(base + "name", key);
        for (var e : placeholders.entrySet()) {
            rawName = rawName.replace("{" + e.getKey() + "}", e.getValue());
        }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', rawName));

        // lore
        List<String> rawLore = (List<String>) cache.getOrDefault(base + "lore", List.of());
        List<String> lore = new ArrayList<>(rawLore.size());
        for (String line : rawLore) {
            String l = line;
            for (var e : placeholders.entrySet()) {
                l = l.replace("{" + e.getKey() + "}", e.getValue());
            }
            lore.add(ChatColor.translateAlternateColorCodes('&', l));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /** Définit quel clan doit être utilisé pour le prochain CLAN_BANNER. */
    public void setContextClan(Clan clan) {
        this.contextClan = clan;
    }

    /** Supprime le contexte de clan après usage (à appeler après getGuiItem). */
    public void clearContextClan() {
        this.contextClan = null;
    }

    /** Accès brut à la config guis.yml si besoin. */
    public FileConfiguration getConfig() {
        return config;
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }
}
