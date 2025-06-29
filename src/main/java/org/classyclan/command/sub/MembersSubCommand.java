package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.menu.members.MembersMenu;

import java.util.UUID;

public class MembersSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "members";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (!ClassyClan.getInstance().getClanManager().isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClassyClan.getInstance().getClanManager().getClan(playerId);

        MembersMenu.open(player, clan);
    }
}
