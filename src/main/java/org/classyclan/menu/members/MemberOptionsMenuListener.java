package org.classyclan.menu.members;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.classyclan.ClassyClan;

import java.util.UUID;

public class MemberOptionsMenuListener implements Listener {

    private final ClassyClan plugin;

    public MemberOptionsMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private boolean isOptionsMenu(InventoryView view) {
        return view.getTopInventory().getHolder() instanceof MemberOptionsHolder;
    }

    /**
     * Bloque et purges toute tentative de duplication (curseur, hotbar swap, ghost-items).
     */
    private void cancelAndSync(Player player, InventoryClickEvent event) {
        event.setCancelled(true);

        // Supprime l’item cliqué s’il est dans l’inventaire joueur
        if (event.getCurrentItem() != null && event.getClickedInventory() == player.getInventory()) {
            event.setCurrentItem(null);
        }

        // Supprime l’item sur le curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Supprime tout hotbar swap (alt+num)
        if (event.getHotbarButton() != -1) {
            player.getInventory().setItem(event.getHotbarButton(), null);
        }

        // Force la synchronisation client/serveur
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isOptionsMenu(view)) return;

        if (event.isShiftClick()) {
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            return;
        }

        // Bloque + purge + sync, même pour shift-click
        cancelAndSync(player, event);
        if (event.isShiftClick()) return;

        // Ne traite que les clics dans le menu lui-même
        if (event.getClickedInventory() != view.getTopInventory()) return;

        MemberOptionsHolder holder = (MemberOptionsHolder) view.getTopInventory().getHolder();
        UUID targetId = holder.getMemberId();
        String targetName = Bukkit.getOfflinePlayer(targetId).getName();

        switch (event.getRawSlot()) {
            case 0 -> player.performCommand("clan promote " + targetName);
            case 1 -> player.performCommand("clan demote " + targetName);
            case 2 -> {
                player.performCommand("clan kick " + targetName);
                player.closeInventory();
                player.performCommand("clan members");
            }
            case 4 -> player.performCommand("c player info " + targetName);
            case 8 -> player.performCommand("clan setleader " + targetName);
            case 13 -> {
                player.closeInventory();
                player.performCommand("clan members");
            }
            default -> {
                // slots ignorés
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof MemberOptionsHolder)) return;

        event.setCancelled(true);
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof MemberOptionsHolder)) return;

        // Vide le menu côté serveur
        event.getInventory().clear();

        // Supprime ghost-item sur curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Resync un tick après pour purge totale
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
