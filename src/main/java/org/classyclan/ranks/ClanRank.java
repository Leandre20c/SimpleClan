package org.classyclan.ranks;

import java.util.List;

public enum ClanRank {
    MEMBER,
    TRUSTED,
    SOLDIER,
    COMMANDER,
    LEADER;

    private RankConfig config;

    public void loadConfig(RankConfig config) {
        this.config = config;
    }

    public String getDisplayName() {
        return config != null ? config.getDisplayName() : name();
    }

    public boolean hasPermission(String permission) {
        return config != null && config.getPermissions().contains(permission);
    }

    public List<String> getDescription() {
        return config != null ? config.getDescription() : List.of("§cErreur: description manquante");
    }

    public boolean isOrHigher(ClanRank other) {
        return this.ordinal() >= other.ordinal();
    }

    public boolean hasInheritedPermission(String permission) {
        for (ClanRank rank : ClanRank.values()) {
            if (this.isOrHigher(rank) && rank.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
}


