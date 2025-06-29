package org.classyclan.command.sub;

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

        // 🌐 Base
        sendCommand(player, "/clan menu", "Open the clan menu");
        sendCommand(player, "/clan info", "Show info about your current clan");

        // 🏗️ Management
        sendCommand(player, "/clan create <name> <prefix>", "Create a new clan");
        sendCommand(player, "/clan rename <name> <prefix>", "Rename your clan");
        sendCommand(player, "/clan disband", "Disband your clan (leader only)");
        sendCommand(player, "/clan leave", "Leave your current clan");

        // 👥 Members & Ranks
        sendCommand(player, "/clan invite <player>", "Invite a player to your clan");
        sendCommand(player, "/clan join <clan>", "Join a clan you've been invited to");
        sendCommand(player, "/clan kick <player>", "Kick a member from your clan");
        sendCommand(player, "/clan promote <player>", "Promote a member to a higher rank");
        sendCommand(player, "/clan demote <player>", "Demote a member to a lower rank");
        sendCommand(player, "/clan members", "Show members of your clan");
        sendCommand(player, "/clan members <clan>", "Show members of another clan");
        sendCommand(player, "/clan rank <player>", "View a player's clan and rank");
        sendCommand(player, "/clan list", "List all clans");

        // 🏠 Base
        sendCommand(player, "/clan setbase", "Set your clan base");
        sendCommand(player, "/clan base", "Teleport to your clan base");

        // 💰 Vault / Bank
        sendCommand(player, "/clan vault", "Open your clan vault");
        sendCommand(player, "/clan bank", "Show bank balance");
        sendCommand(player, "/clan bank deposit <amount>", "Deposit money into the clan bank");
        sendCommand(player, "/clan bank withdraw <amount>", "Withdraw money from the clan bank");

        // ♻️ Utils
        sendCommand(player, "/clan reload", "Reload plugin configuration");
        sendCommand(player, "/clan help", "Show this help menu");

        player.sendMessage("§8§m----------------------------------");
    }

    private void sendCommand(Player player, String command, String description) {
        TextComponent line = new TextComponent("§a" + command + " §7- " + description);
        line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        player.spigot().sendMessage(line);
    }
}
