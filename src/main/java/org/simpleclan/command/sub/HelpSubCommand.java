package org.simpleclan.command.sub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class HelpSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(Player player, String[] args) {

        player.sendMessage("§8§m----------§r §bClan Help §8§m----------");

        sendCommand(player, "/clan menu", "Open the clan menu");
        sendCommand(player, "/clan create <name> <prefix>", "Create a new clan");
        sendCommand(player, "/clan rename <name> <prefix>", "Rename your clan");
        sendCommand(player, "/clan leave", "Leave your current clan");
        sendCommand(player, "/clan disband", "Disband your clan (owner only)");
        sendCommand(player, "/clan invite <player>", "Invite a player to your clan");
        sendCommand(player, "/clan join <clan>", "Join a clan you've been invited to");
        sendCommand(player, "/clan setbase", "Set your clan base (owner only)");
        sendCommand(player, "/clan base", "Teleport to your clan base");

        player.sendMessage("§8§m----------------------------------");
    }

    private void sendCommand(Player player, String command, String description) {
        TextComponent line = new TextComponent("§a" + command + " §7- " + description);
        line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        player.spigot().sendMessage(line);
    }
}
