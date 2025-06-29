package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.chat.ClanChatManager;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanPermissionUtil;

public class ChatSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager clanManager = ClassyClan.getInstance().getClanManager();
        ClanChatManager chatManager = ClassyClan.getInstance().getClanChatManager();

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cYou are not in a clan.");
            return;
        }

        if (!ClanPermissionUtil.checkPermission(player, clan, "clan.chat")) return;

        chatManager.toggleClanChat(player.getUniqueId());
        boolean enabled = chatManager.isInClanChat(player.getUniqueId());

        player.sendMessage("§7Clan chat " + (enabled ? "§aenabled" : "§cdisabled") + "§7.");
    }
}
