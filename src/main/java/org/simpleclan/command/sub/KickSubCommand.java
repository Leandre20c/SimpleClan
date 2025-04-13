package org.simpleclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;
import java.util.UUID;

public class KickSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SimpleClan.getMessages().get("usage-kick"));
            return;
        }

        UUID senderId = player.getUniqueId();

        if (!ClanManager.get().isInClan(senderId)) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(senderId);

        if (!clan.getOwner().equals(senderId)) {
            player.sendMessage(SimpleClan.getMessages().get("action-requires-owner"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(SimpleClan.getMessages().get("player-not-found"));
            return;
        }

        UUID targetId = target.getUniqueId();

        if (!clan.isMember(targetId)) {
            player.sendMessage(SimpleClan.getMessages().get("player-not-in-your-clan"));
            return;
        }

        if (senderId.equals(targetId)) {
            player.sendMessage(SimpleClan.getMessages().get("cant-kick-yourself"));
            return;
        }

        clan.removeMember(targetId);
        ClanManager.get().removePlayer(targetId);

        player.sendMessage(SimpleClan.getMessages().get("kicked-from-clan", Map.of(
                "name", clan.getName(),
                "player", target.getName()
        )));

        target.sendMessage(SimpleClan.getMessages().get("you-were-kicked", Map.of(
                "name", clan.getName()
        )));
    }
}
