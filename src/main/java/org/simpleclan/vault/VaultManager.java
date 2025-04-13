// 📁 VaultManager.java
package org.simpleclan.vault;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VaultManager {

    private static final Map<String, Inventory> vaults = new HashMap<>();

    public static Inventory getVault(Clan clan) {
        String key = clan.getName().toLowerCase();

        if (vaults.containsKey(key)) {
            return vaults.get(key);
        }

        Inventory inventory = Bukkit.createInventory(null, 27, "Clan Vault - " + clan.getName());
        loadVault(clan, inventory);
        vaults.put(key, inventory);
        return inventory;
    }

    public static void saveVault(Clan clan) {
        String key = clan.getName().toLowerCase();
        Inventory inventory = vaults.get(key);
        if (inventory == null) return;

        File file = getVaultFile(clan);
        FileConfiguration config = new YamlConfiguration();
        config.set("contents", inventory.getContents());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadVault(Clan clan, Inventory inventory) {
        File file = getVaultFile(clan);
        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ItemStack[] contents = ((ItemStack[]) config.get("contents"));
        if (contents != null) inventory.setContents(contents);
    }

    private static File getVaultFile(Clan clan) {
        File folder = new File(SimpleClan.getInstance().getDataFolder(), "vaults");
        if (!folder.exists()) folder.mkdirs();
        return new File(folder, clan.getName().toLowerCase() + ".yml");
    }
}
