package org.classyclan.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.Map;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class InviteSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ClassyClan.getMessages().get("usage-invite"));
            return;
        }

        if (!ClassyClan.getInstance().getClanManager().isInClan(player.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClassyClan.getInstance().getClanManager().getClan(player.getUniqueId());

        if (!checkPermission(player, clan, "clan.invite")) return;

        Player target = Bukkit.getPlayer(args[1]);

        if (ClassyClan.getInstance().getClanManager().isInClan(target.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("player-already-in-clan"));
            return;
        }

        if (!target.isOnline()) {
            player.sendMessage(ClassyClan.getMessages().get("player-not-found"));
            return;
        }

        ClassyClan.getInstance().getClanManager().invitePlayer(player, target);

        player.sendMessage(ClassyClan.getMessages().get("invite-sent", Map.of("player", target.getName())));
        sendCommand(target, "/clan join " + clan.getRawName(), ClassyClan.getMessages().get("invite-sent-clickable-join", Map.of("player", target.getName())));

        String clanName = clan.getColoredName();

        target.sendMessage(ClassyClan.getMessages().get("invite-received", Map.of("clan", clanName)));
    }

    private void sendCommand(Player player, String command, String description) {
        TextComponent line = new TextComponent("§a" + command + " §7- " + description);
        line.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        player.spigot().sendMessage(line);
    }
}
