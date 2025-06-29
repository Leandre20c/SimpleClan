package org.classyclan.menu.color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import java.util.List;

public class ColorMenuListener implements Listener {

    private final ClassyClan plugin;

    public ColorMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private String getMenuTitle() {
        String raw = plugin.getGuiManager().getString("menus.color.title");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    private boolean isColorMenu(InventoryView view) {
        String title = view.getTitle();
        return title != null && title.equals(getMenuTitle());
    }

    /**
     * Bloque toute interaction, purge curseur et hotbar swap, et force la sync client/serveur.
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

        // Forcer la resynchronisation client/serveur
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        InventoryView view = event.getView();
        if (!isColorMenu(view)) return;

        if (event.isShiftClick()) {
            if (event.getClickedInventory() == view.getTopInventory()) {
                event.setCancelled(true);
            }
            return;
        }

        // Bloque + purge + sync y compris pour shift-click
        cancelAndSync(player, event);
        if (event.isShiftClick()) return;

        // Ne traiter que les clics dans le top-inventory (le menu)
        if (event.getClickedInventory() != view.getTopInventory()) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        if (!clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        String displayName = meta.getDisplayName();
        List<String> lore = meta.getLore();
        if (displayName == null || displayName.length() < 2 || lore == null || lore.isEmpty()) return;

        // Statut de déblocage dans la première ligne du lore
        String statusLine = lore.get(0);
        String lockedMarker = ChatColor.translateAlternateColorCodes('&', "&cLocked");
        if (statusLine.contains(lockedMarker)) {
            player.sendMessage(ChatColor.RED + "You haven't unlocked this color yet!");
            return;
        }

        // Appliquer la nouvelle couleur
        Clan clan = plugin.getClanManager().getClan(player.getUniqueId());
        if (clan == null) return;

        String colorCode = displayName.substring(0, 2);
        clan.setColor(colorCode);
        player.sendMessage(ChatColor.GREEN + "You have changed your clan color to " + displayName + ChatColor.GREEN + "!");
        player.closeInventory();
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!isColorMenu(event.getView())) return;

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
        if (!isColorMenu(view)) return;

        // Vide l’inventaire du menu côté serveur
        view.getTopInventory().clear();

        // Supprime tout ghost-item sur le curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Resync un tick après pour purge totale
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
