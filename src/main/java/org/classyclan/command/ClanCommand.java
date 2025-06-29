// 📄 ClanCommand.java
package org.classyclan.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.command.sub.*;
import org.classyclan.menu.clan.ClanMenu;

import java.util.*;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public ClanCommand() {
        // 🏗️ Clan Management
        registerSubCommand(new CreateSubCommand());
        registerSubCommand(new RenameSubCommand());
        registerSubCommand(new DisbandSubCommand());
        registerSubCommand(new LeaveSubCommand());
        registerSubCommand(new KickSubCommand());
        registerSubCommand(new PromoteSubCommand());
        registerSubCommand(new DemoteSubCommand());
        // description
        registerSubCommand(new DescriptionSubCommand());

        // 👥 Members & Invitations
        registerSubCommand(new InviteSubCommand());
        registerSubCommand(new JoinSubCommand());
        registerSubCommand(new MembersSubCommand());
        // Open
        registerSubCommand(new OpenSubCommand());
        registerSubCommand(new CloseSubCommand());


        // 🏠 Base & Teleportation
        registerSubCommand(new SetBaseSubCommand());
        registerSubCommand(new BaseSubCommand());

        // 💰 Economy
        registerSubCommand(new BankSubCommand());

        registerSubCommand(new LevelSubCommand());
        registerSubCommand(new LevelupSubCommand());

        // Vault
        registerSubCommand(new VaultSubCommand());

        // 📋 Display & Menu
        registerSubCommand(new HelpSubCommand());
        registerSubCommand(new ListSubCommand());
        registerSubCommand(new MenuSubCommand());
        registerSubCommand(new InfoSubCommand());
        registerSubCommand(new ColorSubCommand());
        registerSubCommand(new SettingsSubCommand());
        registerSubCommand(new BasesSubCommand());
        registerSubCommand(new VaultsSubCommand());
        // banner
        registerSubCommand(new BannerSubCommand());

        // Chat
        registerSubCommand(new ChatSubCommand());
        registerSubCommand(new MsgSubCommand());


        // ♻️ Utility
        registerSubCommand(new ReloadSubCommand());
    }

    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ClassyClan.getMessages().get("only-players"));
            return true;
        }

        if (args.length == 0) {
            player.performCommand("clan menu");
            return true;
        }

        String subName = args[0].toLowerCase();
        SubCommand sub = subCommands.get(subName);

        if (sub != null) {
            sub.execute(player, args);
        } else {
            player.sendMessage(ClassyClan.getMessages().get("unknown-command"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.entrySet().stream()
                    .filter(entry -> {
                        String name = entry.getKey();
                        if (name.equalsIgnoreCase("reload")) {
                            return sender.hasPermission("simpleclan.reload");
                        }
                        return true;
                    })
                    .map(Map.Entry::getKey)
                    .toList();
        }

        if (args[0].equalsIgnoreCase("bank")) {
            if (args.length == 2) return List.of("deposit", "withdraw", "balance");
            if (args.length == 3) return List.of("<amount>");
        }

        if ((args[0].equalsIgnoreCase("invite") ||
                args[0].equalsIgnoreCase("kick") ||
                args[0].equalsIgnoreCase("promote") ||
                args[0].equalsIgnoreCase("demote") ||
                args[0].equalsIgnoreCase("player")) && args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (args[0].equalsIgnoreCase("banner")) {
            return List.of("get", "set");
        }

        return List.of();
    }

}
