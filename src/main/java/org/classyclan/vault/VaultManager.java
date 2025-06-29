// 📁 VaultManager.java
package org.classyclan.vault;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class VaultManager {

    private static final Map<String, Inventory> vaults = new HashMap<>();
    private static final VaultManager instance = new VaultManager();

    public static VaultManager get() {
        return instance;
    }

    public void openVault(Player player, Clan clan, int index) {
        Inventory vault = getVault(clan, index);
        player.openInventory(vault);
    }

    public static Inventory getVault(Clan clan, int index) {
        String key = clan.getRawName().toLowerCase() + "_" + index;
        if (vaults.containsKey(key)) return vaults.get(key);

        Inventory inv = Bukkit.createInventory(null, 27, "Clan Vault " + index + " - " + clan.getColoredName());
        loadVault(clan, index, inv);
        vaults.put(key, inv);
        return inv;
    }

    public static void saveVault(Clan clan) {
        saveVault(clan, 1);
    }

    public static void saveVault(Clan clan, int index) {
        String key = clan.getRawName().toLowerCase() + "_" + index;
        Inventory inv = vaults.get(key);
        if (inv == null) return;

        File file = getVaultFile(clan, index);
        FileConfiguration config = new YamlConfiguration();
        String serializedData = serializeInventory(inv);
        if (serializedData != null) {
            config.set("contents", serializedData);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadVault(Clan clan, int index, Inventory inventory) {
        File file = getVaultFile(clan, index);
        if (!file.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String serializedData = config.getString("contents");
        if (serializedData != null) {
            deserializeInventory(inventory, serializedData);
        }
    }

    public static String serializeInventory(Inventory inventory) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeInt(inventory.getSize());
            for (ItemStack item : inventory.getContents()) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deserializeInventory(Inventory inventory, String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            int size = dataInput.readInt();
            ItemStack[] items = new ItemStack[size];
            for (int i = 0; i < size; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            inventory.setContents(items);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static File getVaultFile(Clan clan, int index) {
        File folder = new File(ClassyClan.getInstance().getDataFolder(), "vaults");
        if (!folder.exists()) folder.mkdirs();
        return new File(folder, clan.getRawName().toLowerCase() + "_" + index + ".yml");
    }

    public double getPromotionCost(int level) {
        FileConfiguration config = ClassyClan.getInstance().getConfig();
        double base = config.getDouble("leveling.base-cost", 1000.0);
        double increment = config.getDouble("leveling.linear-increment", 500.0);
        return base + (increment * (level - 1));
    }
}
