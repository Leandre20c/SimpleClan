package org.simpleclan.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ClanMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();

        if (!event.getView().getTitle().equals("Clan Menu")) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        switch (event.getSlot()) {
            case 19 -> player.performCommand("clan leave");
            case 18 -> player.performCommand("clan disband");
            case 27 -> player.sendMessage("§eUse /clan rename <name> <prefix>");
        }

        player.closeInventory();
    }
}
