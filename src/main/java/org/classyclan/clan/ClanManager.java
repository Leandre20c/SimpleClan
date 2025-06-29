package org.classyclan.clan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.storage.ClanStorage;

import java.util.*;

public class ClanManager {
    private final Map<String, Clan> clans = new HashMap<>();
    private final Map<UUID, Clan> playerClanMap = new HashMap<>();
    private final Map<UUID, String> pendingInvites = new HashMap<>();

    public boolean isInClan(UUID playerId) {
        return playerClanMap.containsKey(playerId);
    }

    public Clan getClan(UUID playerId) {
        return playerClanMap.get(playerId);
    }

    public boolean clanExists(String name) {
        return clans.containsKey(name.toLowerCase());
    }

    public Clan createClan(String name, String prefix, UUID owner) {
        Clan newClan = new Clan(name, prefix, Bukkit.getPlayer(owner));
        clans.put(name.toLowerCase(), newClan);
        playerClanMap.put(owner, newClan);
        newClan.addMember(owner);
        ClanStorage.saveClan(newClan);
        return newClan;
    }

    public void disbandClan(String name) {
        String key = name.toLowerCase();
        if (!clans.containsKey(key)) return;

        Clan clan = clans.get(key);
        for (UUID member : clan.getMembers()) {
            playerClanMap.remove(member);
        }

        clans.remove(key);
        ClanStorage.deleteClan(clan);
    }

    public void removePlayer(UUID uuid) {
        Clan clan = getClan(uuid);
        if (clan != null) {
            clan.removeMember(uuid);
            playerClanMap.remove(uuid);
            ClanStorage.saveClan(clan);
        }
    }

    public void renameClan(Clan clan, String newName, String newPrefix) {
        String oldKey = clan.getRawName().toLowerCase();
        clans.remove(oldKey);
        ClanStorage.deleteClan(clan);

        clan.setName(newName);
        clan.setPrefix(newPrefix);

        clans.put(newName.toLowerCase(), clan);
        ClanStorage.saveClan(clan);
    }

    public boolean canJoinClan(Clan clan) {
        int max = ClassyClan.getInstance().getConfig().getInt("max-members-per-clan", 0);
        return (max == 0 || clan.getMembers().size() < max);
    }

    public void invitePlayer(Player inviter, Player target) {
        Clan clan = getClan(inviter.getUniqueId());
        if (clan != null) {
            pendingInvites.put(target.getUniqueId(), clan.getRawName().toLowerCase());
        }
    }

    public boolean hasInvite(UUID playerId, String clanName) {
        return pendingInvites.containsKey(playerId) &&
                pendingInvites.get(playerId).equalsIgnoreCase(clanName);
    }

    public void removeInvite(UUID playerId) {
        pendingInvites.remove(playerId);
    }

    public Clan getClanByName(String name) {
        return clans.get(name.toLowerCase());
    }

    public Clan getClanByPlayer(UUID playerId) {
        return playerClanMap.get(playerId);
    }

    public void setPlayerClan(UUID playerId, Clan clan) {
        if (clan == null) return;
        playerClanMap.put(playerId, clan);
        clan.addMember(playerId);
        ClanStorage.saveClan(clan);
    }

    public Collection<Clan> getAllClans() {
        return clans.values();
    }

    public void loadAllFromStorage() {
        clans.clear();
        playerClanMap.clear();

        for (Clan clan : ClanStorage.loadAllClans()) {
            clans.put(clan.getRawName().toLowerCase(), clan);
            for (UUID member : clan.getMembers()) {
                playerClanMap.put(member, clan);
            }
        }
    }

    public void saveAllToStorage() {
        for (Clan clan : clans.values()) {
            ClanStorage.saveClan(clan);
        }
    }
}
