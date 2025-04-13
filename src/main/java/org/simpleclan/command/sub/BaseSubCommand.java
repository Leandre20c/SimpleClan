package org.simpleclan.command.sub;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseSubCommand implements SubCommand {

    private static final Map<UUID, Long> lastTeleport = new HashMap<>();

    @Override
    public String getName() {
        return "base";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();

        if (!ClanManager.get().isInClan(playerId)) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(playerId);
        Location base = clan.getBase();

        if (base == null) {
            player.sendMessage(SimpleClan.getMessages().get("no-base-set"));
            return;
        }

        // Cooldown check
        int cooldownSeconds = SimpleClan.getInstance().getConfig().getInt("base-teleport-cooldown", 60);
        long now = System.currentTimeMillis();
        long lastUsed = lastTeleport.getOrDefault(playerId, 0L);

        if ((now - lastUsed) < cooldownSeconds * 1000L) {
            long remaining = ((lastUsed + cooldownSeconds * 1000L) - now) / 1000L;
            player.sendMessage(SimpleClan.getMessages().get("base-cooldown", Map.of(
                    "time", String.valueOf(remaining)
            )));
            return;
        }

        lastTeleport.put(playerId, now);
        player.teleport(base);
        player.sendMessage(SimpleClan.getMessages().get("teleported-to-base"));
    }
}
