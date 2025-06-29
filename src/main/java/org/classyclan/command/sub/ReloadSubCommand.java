package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;

public class ReloadSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(ClassyClan.getMessages().get("no-permission"));
            return;
        }

        // Reload des fichiers de configuration
        ClassyClan.getInstance().reloadConfig(); // Reload du config.yml
        ClassyClan.getInstance().saveDefaultConfig(); // Crée config.yml si jamais il n'existait pas
        ClassyClan.getMessages().reload(); // Reload du messages.yml
        ClassyClan.getInstance().getGuiManager().reload();
        ClassyClan.getInstance().loadRankConfigs();

        player.sendMessage(ClassyClan.getMessages().get("reloaded"));
    }
}
