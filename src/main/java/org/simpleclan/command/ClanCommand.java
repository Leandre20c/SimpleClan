package org.simpleclan.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.command.sub.*;
import org.simpleclan.menu.ClanMenu;

import java.util.*;
import java.util.stream.Collectors;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public ClanCommand() {
        registerSubCommand(new CreateSubCommand());
        registerSubCommand(new RenameSubCommand());
        registerSubCommand(new DisbandSubCommand());
        registerSubCommand(new LeaveSubCommand());
        registerSubCommand(new KickSubCommand());
        registerSubCommand(new InviteSubCommand());
        registerSubCommand(new JoinSubCommand());
        registerSubCommand(new MembersSubCommand());
        registerSubCommand(new SetBaseSubCommand());
        registerSubCommand(new BaseSubCommand());
        registerSubCommand(new BankSubCommand());
        registerSubCommand(new VaultSubCommand());
        registerSubCommand(new HelpSubCommand());
        registerSubCommand(new ListSubCommand());
        registerSubCommand(new MenuSubCommand());
        registerSubCommand(new ReloadSubCommand());
    }

    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SimpleClan.getMessages().get("only-players"));
            return true;
        }

        if (args.length == 0) {
            ClanMenu.open(player);
            return true;
        }

        String subName = args[0].toLowerCase();
        SubCommand sub = subCommands.get(subName);

        if (sub != null) {
            sub.execute(player, args);
        } else {
            player.sendMessage(SimpleClan.getMessages().get("unknown-command"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(subCommands.keySet());
        }

        if (args[0].equalsIgnoreCase("bank")) {
            if (args.length == 2) {
                return List.of("deposit", "withdraw");
            }
            if (args.length == 3) {
                return List.of("<amount>");
            }
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();

            if (sub.equals("invite") || sub.equals("join") || sub.equals("kick")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }
}
