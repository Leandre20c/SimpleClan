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

public class DemoteSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "demote";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ClassyClan.getMessages().get("usage-demote"));
            return;
        }

        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = manager.getClan(playerId);

        if (!checkPermission(player, clan, "clan.rank.demote")) return;

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !clan.isMember(target.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("player-not-in-your-clan"));
            return;
        }

        UUID targetId = target.getUniqueId();
        if (!checkNotSelf(player, targetId, "demote")) return;

        ClanRank executorRank = clan.getRank(playerId);
        ClanRank targetRank = clan.getRank(targetId);
        if (!checkRankHierarchy(player, executorRank, targetRank)) return;

        ClanRank nextRank = switch (targetRank) {
            case LEADER -> ClanRank.COMMANDER;
            case COMMANDER -> ClanRank.SOLDIER;
            case SOLDIER -> ClanRank.TRUSTED;
            case TRUSTED, MEMBER -> ClanRank.MEMBER;
        };

        clan.setRank(targetId, nextRank);

        sendFormatted(player, "player-demoted", Map.of(
                "player", target.getName(),
                "rank", nextRank.getDisplayName()
        ));
    }
}
