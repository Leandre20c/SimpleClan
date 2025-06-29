package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.menu.allClans.AllClansMenu;

public class ListSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(Player player, String[] args) {
        AllClansMenu.open(player);
    }
}
