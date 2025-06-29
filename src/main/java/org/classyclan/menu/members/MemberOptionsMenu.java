// src/main/java/org/simpleclan/menu/MemberOptionsMenu.java
package org.classyclan.menu.members;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.gui.GuiManager;

import java.util.Map;
import java.util.UUID;

public class MemberOptionsMenu {

    /**
     * Ouvre le menu d’options pour un membre.
     * @param viewer   le joueur qui clique
     * @param clan     le clan du membre
     * @param memberId l’UUID du membre ciblé
     */
    public static void open(Player viewer, Clan clan, UUID memberId) {
        GuiManager gui = ClassyClan.getInstance().getGuiManager();
        String memberName = Bukkit.getOfflinePlayer(memberId).getName();

        // 1) Titre configuré avec {memberName}
        String rawTitle = gui.getString("menus.memberOptions.title");
        String title = ChatColor.translateAlternateColorCodes('&',
                rawTitle.replace("{memberName}", memberName)
        );

        // 2) Holder + inventaire 2 lignes = 18 slots
        MemberOptionsHolder holder = new MemberOptionsHolder(clan, memberId);
        Inventory inv = Bukkit.createInventory(holder, 2 * 9, title);
        holder.setInventory(inv);

        // 3) Filler sur tous les slots
        ItemStack filler = gui.getGuiItem("common.filler", Map.of());
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }

        // 4) Actions principales
        Map<String,String> placeholders = Map.of("memberName", memberName);

        // case 0 : promote
        inv.setItem(0, gui.getGuiItem("memberOptions.promote", placeholders));

        // case 1 : demote
        inv.setItem(1, gui.getGuiItem("memberOptions.demote", placeholders));

        // case 2 : Kick
        inv.setItem(2, gui.getGuiItem("memberOptions.kick", placeholders));

        // case 4 : View stats (tête + stats)
        inv.setItem(4, gui.getGuiItem("memberOptions.stats", placeholders));

        // case 8 : Set leader
        inv.setItem(8, gui.getGuiItem("memberOptions.setLeader", placeholders));

        // 13
        inv.setItem(13, gui.getGuiItem("memberOptions.back", placeholders));

        // 5) Open
        viewer.openInventory(inv);
    }
}
