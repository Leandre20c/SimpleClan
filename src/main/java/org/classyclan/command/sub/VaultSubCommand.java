package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.vault.VaultManager;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class VaultSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager clanManager = ClassyClan.getInstance().getClanManager();

        if (!clanManager.isInClan(player.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan-vault"));
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (!checkPermission(player, clan, "clan.vault.access")) return;

        int index = 1;
        if (args.length >= 2) {
            try {
                index = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid vault number.");
                return;
            }
        }

        int maxVaults = 1 + clan.getExtraVaults();
        if (index < 1 || index > maxVaults) {
            player.sendMessage("§cVault " + index + " is unavailable. Max unlocked: " + maxVaults);
            return;
        }

        VaultManager.get().openVault(player, clan, index);
    }
}
