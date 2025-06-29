package org.classyclan.ranks;

import java.util.List;

public class RankConfig {
    private final String displayName;
    private final List<String> permissions;
    private final List<String> description;

    public RankConfig(String displayName, List<String> permissions, List<String> description) {
        this.displayName = displayName;
        this.permissions = permissions;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getDescription() {
        return description;
    }
}


