package org.simpleclan.vault;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

public class VaultListener implements Listener {

    @EventHandler
    public void onVaultClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        if (!event.getView().getTitle().startsWith("Clan Vault - ")) return;

        Clan clan = ClanManager.get().getClan(player.getUniqueId());
        if (clan == null) return;

        Bukkit.getScheduler().runTaskLater(SimpleClan.getInstance(), () -> {
            boolean stillOpen = Bukkit.getOnlinePlayers().stream()
                    .anyMatch(p -> p.getOpenInventory().getTitle().equals("Clan Vault - " + clan.getName()));
            if (!stillOpen) {
                VaultManager.saveVault(clan);
            }
        }, 2L);
    }
}
