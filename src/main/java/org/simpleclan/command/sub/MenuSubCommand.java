package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.menu.ClanMenu;

public class MenuSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanMenu.open(player);
    }
}
