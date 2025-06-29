package org.classyclan.command.sub;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.ranks.ClanPermissionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseSubCommand implements SubCommand {

    private static final Map<UUID, Long> lastTeleport = new HashMap<>();
    private static final Map<UUID, Boolean> canTeleport = new HashMap<>();
    private static final long COOLDOWN_MILLIS = 3000L;

    public static void teleportWithCountdown(Player player, Clan clan, int baseIndex) {
        Location base = clan.getBase(baseIndex);
        if (base == null) {
            player.sendMessage(ClassyClan.getMessages().get("no-base-nb-set", Map.of(
                    "base_index", String.valueOf(baseIndex)
            )));
            return;
        }

        if (!ClanPermissionUtil.checkPermission(player, clan, "clan.base.teleport")) return;

        player.sendMessage(ClassyClan.getMessages().get("teleport-start", Map.of(
                "base_index", String.valueOf(baseIndex)
        )));

        UUID uuid = player.getUniqueId();
        canTeleport.put(uuid, true);

        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (!canTeleport.getOrDefault(uuid, false)) {
                    player.sendMessage(ClassyClan.getMessages().get("teleport-cancelled"));
                    cancel();
                    return;
                }

                if (countdown == 0) {
                    player.teleport(base);
                    player.sendMessage(ClassyClan.getMessages().get("teleported-to-base"));
                    canTeleport.remove(uuid);
                    cancel();
                    return;
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(ClassyClan.getMessages().get("teleporting-in", Map.of(
                                "seconds", String.valueOf(countdown),
                                "base_index", String.valueOf(baseIndex)
                        ))));
                countdown--;
            }
        }.runTaskTimer(ClassyClan.getInstance(), 0L, 20L);
    }

    public static boolean canTeleport(UUID uuid) {
        return canTeleport.getOrDefault(uuid, false);
    }

    public static void cancelTeleport(UUID uuid) {
        canTeleport.put(uuid, false);
    }

    @Override
    public String getName() {
        return "base";
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

        int baseIndex = 1;
        if (args.length > 1) {
            try {
                baseIndex = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        Location base = clan.getBase(baseIndex);
        if (base == null) {
            player.sendMessage(ClassyClan.getMessages().get("no-base-nb-set", Map.of(
                    "base_index", String.valueOf(baseIndex)
            )));
            return;
        }

        long now = System.currentTimeMillis();
        long lastUsed = lastTeleport.getOrDefault(playerId, 0L);

        if ((now - lastUsed) < COOLDOWN_MILLIS) {
            long remaining = (COOLDOWN_MILLIS - (now - lastUsed)) / 1000;
            player.sendMessage(ClassyClan.getMessages().get("base-cooldown", Map.of(
                    "time", String.valueOf(remaining)
            )));
            return;
        }

        lastTeleport.put(playerId, now);
        teleportWithCountdown(player, clan, baseIndex);
    }
}
