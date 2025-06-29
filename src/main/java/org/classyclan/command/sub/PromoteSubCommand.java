package org.classyclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanRank;

import java.util.Map;
import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.*;

public class PromoteSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "promote";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ClassyClan.getMessages().get("usage-promote"));
            return;
        }

        ClanManager manager = ClassyClan.getInstance().getClanManager();
        UUID playerId = player.getUniqueId();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = manager.getClan(playerId);

        if (!checkPermission(player, clan, "clan.rank.promote")) return;

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !clan.isMember(target.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("player-not-in-your-clan"));
            return;
        }

        UUID targetId = target.getUniqueId();
        if (!checkNotSelf(player, targetId, "promote")) return;

        ClanRank executorRank = clan.getRank(playerId);
        ClanRank targetRank = clan.getRank(targetId);

        ClanRank nextRank = switch (targetRank) {
            case MEMBER -> ClanRank.TRUSTED;
            case TRUSTED -> ClanRank.SOLDIER;
            case SOLDIER -> ClanRank.COMMANDER;
            case COMMANDER, LEADER -> ClanRank.LEADER;
        };

        if (!checkPromoteHierarchy(player, executorRank, nextRank)) return;

        clan.setRank(targetId, nextRank);

        sendFormatted(player, "player-promoted", Map.of(
                "player", target.getName(),
                "rank", nextRank.getDisplayName()
        ));
    }
}
