package org.simpleclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;

public class CreateSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(SimpleClan.getMessages().get("usage-create"));
            return;
        }

        String name = args[1];
        String prefix = args[2];

        if (ClanManager.get().isInClan(player.getUniqueId())) {
            player.sendMessage(SimpleClan.getMessages().get("already-in-clan"));
            return;
        }

        if (name.length() > SimpleClan.getInstance().getConfig().getInt("max-clan-name-length")) {
            player.sendMessage(SimpleClan.getMessages().get("name-too-long"));
            return;
        }

        if (prefix.length() > 2) {
            player.sendMessage(SimpleClan.getMessages().get("prefix-too-long"));
            return;
        }

        if (ClanManager.get().clanExists(name)) {
            player.sendMessage(SimpleClan.getMessages().get("clan-exists"));
            return;
        }

        ClanManager.get().createClan(name, prefix, player.getUniqueId());

        Bukkit.broadcastMessage(SimpleClan.getMessages().get("clan-created", Map.of(
                "name", name,
                "prefix", prefix,
                "player", player.getName()
        )));
    }
}
