package org.classyclan.menu.bases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.command.sub.BaseSubCommand;

public class BasesMenuListener implements Listener {

    private final ClassyClan plugin;

    public BasesMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private String getMenuTitle() {
        String raw = plugin.getGuiManager().getString("menus.bases.title");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    private boolean isBasesMenu(InventoryView view) {
        String title = view.getTitle();
        return title != null && title.equals(getMenuTitle());
    }

    /**
     * Bloque l'interaction, purge curseur et hotbar swap, et force la sync client/serveur.
     */
    private void cancelAndSync(Player player, InventoryClickEvent event) {
        event.setCancelled(true);

        // Supprime l’item cliqué dans l’inventaire joueur
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

        // Force la resynchronisation
        player.updateInventory();
    }

    private void handleBaseClick(Player player, InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.ENDER_PEARL) return;

        Clan clan = plugin.getClanManager().getClan(player.getUniqueId());
        if (clan == null) return;

        int slot = event.getRawSlot();
        int baseIndex = slot - 10; // slots 11–15 → index 1–5

        if (baseIndex >= 1 && baseIndex <= 5 && clan.getBase(baseIndex) != null) {
            BaseSubCommand.teleportWithCountdown(player, clan, baseIndex);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isBasesMenu(view)) return;

        if (event.isShiftClick()) {
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            return;
        }

        // Bloque + purge + sync, et protège contre shift-click
        cancelAndSync(player, event);
        if (event.isShiftClick()) return;

        // Ne traiter que les clics dans le top-inventory (le menu)
        if (event.getClickedInventory() != view.getTopInventory()) return;

        handleBaseClick(player, event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!isBasesMenu(event.getView())) return;

        event.setCancelled(true);
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!isBasesMenu(event.getView())) return;

        // Vide le menu côté serveur
        event.getView().getTopInventory().clear();

        // Supprime ghost-item sur le curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Resync un tick après pour purge totale
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
