package org.classyclan.menu.color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.gui.GuiManager;

import java.util.*;

public class ColorMenu {

    public static void open(Player player) {
        Clan clan = ClassyClan.getInstance().getClanManager().getClan(player.getUniqueId());

        FileConfiguration config = ClassyClan.getInstance().getConfig();

        GuiManager gui = ClassyClan.getInstance().getGuiManager();
        String rawTitle = gui.getString("menus.color.title");
        String title    = ChatColor.translateAlternateColorCodes('&', rawTitle);
        Inventory menu = Bukkit.createInventory(null, 54, title);

        // Filler
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int slot = 0; slot < 54; slot++) {
            menu.setItem(slot, filler);
        }

        // Map pour stocker couleur -> niveau de déblocage
        Map<String, Integer> colorToLevel = new LinkedHashMap<>();

        if (config.isConfigurationSection("leveling.rewards")) {
            for (String levelStr : config.getConfigurationSection("leveling.rewards").getKeys(false)) {
                int level = Integer.parseInt(levelStr);

                if (config.isString("leveling.rewards." + levelStr + ".clan-color")) {
                    String colorCode = config.getString("leveling.rewards." + levelStr + ".clan-color");

                    if (colorCode != null && !colorToLevel.containsKey(colorCode)) {
                        colorToLevel.put(colorCode, level);
                    }
                }
            }
        }

        // Centrage
        int totalColors = colorToLevel.size();
        int startIndex = (9 - totalColors) / 2 + 9;

        for (Map.Entry<String, Integer> entry : colorToLevel.entrySet()) {
            String colorCode = entry.getKey();
            int requiredLevel = entry.getValue();

            ChatColor chatColor = ChatColor.getByChar(colorCode.replace("§", ""));
            if (chatColor == null) continue;

            Material dyeMaterial = ColorMenuUtils.getMaterialForColor(chatColor);
            if (dyeMaterial == null) continue;

            ItemStack item = new ItemStack(dyeMaterial);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(colorCode + chatColor.name());

            List<String> lore = new ArrayList<>();
            boolean unlocked = clan.getLevel() >= requiredLevel;

            lore.add("§7Status: " + (unlocked ? "§aUnlocked" : "§cLocked"));
            lore.add("§7Required Level: §e" + requiredLevel);

            meta.setLore(lore);

            if (unlocked) {
                meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setItemMeta(meta);
            menu.setItem(startIndex++, item);
        }

        player.openInventory(menu);
    }
}
