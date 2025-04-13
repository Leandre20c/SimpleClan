package org.simpleclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MembersSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "members";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (!ClanManager.get().isInClan(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(playerId);
        UUID owner = clan.getOwner();

        List<String> members = clan.getMembers().stream()
                .map(uuid -> {
                    String name = Bukkit.getOfflinePlayer(uuid).getName();
                    if (uuid.equals(owner)) {
                        return "§6👑 " + name;
                    } else {
                        return "§7• " + name;
                    }
                })
                .sorted()
                .collect(Collectors.toList());

        player.sendMessage(SimpleClan.getMessages().get("members-header", Map.of(
                "name", clan.getName(),
                "prefix", clan.getPrefix()
        )));

        members.forEach(player::sendMessage);
    }
}
