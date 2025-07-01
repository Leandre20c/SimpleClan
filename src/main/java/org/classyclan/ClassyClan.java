// 📄 SimpleClan.java
package org.classyclan;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.classyclan.api.ClassyClanAPI;
import org.classyclan.chat.ClanChatListener;
import org.classyclan.chat.ClanChatManager;
import org.classyclan.clan.ClanManager;
import org.classyclan.command.ClanCommand;
import org.classyclan.gui.GuiManager;
import org.classyclan.listener.BaseTeleportListener;
import org.classyclan.menu.MembersMenuListener;
import org.classyclan.menu.allClans.AllClansMenuListener;
import org.classyclan.menu.bases.BasesMenuListener;
import org.classyclan.menu.clan.ClanMenuListener;
import org.classyclan.menu.color.ColorMenuListener;
import org.classyclan.menu.members.MemberOptionsMenuListener;
import org.classyclan.menu.vaults.VaultMenuListener;
import org.classyclan.menu.settings.SettingsMenuListener;
import org.classyclan.message.MessageManager;
import org.classyclan.placeholder.ClanPlaceholderExpansion;
import org.classyclan.ranks.ClanRank;
import org.classyclan.ranks.RankConfig;
import org.classyclan.vault.VaultListener;

import java.io.File;
import java.util.List;

public class ClassyClan extends JavaPlugin {

    private static ClassyClan instance;

    private ClanManager clanManager;
    private static MessageManager messageManager;
    private GuiManager guiManager;
    private static Economy economy;
    private ClanChatManager clanChatManager;
    private static ClassyClanAPI api;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.clanManager = new ClanManager();
        messageManager = new MessageManager(this);
        this.clanChatManager = new ClanChatManager();
        this.guiManager = new GuiManager(this);
        api = new ClassyClanAPI(clanManager);

        // Enregistrement des commandes
        ClanCommand clanCommand = new ClanCommand();
        getCommand("clan").setExecutor(clanCommand);
        getCommand("clan").setTabCompleter(clanCommand);
        getServer().getPluginManager().registerEvents(new ClanMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new ColorMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new SettingsMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new BasesMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new VaultMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new ClanChatListener(), this);
        getServer().getPluginManager().registerEvents(new AllClansMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new MembersMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new MemberOptionsMenuListener(this), this);

        getServer().getPluginManager().registerEvents(new BaseTeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new VaultListener(), this);



        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ClanPlaceholderExpansion(clanManager).register();
            getLogger().info("✅ Clan PlaceholderAPI expansion registered.");
        } else {
            getLogger().warning("⚠️ PlaceholderAPI non détecté, les placeholders ne fonctionneront pas.");
        }

        // Vault Economy/Permission/Chat setup
        if (!setupEconomy()) {
            getLogger().severe("❌ Vault non détecté ! Le plugin va être désactivé.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Chargement des clans
        clanManager.loadAllFromStorage();
        loadRankConfigs();

    }

    @Override
    public void onDisable() {
        if (clanManager != null) {
            clanManager.saveAllToStorage();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public static ClassyClan getInstance() {
        return instance;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public static MessageManager getMessages() {
        return messageManager;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public void loadRankConfigs() {
        File file = new File(getDataFolder(), "ranks.yml");
        if (!file.exists()) {
            saveResource("ranks.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("ranks");

        if (section == null) {
            getLogger().severe("Missing 'ranks' section in ranks.yml!");
            return;
        }

        for (String key : section.getKeys(false)) {
            try {
                ClanRank rank = ClanRank.valueOf(key.toUpperCase());
                String displayName = section.getString(key + ".display_name", rank.name());
                List<String> permissions = section.getStringList(key + ".permissions");
                List<String> description = section.getStringList(key + ".description");
                rank.loadConfig(new RankConfig(displayName, permissions, description));
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid rank in ranks.yml: " + key);
            }
        }

        getLogger().info("Ranks loaded successfully.");
    }

    public ClanChatManager getClanChatManager() {
        return clanChatManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public static ClassyClanAPI getAPI() {
        return api;
    }
}
