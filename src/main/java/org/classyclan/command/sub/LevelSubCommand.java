package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import java.util.UUID;

public class LevelSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "level";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager clanManager = ClassyClan.getInstance().getClanManager();

        UUID playerId = player.getUniqueId();
        if (!clanManager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = clanManager.getClan(playerId);

        player.sendMessage("§7Your clan is level §b" + clan.getLevel() + "§7/§b50");


    }
}
