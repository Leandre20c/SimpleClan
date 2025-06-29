// 📁 BasesMenu.java
package org.classyclan.menu.bases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.gui.GuiManager;

import java.util.List;

public class BasesMenu {

    public static void open(Player player) {
        Clan clan = ClassyClan.getInstance().getClanManager().getClan(player.getUniqueId());
        if (clan == null) return;

        int maxBases = 1 + clan.getExtraHomes();

        GuiManager gui = ClassyClan.getInstance().getGuiManager();
        gui.setContextClan(clan);

        String rawTitle = gui.getString("menus.bases.title");
        String title    = ChatColor.translateAlternateColorCodes('&', rawTitle);

        Inventory menu = Bukkit.createInventory(null, 3 * 9, title);

        for (int i = 0; i < 27; i++) {
            ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta fillerMeta = filler.getItemMeta();
            fillerMeta.setDisplayName(" ");
            filler.setItemMeta(fillerMeta);
            menu.setItem(i, filler);
        }

        for (int i = 1; i <= 5; i++) {
            ItemStack item;
            if (i <= maxBases && clan.getBase(i) != null) {
                item = new ItemStack(Material.ENDER_PEARL);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§aBase " + i);
                meta.setLore(List.of("§7Click to teleport to base " + i));
                item.setItemMeta(meta);
            } else {
                item = new ItemStack(Material.BARRIER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§cBase " + i + " unavailable");
                if (i > maxBases) {
                    meta.setLore(List.of("§7Unlock more base slots by leveling up."));
                } else {
                    meta.setLore(List.of("§7Base not set."));
                }
                item.setItemMeta(meta);
            }
            menu.setItem(10 + i, item); // places base items at slot 10 to 14
        }

        player.openInventory(menu);
    }
}
