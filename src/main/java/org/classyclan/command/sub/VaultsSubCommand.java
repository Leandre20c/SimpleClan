package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.menu.vaults.VaultMenu;

import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class VaultsSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "vaults";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = manager.getClan(playerId);
        if (!checkPermission(player, clan, "clan.vault.access")) return;

        VaultMenu.open(player);
    }
}
