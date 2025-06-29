package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import java.util.Map;

public class JoinSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ClassyClan.getMessages().get("usage-join"));
            return;
        }

        String clanName = args[1];
        Clan clan = ClassyClan.getInstance().getClanManager().getClanByName(clanName);

        if (!ClassyClan.getInstance().getClanManager().clanExists(clanName)) {
            player.sendMessage(ClassyClan.getMessages().get("clan-not-found"));
            return;
        }

        if (clan.isClanOpen())
        {
            joinClan(clan, player);
            return;
        }

        if (ClassyClan.getInstance().getClanManager().hasInvite(player.getUniqueId(), clanName)) {
            joinClan(clan, player);
            return;
        }

        player.sendMessage(ClassyClan.getMessages().get("no-invite"));
    }

    private void joinClan(Clan clan, Player player){
        if (!ClassyClan.getInstance().getClanManager().canJoinClan(clan)) {
            player.sendMessage(ClassyClan.getMessages().get("clan-full"));
            return;
        }

        clan.addMember(player.getUniqueId());
        ClassyClan.getInstance().getClanManager().setPlayerClan(player.getUniqueId(), clan);
        ClassyClan.getInstance().getClanManager().removeInvite(player.getUniqueId());

        player.sendMessage(ClassyClan.getMessages().get("joined-clan", Map.of("name", clan.getColoredName())));
    }
}
