package org.simpleclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.ChatColor;

import java.util.Map;

public class InviteSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SimpleClan.getMessages().get("usage-invite"));
            return;
        }

        if (!ClanManager.get().isInClan(player.getUniqueId())) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(player.getUniqueId());

        if (!clan.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(SimpleClan.getMessages().get("action-requires-owner"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (ClanManager.get().isInClan(target.getUniqueId())){
            player.sendMessage(SimpleClan.getMessages().get("player-already-in-clan"));
            return;
        }

        if (target == null || !target.isOnline()) {
            player.sendMessage(SimpleClan.getMessages().get("player-not-found"));
            return;
        }

        ClanManager.get().invitePlayer(player, target);

        player.sendMessage(SimpleClan.getMessages().get("invite-sent", Map.of("player", target.getName())));

        String clanName = clan.getName();

        // target invitation received
        target.sendMessage(SimpleClan.getMessages().get("invite-received", Map.of("clan", clanName)));
        // join clickable message
        TextComponent join = new TextComponent("§a§l[JOIN]");
        join.setColor(ChatColor.GREEN);
        join.setBold(true);
        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan join " + clanName));

        target.spigot().sendMessage(join);

    }
}
