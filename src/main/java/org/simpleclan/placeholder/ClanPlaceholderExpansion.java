package org.simpleclan.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

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
        return "HommeGenial";
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
        if (offlinePlayer == null || offlinePlayer.getUniqueId() == null) return "";

        UUID uuid = offlinePlayer.getUniqueId();
        Clan clan = clanManager.getClanByPlayer(uuid);

        if (clan == null) return "";

        return switch (identifier.toLowerCase()) {
            case "prefix" -> "[" + clan.getPrefix() + "]";
            case "name" -> clan.getName();
            case "owner" -> {
                UUID ownerId = clan.getOwner();
                yield Bukkit.getOfflinePlayer(ownerId).getName();
            }
            case "home" -> {
                if (clan.getBase() == null) yield "Unset";
                yield clan.getBase().getWorld().getName() + "," +
                        clan.getBase().getBlockX() + "," +
                        clan.getBase().getBlockY() + "," +
                        clan.getBase().getBlockZ();
            }
            case "bank" -> String.format("%.2f", clan.getBank());
            case "member_count" -> String.valueOf(clan.getMembers().size());
            default -> null;
        };
    }
}
