package org.classyclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanPermissionUtil;
import org.classyclan.ranks.ClanRank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MsgSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cUsage: /clan msg <message>");
            return;
        }

        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        ClanManager clanManager = ClassyClan.getInstance().getClanManager();
        Clan clan = clanManager.getClan(player.getUniqueId());
        ClanRank playerRank = clan.getRank(player.getUniqueId());

        if (!ClanPermissionUtil.checkPermission(player, clan, "clan.chat")) return;

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("clan_prefix", clan.getColoredPrefix());
        placeholders.put("clan_name", clan.getColoredName());
        placeholders.put("player_name", player.getName());
        placeholders.put("player_clan_rank", playerRank.getDisplayName());
        placeholders.put("message", message);

        String formattedMessage = ClassyClan.getMessages().get("clan-chat-format", placeholders);

        for (UUID uuid : clan.getMembers()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member != null && member.isOnline()) {
                member.sendMessage(formattedMessage);
            }
        }
    }
}
