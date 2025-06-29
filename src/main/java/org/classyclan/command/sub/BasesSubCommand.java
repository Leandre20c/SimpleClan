package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.ClanManager;
import org.classyclan.menu.bases.BasesMenu;

import java.util.UUID;

public class BasesSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "bases";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        BasesMenu.open(player);
    }
}
