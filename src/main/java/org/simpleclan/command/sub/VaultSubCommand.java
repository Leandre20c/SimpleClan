package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;
import org.simpleclan.vault.VaultManager;

public class VaultSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!ClanManager.get().isInClan(player.getUniqueId())) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan-vault"));
            return;
        }

        Clan clan = ClanManager.get().getClan(player.getUniqueId());
        player.openInventory(VaultManager.getVault(clan));
    }
}