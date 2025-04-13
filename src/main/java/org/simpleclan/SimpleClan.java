package org.simpleclan;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleclan.clan.ClanManager;
import org.simpleclan.command.ClanCommand;
import org.simpleclan.message.MessageManager;
import org.simpleclan.placeholder.ClanPlaceholderExpansion;

public class SimpleClan extends JavaPlugin {

    private static SimpleClan instance;
    private static MessageManager messageManager;
    private static Economy economy;
    private static Permission permissions;
    private static Chat chat;

    private ClanManager clanManager;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Vault not found or no economy plugin found! Disabling SimpleClan...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialise les gestionnaires
        this.clanManager = new ClanManager();
        messageManager = new MessageManager(this);

        // Enregistrement de la commande principale
        ClanCommand clanCommand = new ClanCommand();
        getCommand("clan").setExecutor(clanCommand);
        getCommand("clan").setTabCompleter(clanCommand);

        // Enregistrement de l'expansion PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ClanPlaceholderExpansion(clanManager).register();
            getLogger().info("✅ Clan PlaceholderAPI expansion registered.");
        } else {
            getLogger().warning("⚠️ PlaceholderAPI non détecté, les placeholders ne fonctionneront pas.");
        }

        // Chargement des clans depuis le stockage
        clanManager.loadAllFromStorage();
    }

    @Override
    public void onDisable() {
        // Sauvegarde des clans à l'arrêt
        clanManager.saveAllToStorage();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

    public static SimpleClan getInstance() {
        return instance;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public static MessageManager getMessages() {
        return messageManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
