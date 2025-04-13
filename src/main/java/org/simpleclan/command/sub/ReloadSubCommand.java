package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;

public class ReloadSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(SimpleClan.getMessages().get("no-permission"));
            return;
        }

        SimpleClan.getMessages().reload();
        player.sendMessage(SimpleClan.getMessages().get("messages-reloaded"));
    }
}
