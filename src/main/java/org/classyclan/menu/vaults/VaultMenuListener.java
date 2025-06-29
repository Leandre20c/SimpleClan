package org.classyclan.menu.vaults;

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
import org.classyclan.vault.VaultManager;

public class VaultMenuListener implements Listener {

    private final ClassyClan plugin;

    public VaultMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private String getMenuTitle() {
        String raw = plugin.getGuiManager().getString("menus.vaults.title");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    private boolean isVaultMenu(InventoryView view) {
        String title = view.getTitle();
        return title != null && title.equals(getMenuTitle());
    }

    /**
     * Bloque l'interaction, purge curseur, hotbar swap, et force la synchro client.
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

        // Supprime tout swap hotbar (alt+num)
        if (event.getHotbarButton() != -1) {
            player.getInventory().setItem(event.getHotbarButton(), null);
        }

        // Forcer la sync entre serveur et client pour virer tout ghost-item
        player.updateInventory();
    }

    private void handleVaultClick(Player player, InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.CHEST) return;

        Clan clan = plugin.getClanManager().getClan(player.getUniqueId());
        if (clan == null) return;

        int slot = event.getRawSlot();
        int vaultIndex = slot - 10; // slots 11 à 15 => index 1 à 5
        if (vaultIndex >= 1 && vaultIndex <= 5) {
            player.closeInventory();
            VaultManager.get().openVault(player, clan, vaultIndex);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isVaultMenu(view)) return;

        if (event.isShiftClick()) {
            // on laisse tomber sans bloquer le menu, mais on purge ghost-items
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            cancelAndSync(player, event);
            return;
        }

        // Bloque + purge + sync
        cancelAndSync(player, event);

        // On ne traite que les clics dans le menu (top inventory)
        if (event.getClickedInventory() != view.getTopInventory()) return;

        handleVaultClick(player, event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isVaultMenu(view)) return;

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
        if (!isVaultMenu(view)) return;

        // Vide le menu côté serveur
        view.getTopInventory().clear();

        // Supprime les ghost-items sur le curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Resync un tick après pour purge totale
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
