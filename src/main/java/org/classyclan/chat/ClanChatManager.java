package org.classyclan.chat;

import java.util.HashSet;
import java.util.UUID;

public class ClanChatManager {

    private final HashSet<UUID> clanChatPlayers = new HashSet<>();

    public void toggleClanChat(UUID playerUUID) {
        if (!clanChatPlayers.remove(playerUUID)) {
            clanChatPlayers.add(playerUUID);
        }
    }

    public boolean isInClanChat(UUID playerUUID) {
        return clanChatPlayers.contains(playerUUID);
    }

    public void disableClanChat(UUID playerUUID) {
        clanChatPlayers.remove(playerUUID);
    }
}

