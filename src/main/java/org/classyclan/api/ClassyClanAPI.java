package org.classyclan.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ClassyClanAPI {

    private final ClanManager clanManager;

    public ClassyClanAPI(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    /**
     * Récupère le clan d’un joueur (null si aucun).
     */
    public Clan getClanOf(UUID playerId) {
        return clanManager.getClanByPlayer(playerId);
    }

    /**
     * Récupère le clan par son nom brut.
     */
    public Clan getClanByName(String name) {
        return clanManager.getClanByName(name);
    }

    /**
     * Vérifie si un joueur est dans un clan.
     */
    public boolean isInClan(UUID playerId) {
        return getClanOf(playerId) != null;
    }

    /**
     * Récupère tous les clans existants.
     */
    public Collection<Clan> getAllClans() {
        return clanManager.getAllClans();
    }

    /**
     * Dépose de l'argent dans la banque du clan.
     */
    public void depositToClan(Clan clan, double amount) {
        clan.deposit(amount);
    }

    /**
     * Retire de l'argent du clan (si possible).
     */
    public boolean withdrawFromClan(Clan clan, double amount) {
        return clan.withdraw(amount);
    }

    /**
     * Récupère le rang d’un joueur dans son clan.
     */
    public String getRank(UUID playerId) {
        Clan clan = getClanOf(playerId);
        if (clan == null) return null;
        return clan.getRank(playerId).name();
    }
}
