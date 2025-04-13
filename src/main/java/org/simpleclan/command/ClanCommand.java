package org.simpleclan.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.command.sub.*;
import org.simpleclan.menu.ClanMenu;

import java.util.*;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public ClanCommand() {
        registerSubCommand(new CreateSubCommand());
        registerSubCommand(new LeaveSubCommand());
        registerSubCommand(new DisbandSubCommand());
        registerSubCommand(new RenameSubCommand());
        registerSubCommand(new ReloadSubCommand());
        registerSubCommand(new MenuSubCommand());
        registerSubCommand(new SetBaseSubCommand());
        registerSubCommand(new BaseSubCommand());
        registerSubCommand(new InviteSubCommand());
        registerSubCommand(new JoinSubCommand());
        registerSubCommand(new HelpSubCommand());
        registerSubCommand(new KickSubCommand());
        registerSubCommand(new MembersSubCommand());
        registerSubCommand(new ListSubCommand());
        registerSubCommand(new VaultSubCommand());

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
            String current = args[0].toLowerCase();
            return subCommands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(current))
                    .sorted()
                    .toList();
        }

        return List.of();
    }
}
