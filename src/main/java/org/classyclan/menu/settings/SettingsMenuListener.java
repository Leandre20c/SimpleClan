package org.classyclan.menu.settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.classyclan.ClassyClan;
import org.classyclan.menu.clan.ClanMenu;

public class SettingsMenuListener implements Listener {

    private final ClassyClan plugin;

    public SettingsMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private String getMenuTitle() {
        String raw = ClassyClan.getInstance()
                .getGuiManager()
                .getString("menus.settings.title");
        String expected = ChatColor.translateAlternateColorCodes('&', raw);

        return expected;
    }

    private boolean isSettingsMenu(InventoryView view) {
        return view.getTitle() != null && view.getTitle().equals(getMenuTitle());
    }

    /**
     * Bloque l'interaction, purge curseur, hotbar swap, et force la synchro client.
     */
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

    private void handleSettingsClick(Player player, InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 11 -> {
                // TODO: gérer submenu ranks si besoin
            }
            case 12 -> {
                player.closeInventory();
                player.sendMessage(ClassyClan.getMessages().get("usage-description"));
            }
            case 13 -> {
                player.closeInventory();
                player.sendMessage(ClassyClan.getMessages().get("usage-rename"));
            }
            case 14 -> {
                player.closeInventory();
                player.performCommand("clan disband");
            }
            case 15 -> {
                player.closeInventory();
                player.performCommand("clan color");
            }
            case 18 -> {
                player.closeInventory();
                ClanMenu.open(player);
            }
            case 26 -> {
                player.closeInventory();
            }
            default -> {
                // rien
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isSettingsMenu(view)) return;

        if (event.isShiftClick()) {
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            return;
        }

        // Bloque + purge + sync
        cancelAndSync(player, event);

        // On ne traite que les clics dans le top-inventory
        if (event.getClickedInventory() != view.getTopInventory()) return;

        handleSettingsClick(player, event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isSettingsMenu(view)) return;

        event.setCancelled(true);
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isSettingsMenu(view)) return;

        // Vide le menu côté serveur
        view.getTopInventory().clear();
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Sync 1 tick plus tard pour supprimer les ghost-items
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
