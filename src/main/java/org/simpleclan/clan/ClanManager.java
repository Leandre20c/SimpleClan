package org.simpleclan.clan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;

import java.util.*;

public class ClanManager {
    private final Map<String, Clan> clans = new HashMap<>();
    private final Map<UUID, Clan> playerClanMap = new HashMap<>();
    private final Map<UUID, String> pendingInvites = new HashMap<>();

    private static final ClanManager instance = new ClanManager();

    public static ClanManager get() {
        return instance;
    }

    public boolean isInClan(UUID playerId){
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

        // for secure, even if it is not needed
        newClan.addMember(owner);

        return newClan;
    }

    public void disbandClan(String name) {
        String key = name.toLowerCase();

        if (!clans.containsKey(key)) {
            return;
        }

        Clan clan = clans.get(key);

        for (UUID member : clan.getMembers()) {
            playerClanMap.remove(member);
        }

        clans.remove(key);
    }

    public void removePlayer(UUID uuid) {
        playerClanMap.remove(uuid);
    }

    public void renameClan(Clan clan, String newName, String newPrefix) {
        clans.remove(clan.getName().toLowerCase());

        clan.setName(newName);
        clan.setPrefix(newPrefix);

        clans.put(newName.toLowerCase(), clan);
    }

    public boolean canJoinClan(Clan clan) {
        int max = SimpleClan.getInstance().getConfig().getInt("max-members-per-clan", 0);
        return max == 0 || clan.getMembers().size() < max;
    }

    public void invitePlayer(Player inviter, Player target){
        Clan clan = getClan(inviter.getUniqueId());
        if (clan != null) {
            pendingInvites.put(target.getUniqueId(), clan.getName().toLowerCase());
        }
    }

    public boolean hasInvite(UUID playerId, String clanName){
        return pendingInvites.containsKey(playerId) &&
                pendingInvites.get(playerId).equalsIgnoreCase(clanName);
    }

    public void removeInvite(UUID playerId) {
        pendingInvites.remove(playerId);
    }

    public Clan getClanByName(String name) {
        return clans.get(name.toLowerCase());
    }

    public void setPlayerClan(UUID playerId, Clan clan) {
        playerClanMap.put(playerId, clan);
    }

    public Collection<Clan> getAllClans() {
        return clans.values();
    }
}
