// src/main/java/org/simpleclan/menu/SettingsMenu.java
package org.classyclan.menu.settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.gui.GuiManager;
import org.classyclan.ranks.ClanRank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsMenu {

    /**
     * Ouvre le menu « Clan Settings » (3×9 = 27 slots), configuré via guis.yml.
     */
    public static void open(Player player) {
        GuiManager gui = ClassyClan.getInstance().getGuiManager();
        ClanManager cm = ClassyClan.getInstance().getClanManager();
        Clan clan = cm.getClan(player.getUniqueId());

        String rawTitle = gui.getString("menus.settings.title");
        String title = ChatColor.translateAlternateColorCodes('&', rawTitle);

        Inventory menu = Bukkit.createInventory(null, 3*9, title);

        var filler = gui.getGuiItem("common.filler", Map.of());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, filler);
        }

        List<String> lore = new ArrayList<>();
        ClanRank[] ranks = ClanRank.values();
        for (int i = ranks.length - 1; i >= 0; i--) {
            ClanRank rank = ranks[i];
            lore.add(rank.getDisplayName());

            List<String> description = rank.getDescription();
            if (description != null && !description.isEmpty()) {
                lore.addAll(description);
            }

            lore.add("");
        }
        if (!lore.isEmpty()) {
            lore.remove(lore.size() - 1);
        }

        ItemStack ranksItem = new ItemStack(Material.SHIELD);
        var meta = ranksItem.getItemMeta();
        meta.setDisplayName("§cClan Ranks");
        meta.setLore(lore);
        ranksItem.setItemMeta(meta);
        menu.setItem(11, ranksItem);

        menu.setItem(13, gui.getGuiItem(
                "settings.rename",
                Map.of()
        ));

        // Description, placeholder {description}
        String desc = clan.getDescription().isEmpty() ? "Aucune" : clan.getDescription();
        menu.setItem(12, gui.getGuiItem(
                "settings.description",
                Map.of("description", desc)
        ));

        menu.setItem(14, gui.getGuiItem(
                "settings.disband",
                Map.of()
        ));

        menu.setItem(15, gui.getGuiItem(
                "settings.color",
                Map.of()
        ));

        menu.setItem(18, gui.getGuiItem("settings.back", Map.of()));
        menu.setItem(26, gui.getGuiItem("settings.close", Map.of()));

        // 9) Ouvre l’inventaire
        player.openInventory(menu);
    }
}
