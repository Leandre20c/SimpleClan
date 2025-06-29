// src/main/java/org/simpleclan/menu/holder/ClanMenuHolder.java
package org.classyclan.menu.clan;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.classyclan.clan.Clan;

public class ClanMenuHolder implements InventoryHolder {
    private final Clan clan;
    private Inventory inventory;  // on gardera l’inventaire créé

    public ClanMenuHolder(Clan clan) {
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
}
