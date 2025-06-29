package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class SetBaseSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "setbase";
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

        if (!checkPermission(player, clan, "clan.base.setbase")) return;

        int baseNumber = 1;
        if (args.length > 1) {
            try {
                baseNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid base number.");
                return;
            }
        }

        int maxBases = 1 + clan.getExtraHomes();
        if (baseNumber > maxBases || baseNumber < 1) {
            player.sendMessage("§cYou can only set up to " + maxBases + " base(s).");
            return;
        }

        clan.setBase(baseNumber, player.getLocation());
        player.sendMessage("§aClan base " + baseNumber + " has been set to your current location.");
    }
}
