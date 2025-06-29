package org.classyclan.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClanPlaceholderExpansion extends PlaceholderExpansion {

    private final ClanManager clanManager;

    public ClanPlaceholderExpansion(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clan";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Ouistitiw";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String identifier) {
        if (offlinePlayer == null || offlinePlayer.getUniqueId() == null) {
            return "";
        }

        UUID uuid = offlinePlayer.getUniqueId();
        Clan clan = clanManager.getClanByPlayer(uuid);

        // → Si aucun clan trouvé, on crée un dummy qui a rawName = ""
        if (clan == null) {
            clan = new Clan("", "", uuid);
        }

        return switch (identifier.toLowerCase()) {
            case "tag"           -> clan.getColoredPrefix();
            case "raw_tag"       -> clan.getRawPrefix();
            case "name"          -> clan.getColoredName();
            case "raw_name"      -> clan.getRawName();
            case "owner"         -> Bukkit.getOfflinePlayer(clan.getOwner()).getName();
            case "bank"          -> String.format("%.2f", clan.getBank());
            case "level"         -> String.valueOf(clan.getLevel());
            case "extra_members" -> String.valueOf(clan.getExtraMembers());
            case "extra_bases"   -> String.valueOf(clan.getExtraHomes());   // typo corrigée
            case "extra_vaults"  -> String.valueOf(clan.getExtraVaults());
            case "color"         -> clan.getColor();
            case "member_count"  -> String.valueOf(clan.getMembers().size());
            case "rank"          -> clan.getRank(uuid).name();
            case "is_leader"     -> String.valueOf(clan.getOwner().equals(uuid));
            case "members"       -> String.join(", ",
                    clan.getMembers().stream()
                            .map(id -> Bukkit.getOfflinePlayer(id).getName())
                            .toList());
            default               -> "";
        };
    }

}
