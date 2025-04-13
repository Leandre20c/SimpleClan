package org.simpleclan.command.sub;

import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;
import java.util.UUID;

public class RenameSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (args.length < 3) {
            player.sendMessage(SimpleClan.getMessages().get("usage-rename"));
            return;
        }

        if (!ClanManager.get().isInClan(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(playerId);

        if (!clan.getOwner().equals(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("action-requires-owner"));
            return;
        }

        String newName = args[1];
        String newPrefix = args[2];

        if (newName.length() > SimpleClan.getInstance().getConfig().getInt("max-clan-name-length")) {
            player.sendMessage(SimpleClan.getMessages().get("name-too-long"));
            return;
        }

        if (newPrefix.length() > 2) {
            player.sendMessage(SimpleClan.getMessages().get("prefix-too-long"));
            return;
        }

        if (ClanManager.get().clanExists(newName)) {
            player.sendMessage(SimpleClan.getMessages().get("clan-exists"));
            return;
        }

        ClanManager.get().renameClan(clan, newName, newPrefix);

        player.sendMessage(SimpleClan.getMessages().get("clan-renamed", Map.of(
                "name", newName,
                "prefix", newPrefix
        )));
    }
}
