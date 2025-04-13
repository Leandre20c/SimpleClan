package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;

public class JoinSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SimpleClan.getMessages().get("usage-join"));
            return;
        }

        String clanName = args[1];

        if (!ClanManager.get().clanExists(clanName)) {
            player.sendMessage(SimpleClan.getMessages().get("clan-not-found"));
            return;
        }

        if (!ClanManager.get().hasInvite(player.getUniqueId(), clanName)) {
            player.sendMessage(SimpleClan.getMessages().get("no-invite"));
            return;
        }

        Clan clan = ClanManager.get().getClanByName(clanName);

        if (!ClanManager.get().canJoinClan(clan)) {
            player.sendMessage(SimpleClan.getMessages().get("clan-full"));
            return;
        }

        clan.addMember(player.getUniqueId());
        ClanManager.get().setPlayerClan(player.getUniqueId(), clan);
        ClanManager.get().removeInvite(player.getUniqueId());

        player.sendMessage(SimpleClan.getMessages().get("joined-clan", Map.of("name", clan.getName())));
    }
}
