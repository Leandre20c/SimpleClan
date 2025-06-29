package org.classyclan.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.menu.clan.ClanMenu;
import org.classyclan.menu.members.MemberOptionsMenu;
import org.classyclan.menu.members.MembersMenuHolder;

import java.util.List;
import java.util.UUID;

public class MembersMenuListener implements Listener {

    private final ClassyClan plugin;

    public MembersMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private boolean isMembersMenu(InventoryView view) {
        return view.getTopInventory().getHolder() instanceof MembersMenuHolder;
    }

    private void cancelAndSync(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() != null && event.getClickedInventory() == player.getInventory()) {
            event.setCurrentItem(null);
        }
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        if (event.getHotbarButton() != -1) {
            player.getInventory().setItem(event.getHotbarButton(), null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isMembersMenu(view)) return;

        if (event.isShiftClick()) {
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            return;
        }

        cancelAndSync(player, event);
        if (event.isShiftClick()) return;
        if (event.getClickedInventory() != view.getTopInventory()) return;

        MembersMenuHolder holder = (MembersMenuHolder) view.getTopInventory().getHolder();
        Clan clan = holder.getClan();

        int slot = event.getRawSlot();
        // retour
        if (slot == 36) {
            player.closeInventory();
            ClanMenu.open(player);
            return;
        }
        // fermer
        if (slot == 44) {
            player.closeInventory();
            return;
        }

        // on récupère la liste déjà triée
        List<UUID> members = holder.getOrderedMembers();
        if (slot >= 0 && slot < members.size()) {
            UUID targetId = members.get(slot);
            if (!player.getUniqueId().equals(targetId)) {
                MemberOptionsMenu.open(player, clan, targetId);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!isMembersMenu(event.getView())) return;

        event.setCancelled(true);
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!isMembersMenu(event.getView())) return;

        event.getView().getTopInventory().clear();
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
