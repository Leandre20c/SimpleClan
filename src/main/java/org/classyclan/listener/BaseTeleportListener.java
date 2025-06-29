package org.classyclan.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.classyclan.command.sub.BaseSubCommand;

import java.util.UUID;

public class BaseTeleportListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        if (BaseSubCommand.canTeleport(uuid)) {
            if (e.getFrom().distanceSquared(e.getTo()) > 0.001) {
                BaseSubCommand.cancelTeleport(uuid);
            }
        }
    }
}
