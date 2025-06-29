package org.classyclan.menu.clan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.classyclan.ClassyClan;
import org.classyclan.menu.allClans.AllClansMenu;

public class ClanMenuListener implements Listener {

    private final JavaPlugin plugin;

    public ClanMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private boolean isCustomHolder(Object holder) {
        return holder instanceof ClanMenuHolder || holder instanceof ClanViewHolder;
    }

    private boolean isCustomMenu(InventoryView view) {
        return isCustomHolder(view.getTopInventory().getHolder());
    }

    private void cancelAndSync(Player player, InventoryClickEvent event) {
        // Bloque toute interaction
        event.setCancelled(true);

        // Supprime l’item cliqué s’il est dans l’inventaire joueur
        if (event.getCurrentItem() != null && event.getClickedInventory() == player.getInventory()) {
            event.setCurrentItem(null);
        }

        // Supprime l’item sur le curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Supprime tout swap hotbar (alt+num)
        if (event.getHotbarButton() != -1) {
            player.getInventory().setItem(event.getHotbarButton(), null);
        }

        // Forcer la sync entre serveur/client pour virer tout ghost
        player.updateInventory();
    }

    private void handleMenuActions(Player player, InventoryClickEvent event) {
        Object holder = event.getView().getTopInventory().getHolder();

        if (holder instanceof ClanMenuHolder) {
            switch (event.getRawSlot()) {
                case 10 -> player.performCommand("clan leave");
                case 11 -> player.performCommand("clan settings");
                case 13 -> player.performCommand("clan members");
                case 15 -> player.performCommand("clan level");
                case 16 -> player.performCommand("clan bank");
                case 24 -> player.performCommand("clan vaults");
                case 25 -> player.performCommand("clan bases");
                default -> {}
            }
        } else if (holder instanceof ClanViewHolder) {
            switch (event.getRawSlot()) {
                case 31 -> {
                    player.closeInventory();
                    AllClansMenu.open(player);
                }
                case 33 -> player.closeInventory();
                default -> {}
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isCustomMenu(view)) return;

        if (event.isShiftClick()) {
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            return;
        }

        // Bloque + nettoyage + sync
        cancelAndSync(player, event);

        // N’agir que si on clique dans le menu custom
        if (event.getClickedInventory() != view.getTopInventory()) return;

        handleMenuActions(player, event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!isCustomHolder(event.getInventory().getHolder())) return;

        event.setCancelled(true);
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Sync immédiat
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!isCustomHolder(event.getInventory().getHolder())) return;

        // Vide le menu côté serveur
        event.getInventory().clear();
        // Supprime le ghost item sur curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Ré-synchronisation 1 tick plus tard pour purge totale
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
