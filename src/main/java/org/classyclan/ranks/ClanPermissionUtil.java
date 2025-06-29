package org.classyclan.ranks;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;

import java.util.Map;
import java.util.UUID;

public class ClanPermissionUtil {

    public static boolean checkPermission(Player player, Clan clan, String permission) {
        ClanRank playerRank = clan.getRank(player.getUniqueId());

        if (playerRank.hasInheritedPermission(permission)) return true;

        ClanRank needed = getMinimumRankForPermission(permission);

        if (needed == null) {
            player.sendMessage("§cPermission '" + permission + "' non assignée à un rang.");
            return false;
        }

        player.sendMessage(ClassyClan.getMessages().get("no-clan-permission", Map.of(
                "needed_rank", needed.getDisplayName()
        )));
        return false;
    }

    public static ClanRank getMinimumRankForPermission(String permission) {
        for (ClanRank rank : ClanRank.values()) {
            if (rank.hasPermission(permission)) {
                return rank;
            }
        }
        return null;
    }


    public static boolean checkNotSelf(Player player, UUID targetId, String type) {
        if (player.getUniqueId().equals(targetId)) {
            player.sendMessage(ClassyClan.getMessages().get("cant-"+type+"-yourself")); // ou promote
            return false;
        }
        return true;
    }

    public static boolean checkRankHierarchy(Player player, ClanRank executor, ClanRank target) {
        if (!executor.isOrHigher(target) || executor == target) {
            player.sendMessage(ClassyClan.getMessages().get("no-clan-permission"));
            return false;
        }
        return true;
    }

    public static void sendFormatted(Player player, String key, Map<String, String> placeholders) {
        player.sendMessage(ClassyClan.getMessages().get(key, placeholders));
    }

    public static boolean checkPromoteHierarchy(Player player, ClanRank executorRank, ClanRank nextRank) {
        if (!executorRank.isOrHigher(nextRank) || executorRank == nextRank) {
            player.sendMessage(ClassyClan.getMessages().get("permission-denied"));
            return false;
        }
        return true;
    }



}
