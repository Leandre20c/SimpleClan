package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;
import java.util.UUID;

public class LeaveSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (!ClanManager.get().isInClan(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(playerId);

        if (clan.getOwner().equals(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("leave-blocked-owner"));
            return;
        }

        clan.removeMember(playerId);
        ClanManager.get().removePlayer(playerId);

        player.sendMessage(SimpleClan.getMessages().get("left-clan", Map.of(
                "name", clan.getName()
        )));
    }
}
