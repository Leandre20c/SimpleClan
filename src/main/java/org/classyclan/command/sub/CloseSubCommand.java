package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class CloseSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "close";
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

        if (!checkPermission(player, clan, "clan.settings.setclosed")) return;

        if (!clan.isClanOpen())
        {
            player.sendMessage(ClassyClan.getMessages().get("clan-already-closed"));
            return;
        }

        clan.setClanClosed();

        player.sendMessage(ClassyClan.getMessages().get("clan-closed"));
    }
}
