package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import java.util.Map;
import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class RenameSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();
        Clan clan = ClassyClan.getInstance().getClanManager().getClan(playerId);

        if (!ClassyClan.getInstance().getClanManager().isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        if (!checkPermission(player, clan, "clan.settings.rename")) return;

        if (args.length < 3) {
            player.sendMessage(ClassyClan.getMessages().get("usage-rename"));
            return;
        }

        if (!clan.getOwner().equals(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("action-requires-owner"));
            return;
        }

        String newName = args[1];
        String newPrefix = args[2];

        if (!newPrefix.matches("^[a-zA-Z0-9]+$")) {
            player.sendMessage(ClassyClan.getMessages().get("usage-tag"));
            return;
        }

        int maxNameLength = ClassyClan.getInstance().getConfig().getInt("max-clan-name-length");
        int minNameLength = ClassyClan.getInstance().getConfig().getInt("min-clan-name-length");
        int maxPrefixLength = ClassyClan.getInstance().getConfig().getInt("max-clan-prefix-length");
        int minPrefixLength = ClassyClan.getInstance().getConfig().getInt("min-clan-prefix-length");

        if (newName.length() > maxNameLength) {
            player.sendMessage(ClassyClan.getMessages().get("name-too-long"));
            return;
        }

        if (newName.length() < minNameLength) {
            player.sendMessage(ClassyClan.getMessages().get("name-too-short"));
            return;
        }

        if (newPrefix.length() > maxPrefixLength) {
            player.sendMessage(ClassyClan.getMessages().get("prefix-too-long"));
            return;
        }

        if (newPrefix.length() < minPrefixLength) {
            player.sendMessage(ClassyClan.getMessages().get("prefix-too-short"));
            return;
        }

        if (ClassyClan.getInstance().getClanManager().clanExists(newName)) {
            player.sendMessage(ClassyClan.getMessages().get("clan-exists"));
            return;
        }

        ClassyClan.getInstance().getClanManager().renameClan(clan, newName, newPrefix);

        player.sendMessage(ClassyClan.getMessages().get("clan-renamed", Map.of(
                "name", newName,
                "prefix", newPrefix
        )));
    }
}
