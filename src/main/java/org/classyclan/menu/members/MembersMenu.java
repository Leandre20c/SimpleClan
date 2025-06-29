package org.classyclan.menu.members;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.gui.GuiManager;

import java.util.*;

public class MembersMenu {
    private static final int ROWS = 5;

    public static void open(Player viewer, Clan clan) {
        GuiManager gui = ClassyClan.getInstance().getGuiManager();

        String rawTitle = gui.getString("menus.members.title");
        String title = ChatColor.translateAlternateColorCodes('&',
                rawTitle.replace("{clanName}", clan.getColoredName())
        );

        // ➊ creation holder + inventory
        MembersMenuHolder holder = new MembersMenuHolder(clan);
        Inventory inv = Bukkit.createInventory(holder, ROWS * 9, title);
        holder.setInventory(inv);

        // ➋ calcul et stockage de la liste triée
        List<UUID> members = new ArrayList<>(clan.getMembers());
        members.sort(Comparator.comparingInt(id -> clan.getRank(id).ordinal()));
        Collections.reverse(members); // leader en premier
        holder.setOrderedMembers(members);

        // ➌ filler
        ItemStack filler = gui.getGuiItem("common.filler", Map.of());
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }

        // ➍ placement des têtes dans l’ordre trié
        int slot = 0;
        for (UUID memberId : members) {
            if (slot >= 4 * 9) break;
            String playerName = Bukkit.getOfflinePlayer(memberId).getName();
            String rankName   = clan.getRank(memberId).getDisplayName();

            ItemStack head = gui.getGuiItem(
                    "members.memberHead",
                    Map.of(
                            "memberName", playerName,
                            "memberRank", rankName,
                            "skullOwner",  playerName
                    )
            );
            inv.setItem(slot++, head);
        }

        // ➎ boutons navigation
        inv.setItem(36, gui.getGuiItem("members.back",  Map.of()));
        inv.setItem(44, gui.getGuiItem("members.close", Map.of()));

        viewer.openInventory(inv);
    }
}
