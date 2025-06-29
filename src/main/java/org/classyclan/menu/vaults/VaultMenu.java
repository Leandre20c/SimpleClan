package org.classyclan.menu.vaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.gui.GuiManager;

import java.util.List;

public class VaultMenu {

    public static void open(Player player) {
        Clan clan = ClassyClan.getInstance().getClanManager().getClan(player.getUniqueId());
        if (clan == null) return;

        FileConfiguration config = ClassyClan.getInstance().getConfig();
        int maxVaults = 1 + clan.getExtraVaults();

        GuiManager gui = ClassyClan.getInstance().getGuiManager();
        gui.setContextClan(clan);

        String rawTitle = gui.getString("menus.vaults.title");
        String title    = ChatColor.translateAlternateColorCodes('&', rawTitle);

        Inventory menu = Bukkit.createInventory(null, 3 * 9, title);

        // Filler
        for (int i = 0; i < 27; i++) {
            ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            menu.setItem(i, filler);
        }

        // Vaults de 1 à 5, centrés (slots 11 à 15)
        for (int i = 1; i <= 5; i++) {
            ItemStack item;
            if (i <= maxVaults) {
                item = new ItemStack(Material.CHEST);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§aVault " + i);
                meta.setLore(List.of("§7Click to open vault " + i));
                item.setItemMeta(meta);
            } else {
                item = new ItemStack(Material.BARRIER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§cVault " + i + " unavailable");
                meta.setLore(List.of("§7Unlock more vaults by leveling up."));
                item.setItemMeta(meta);
            }
            menu.setItem(10 + i, item); // slots 11 à 15
        }

        player.openInventory(menu);
    }
}
