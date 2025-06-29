package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.menu.settings.SettingsMenu;

import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class SettingsSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = manager.getClan(playerId);
        if (!checkPermission(player, clan, "clan.settings.menu")) return;

        SettingsMenu.open(player);
    }
}
