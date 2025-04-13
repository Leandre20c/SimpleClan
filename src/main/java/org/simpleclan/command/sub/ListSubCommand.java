package org.simpleclan.command.sub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ListSubCommand implements SubCommand {

    private static final int CLANS_PER_PAGE = 5;

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(Player player, String[] args) {
        int page = 1;
        if (args.length >= 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                // On garde page = 1
            }
        }

        List<Clan> allClans = new ArrayList<>(ClanManager.get().getAllClans());
        allClans.sort(Comparator.comparing(Clan::getName));

        int maxPage = (int) Math.ceil((double) allClans.size() / CLANS_PER_PAGE);
        page = Math.max(1, Math.min(page, maxPage)); // sécurité page valide

        int start = (page - 1) * CLANS_PER_PAGE;
        int end = Math.min(start + CLANS_PER_PAGE, allClans.size());

        player.sendMessage(SimpleClan.getMessages().get("clan-list-header", Map.of(
                "page", String.valueOf(page),
                "max", String.valueOf(maxPage)
        )));

        for (int i = start; i < end; i++) {
            Clan clan = allClans.get(i);
            player.sendMessage("§b- " + clan.getName() + " §7[" + clan.getPrefix() + "] §8(" + clan.getMembers().size() + " members)");
        }

        // Boutons de pagination
        TextComponent line = new TextComponent();

        if (page > 1) {
            TextComponent prev = new TextComponent("⏪ PREVIOUS ");
            prev.setColor(ChatColor.YELLOW);
            prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan list " + (page - 1)));
            line.addExtra(prev);
        }

        if (page < maxPage) {
            TextComponent next = new TextComponent(" NEXT ⏩");
            next.setColor(ChatColor.GREEN);
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan list " + (page + 1)));
            line.addExtra(next);
        }

        if (line.getExtra() != null && !line.getExtra().isEmpty()) {
            player.spigot().sendMessage(line);
        }
    }
}
