package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.ClanManager;
import org.classyclan.menu.clan.ClanMenu;

public class MenuSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager clanManager = ClassyClan.getInstance().getClanManager();

        if (!clanManager.isInClan(player.getUniqueId())){
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        ClanMenu.open(player);
    }
}
