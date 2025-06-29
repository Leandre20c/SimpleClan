// src/main/java/org/simpleclan/menu/holder/MembersMenuHolder.java
package org.classyclan.menu.members;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.classyclan.clan.Clan;

import java.util.List;
import java.util.UUID;

public class MembersMenuHolder implements InventoryHolder {
    private final Clan clan;
    private Inventory inventory;
    private List<UUID> orderedMembers;

    public MembersMenuHolder(Clan clan) {
        this.clan = clan;
    }

    public Clan getClan() {
        return clan;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inv) {
        this.inventory = inv;
    }

    public void setOrderedMembers(List<UUID> orderedMembers) {
        this.orderedMembers = orderedMembers;
    }
    public List<UUID> getOrderedMembers() {
        return orderedMembers;
    }
}
