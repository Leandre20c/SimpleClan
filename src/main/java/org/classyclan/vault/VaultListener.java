package org.classyclan.vault;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

public class VaultListener implements Listener {

    @EventHandler
    public void onVaultClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        String title = event.getView().getTitle();
        if (title == null || !title.matches("^Clan Vault \\d+ - .*")) return;

        Clan clan = ClassyClan.getInstance().getClanManager().getClan(player.getUniqueId());
        if (clan == null) {
            Bukkit.getLogger().warning("[SimpleClan] Clan non trouvé pour le joueur " + player.getName());
            return;
        }

        Bukkit.getScheduler().runTaskLater(ClassyClan.getInstance(), () -> {
            boolean stillOpen = Bukkit.getOnlinePlayers().stream()
                    .anyMatch(p -> {
                        String openTitle = p.getOpenInventory().getTitle();
                        return openTitle != null && openTitle.equals(title);
                    });
            if (!stillOpen) {
                Bukkit.getLogger().info("[SimpleClan] Sauvegarde du vault du clan " + clan.getRawName());
                VaultManager.saveVault(clan);
            }
        }, 2L);
    }
}
