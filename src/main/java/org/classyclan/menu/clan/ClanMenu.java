// src/main/java/org/simpleclan/menu/ClanMenu.java
package org.classyclan.menu.clan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.gui.GuiManager;

import java.util.Map;
import java.util.UUID;

public class ClanMenu {

    public static void open(Player player) {
        GuiManager gui = ClassyClan.getInstance().getGuiManager();
        ClanManager cm = ClassyClan.getInstance().getClanManager();
        Clan clan = cm.getClan(player.getUniqueId());
        if (clan == null) return;

        gui.setContextClan(clan);

        // --- 0) Préparation du titre
        String rawTitle = gui.getString("menus.clanMain.title");
        String title = ChatColor.translateAlternateColorCodes('&', rawTitle)
                .replace("{clanName}",   clan.getColoredName())
                .replace("{clanPrefix}", clan.getColoredPrefix());

        // --- 1) Création du holder et de l’inventaire 4×9
        ClanMenuHolder holder = new ClanMenuHolder(clan);
        Inventory menu = Bukkit.createInventory(holder, 4*9, title);
        holder.setInventory(menu);

        // --- 2) Filler 0–35
        ItemStack filler = gui.getGuiItem("common.filler", Map.of());
        for (int i = 0; i < 4*9; i++) {
            menu.setItem(i, filler);
        }

        // Leave
        menu.setItem(10, gui.getGuiItem("clanMain.leave",  Map.of()));

        // Settings
        menu.setItem(11, gui.getGuiItem("clanMain.settings", Map.of()));


        // Banner showing members
        int current = clan.getMembers().size();
        int max     = ClassyClan.getInstance().getConfig()
                .getInt("max-members-per-clan", 0);
        ItemStack item = gui.getGuiItem(
                "clanMain.viewMembers",
                Map.of("current", String.valueOf(current),
                        "max",     max == 0 ? "∞" : String.valueOf(max)));

        item.getItemMeta().setHideTooltip(true);

        menu.setItem(13, item);

        // --- 4) Level (slot 4)
        menu.setItem(15, gui.getGuiItem(
                "clanMain.level",
                Map.of("level", String.valueOf(clan.getLevel()))
        ));

        // Bank
        menu.setItem(16, gui.getGuiItem(
                "clanMain.bank",
                Map.of("bank", String.format("%.2f", clan.getBank()))
        ));

        // Player stats
        menu.setItem(22, gui.getGuiItem(
                "clanMain.playerHead",
                Map.of(
                        "playerName", player.getName(),
                        "playerRank", clan.getRank(player.getUniqueId()).getDisplayName(),
                        "skullOwner", player.getName()
                )
        ));

        // Vaults
        menu.setItem(24, gui.getGuiItem("clanMain.vaultsButton", Map.of()));

        // Bases
        menu.setItem(25, gui.getGuiItem("clanMain.basesButton", Map.of()));

        player.openInventory(menu);
    }


    /** Ouvre le menu de présentation d’un autre clan (4×9 = 36 slots). */
    public static void openForClan(Player viewer, Clan clan) {
        GuiManager gui = ClassyClan.getInstance().getGuiManager();

        gui.setContextClan(clan);

        // Titre configuré
        String rawTitle = gui.getString("menus.clanView.title");
        String title = ChatColor.translateAlternateColorCodes('&', rawTitle)
                .replace("{clanName}", clan.getRawName());

        // Utilise le holder
        ClanViewHolder holder = new ClanViewHolder(clan);
        Inventory menu = Bukkit.createInventory(holder, 4 * 9, title);
        holder.setInventory(menu);

        // Remplissage classique :
        // 1) Bannière slot 13
        menu.setItem(13, gui.getGuiItem("clanView.banner", Map.of(
                "clanName",    clan.getRawName(),
                "clanPrefix",  clan.getColoredPrefix(),
                "description", clan.getDescription().isEmpty() ? "Aucune" : clan.getDescription(),
                "level",       String.valueOf(clan.getLevel()),
                "memberCount", String.valueOf(clan.getMembers().size()),
                "bank",        String.format("%.2f", clan.getBank())
        )));

        // 2) Membres slots 18–26
        int slot = 18;
        for (UUID memberId : clan.getMembers()) {
            if (slot > 26) break;
            menu.setItem(slot++, gui.getGuiItem("clanView.memberHead", Map.of(
                    "memberName", Bukkit.getOfflinePlayer(memberId).getName(),
                    "memberRank", clan.getRank(memberId).getDisplayName()
            )));
        }

        // 3) Back et Close
        menu.setItem(31, gui.getGuiItem("common.back",  Map.of()));
        menu.setItem(33, gui.getGuiItem("common.close", Map.of()));

        // On ouvre **une fois**
        viewer.openInventory(menu);
    }

}
