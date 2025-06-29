package org.classyclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;

import java.util.Map;

public class CreateSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ClassyClan.getMessages().get("usage-create"));
            return;
        }

        String name = args[1];
        String prefix = args[2];

        if (!prefix.matches("^[a-zA-Z0-9]+$")) {
            player.sendMessage(ClassyClan.getMessages().get("usage-tag"));
            return;
        }

        if (ClassyClan.getInstance().getClanManager().isInClan(player.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("already-in-clan"));
            return;
        }

        int maxNameLength = ClassyClan.getInstance().getConfig().getInt("max-clan-name-length");
        int minNameLength = ClassyClan.getInstance().getConfig().getInt("min-clan-name-length");
        int maxPrefixLength = ClassyClan.getInstance().getConfig().getInt("max-clan-prefix-length");
        int minPrefixLength = ClassyClan.getInstance().getConfig().getInt("min-clan-prefix-length");

        if (name.length() > maxNameLength) {
            player.sendMessage(ClassyClan.getMessages().get("name-too-long"));
            return;
        }

        if (name.length() < minNameLength) {
            player.sendMessage(ClassyClan.getMessages().get("name-too-short"));
            return;
        }

        if (prefix.length() > maxPrefixLength) {
            player.sendMessage(ClassyClan.getMessages().get("prefix-too-long"));
            return;
        }

        if (prefix.length() < minPrefixLength) {
            player.sendMessage(ClassyClan.getMessages().get("prefix-too-short"));
            return;
        }

        if (ClassyClan.getInstance().getClanManager().clanExists(name)) {
            player.sendMessage(ClassyClan.getMessages().get("clan-exists"));
            return;
        }

        ClassyClan.getInstance().getClanManager().createClan(name, prefix, player.getUniqueId());

        Bukkit.broadcastMessage(ClassyClan.getMessages().get("clan-created", Map.of(
                "name", name,
                "prefix", prefix,
                "player", player.getName()
        )));
    }
}
