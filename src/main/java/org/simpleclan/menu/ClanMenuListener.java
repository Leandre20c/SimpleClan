package org.simpleclan.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ClanMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();

        if (!event.getView().getTitle().equals("§b§lClan Menu")) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        switch (event.getSlot()) {
            case 10 -> player.performCommand("clan members");
            case 12 -> player.performCommand("clan setbase");
            case 14 -> player.performCommand("clan base");
            case 16 -> player.performCommand("clan vault");
            case 18 -> player.performCommand("clan disband");
            case 19 -> player.performCommand("clan leave");
            case 20 -> player.sendMessage("§eUse /clan rename <name> <prefix>");
        }

        player.closeInventory();
    }
}
