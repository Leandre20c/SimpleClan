package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import java.util.Map;
import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class DisbandSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "disband";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (!ClassyClan.getInstance().getClanManager().isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClassyClan.getInstance().getClanManager().getClan(playerId);

        if (!checkPermission(player, clan, "clan.disband")) return;

        ClassyClan.getInstance().getClanManager().disbandClan(clan.getRawName());

        player.sendMessage(ClassyClan.getMessages().get("clan-disbanded", Map.of(
                "name", clan.getRawName()
        )));
    }
}
