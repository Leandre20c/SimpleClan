package org.classyclan.menu.allClans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.menu.clan.ClanMenu;

import java.util.List;
import java.util.UUID;

public class AllClansMenuListener implements Listener {

    private final ClassyClan plugin;

    public AllClansMenuListener(ClassyClan plugin) {
        this.plugin = plugin;
    }

    private String getMenuTitle() {
        String raw = plugin.getGuiManager().getString("menus.allClans.title");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    private boolean isAllClansMenu(InventoryView view) {
        String title = view.getTitle();
        return title != null && title.equals(getMenuTitle());
    }

    /**
     * Bloque l'interaction, purge curseur et hotbar swap, et force la synchronisation client.
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

        // Supprime tout hotbar swap (alt+num)
        if (event.getHotbarButton() != -1) {
            player.getInventory().setItem(event.getHotbarButton(), null);
        }

        // Force la resynchronisation client-serveur
        player.updateInventory();
    }

    private void handleAllClansClick(Player player, InventoryClickEvent e) {
        ClanManager cm = plugin.getClanManager();
        UUID uuid = player.getUniqueId();

        int slot = e.getRawSlot();
        int page = AllClansMenu.pageMap.getOrDefault(uuid, 0);
        SortType sort = AllClansMenu.sortMap.getOrDefault(uuid, SortType.LEVEL);
        FilterType filter = AllClansMenu.filterMap.getOrDefault(uuid, FilterType.BOTH);

        List<Clan> filtered = AllClansMenu.sortClans(
                cm.getAllClans().stream()
                        .filter(c -> switch (filter) {
                            case OPEN -> c.isClanOpen();
                            case CLOSED -> !c.isClanOpen();
                            default -> true;
                        })
                        .toList(),
                sort
        );

        int perPage = 45;
        int maxPage = Math.max(0, (filtered.size() - 1) / perPage);

        switch (slot) {
            case 45 -> { // filtre
                FilterType next = switch (filter) {
                    case BOTH -> FilterType.OPEN;
                    case OPEN -> FilterType.CLOSED;
                    default -> FilterType.BOTH;
                };
                AllClansMenu.open(player, 0, sort, next);
            }
            case 46 -> { // tri
                SortType next = switch (sort) {
                    case LEVEL   -> SortType.MEMBERS;
                    case MEMBERS -> SortType.BANK;
                    default      -> SortType.LEVEL;
                };
                AllClansMenu.open(player, 0, next, filter);
            }
            case 48 -> { // précédent
                if (page > 0) AllClansMenu.open(player, page - 1, sort, filter);
            }
            case 49 -> { // gérer/créer clan
                player.closeInventory();
                if (cm.isInClan(uuid)) {
                    player.performCommand("clan menu");
                } else {
                    player.performCommand("clan create");
                }
            }
            case 50 -> { // suivant
                if (page < maxPage) AllClansMenu.open(player, page + 1, sort, filter);
            }
            default -> {
                int index = page * perPage + slot;
                if (slot < perPage && index < filtered.size()) {
                    Clan clicked = filtered.get(index);
                    player.closeInventory();
                    if (clicked.isMember(uuid)) {
                        player.performCommand("clan menu");
                    } else {
                        ClanMenu.openForClan(player, clicked);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        InventoryView view = e.getView();
        if (!isAllClansMenu(view)) return;

        if (e.isShiftClick()) {
            if (e.getClickedInventory() == view.getTopInventory()) {
                e.setCancelled(true);
            }
            return;
        }

        // Bloque + purge + sync, même pour shift-click
        cancelAndSync(player, e);
        if (e.isShiftClick()) return;

        // Ne traiter que les clics dans le menu (top inventory)
        if (e.getClickedInventory() != view.getTopInventory()) return;

        handleAllClansClick(player, e);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (!isAllClansMenu(e.getView())) return;

        e.setCancelled(true);
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }
        player.updateInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if (!isAllClansMenu(e.getView())) return;

        // Vide le menu côté serveur
        e.getView().getTopInventory().clear();

        // Supprime ghost-item sur le curseur
        if (!player.getItemOnCursor().getType().isAir()) {
            player.setItemOnCursor(null);
        }

        // Resync un tick plus tard
        Bukkit.getScheduler().runTask(plugin, player::updateInventory);
    }
}
