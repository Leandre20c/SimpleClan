package org.classyclan.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanRank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanChatListener implements Listener {

    private final ClanChatManager chatManager;
    private final ClanManager clanManager;

    public ClanChatListener() {
        this.chatManager = ClassyClan.getInstance().getClanChatManager();
        this.clanManager = ClassyClan.getInstance().getClanManager();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        UUID uuid = sender.getUniqueId();

        if (!chatManager.isInClanChat(uuid)) return;

        Clan clan = clanManager.getClan(uuid);
        if (clan == null) {
            sender.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            chatManager.disableClanChat(uuid);
            return;
        }

        ClanRank rank = clan.getRank(uuid);

        String message = event.getMessage();
        event.setCancelled(true);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("clan_prefix", clan.getColoredPrefix());
        placeholders.put("clan_name", clan.getColoredName());
        placeholders.put("player_name", sender.getName());
        placeholders.put("player_clan_rank", rank.getDisplayName());
        placeholders.put("message", message);

        String formatted = ClassyClan.getMessages().get("clan-chat-format", placeholders);

        for (UUID memberUUID : clan.getMembers()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member != null && member.isOnline()) {
                member.sendMessage(formatted);
            }
        }
    }
}
