package org.classyclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import java.util.Map;
import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class KickSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ClassyClan.getMessages().get("usage-kick"));
            return;
        }

        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = manager.getClan(playerId);

        if (!checkPermission(player, clan, "clan.kick")) return;

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !clan.isMember(target.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("player-not-in-your-clan"));
            return;
        }

        if (target.getUniqueId().equals(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("cant-kick-yourself"));
            return;
        }

        clan.removeMember(target.getUniqueId());
        manager.removePlayer(target.getUniqueId());

        player.sendMessage(ClassyClan.getMessages().get("kicked-from-clan", Map.of(
                "player", target.getName(),
                "name", clan.getColoredName()
        )));

        target.sendMessage(ClassyClan.getMessages().get("you-were-kicked", Map.of(
                "name", clan.getColoredName()
        )));
    }
}
