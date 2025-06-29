package org.classyclan.command.sub;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanRank;
import org.classyclan.vault.VaultManager;

import java.util.Map;
import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class LevelupSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "levelup";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager manager = ClassyClan.getInstance().getClanManager();
        UUID playerId = player.getUniqueId();

        // Vérifie que le joueur est bien dans un clan
        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = manager.getClan(playerId);
        ClanRank rank = clan.getRank(playerId);

        if (!checkPermission(player, clan, "clan.levelup")) return;

        int currentLevel = clan.getLevel();
        int maxLevel = ClassyClan.getInstance().getConfig().getInt("leveling.max-level", 50);

        if (currentLevel >= maxLevel) {
            player.sendMessage(ClassyClan.getMessages().get("level-max"));
            return;
        }

        int newLevel = currentLevel + 1;
        double cost = VaultManager.get().getPromotionCost(newLevel);

        if (clan.getBank() < cost) {
            player.sendMessage(ClassyClan.getMessages().get("level-up-insufficient-funds", Map.of(
                    "cost", String.format("%.2f", cost)
            )));
            return;
        }

        // Débit du compte
        clan.setBank(clan.getBank() - cost);
        clan.setLevel(newLevel);

        // Application des récompenses
        ConfigurationSection rewards = ClassyClan.getInstance().getConfig().getConfigurationSection("leveling.rewards." + newLevel);
        if (rewards != null) {
            if (rewards.contains("extra-member")) {
                clan.setExtraMembers(clan.getExtraMembers() + rewards.getInt("extra-member"));
            }
            if (rewards.contains("extra-homes")) {
                clan.setExtraHomes(clan.getExtraHomes() + rewards.getInt("extra-homes"));
            }
            if (rewards.contains("extra-vaults")) {
                clan.setExtraVaults(clan.getExtraVaults() + rewards.getInt("extra-vaults"));
            }
            if (rewards.contains("clan-color")) {
                String color = rewards.getString("clan-color");
                if (color != null && !clan.getUnlockedColors().contains(color)) {
                    clan.addUnlockedColor(color);
                }
            }
        }

        player.sendMessage(ClassyClan.getMessages().get("level-up-success", Map.of(
                "level", String.valueOf(newLevel),
                "cost", String.format("%.2f", cost)
        )));
    }
}
