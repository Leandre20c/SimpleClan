package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

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

        if (!ClassyClan.getInstance().getClanManager().isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClassyClan.getInstance().getClanManager().getClan(playerId);

        if (clan.getOwner().equals(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("leave-blocked-owner"));
            return;
        }

        clan.removeMember(playerId);
        ClassyClan.getInstance().getClanManager().removePlayer(playerId);

        player.sendMessage(ClassyClan.getMessages().get("left-clan", Map.of(
                "name", clan.getColoredName()
        )));
    }
}
