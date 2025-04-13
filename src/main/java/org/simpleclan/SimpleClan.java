package org.simpleclan;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleclan.command.ClanCommand;
import org.simpleclan.menu.ClanMenuListener;
import org.simpleclan.message.MessageManager;
import org.simpleclan.vault.VaultListener;

public final class SimpleClan extends JavaPlugin {

    private static SimpleClan instance;
    private static MessageManager messageManager;
    private static Economy economy;

    @Override
    public void onEnable() {
        instance = this;

        if (!setupEconomy()) {
            getLogger().severe("Vault not found or no economy plugin found! Disabling SimpleClan...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        messageManager = new MessageManager(this);

        ClanCommand clanCommand = new ClanCommand();
        getCommand("clan").setExecutor(clanCommand);
        getCommand("clan").setTabCompleter(clanCommand);

        getServer().getPluginManager().registerEvents(new ClanMenuListener(), this);
        getServer().getPluginManager().registerEvents(new VaultListener(), this);

        getLogger().info("SimpleClan has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleClan has been disabled.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public static SimpleClan getInstance() {
        return instance;
    }

    public static MessageManager getMessages() {
        return messageManager;
    }

    public static Economy getEconomy() {
        return economy;
    }
}