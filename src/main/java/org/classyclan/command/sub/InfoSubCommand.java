package org.classyclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanRank;

import java.util.UUID;

public class InfoSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager clanManager = ClassyClan.getInstance().getClanManager();

        UUID playerId = player.getUniqueId();
        if (!clanManager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = clanManager.getClan(playerId);
        ClanRank rank = clan.getRank(playerId);
        String leaderName = Bukkit.getOfflinePlayer(clan.getOwner()).getName();
        int memberCount = clan.getMembers().size();
        double bank = clan.getBank();

        player.sendMessage("§8§m-------------------------------");
        player.sendMessage("§bClan Info: §f" + clan.getColoredName() + " §7[" + clan.getColoredPrefix() + "§7]");
        player.sendMessage("§7Level: §b" + clan.getLevel());
        player.sendMessage("§7Your Rank: §f" + rank.getDisplayName());
        player.sendMessage("§7Leader: §f" + leaderName);
        player.sendMessage("§7Members: §f" + memberCount);
        player.sendMessage("§7Bank: §f$" + String.format("%.2f", bank));
        player.sendMessage("§8§m-------------------------------");
    }
}
