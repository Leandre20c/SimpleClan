package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;
import java.util.UUID;

public class SetBaseSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "setbase";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (!ClanManager.get().isInClan(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(playerId);

        if (!clan.getOwner().equals(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("action-requires-owner"));
            return;
        }

        clan.setBase(player.getLocation());
        player.sendMessage(SimpleClan.getMessages().get("base-set"));
    }
}
