// src/main/java/org/simpleclan/menu/holder/ClanViewHolder.java
package org.classyclan.menu.clan;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.classyclan.clan.Clan;

/**
 * Holder pour le menu "Voir un autre clan"
 */
public class ClanViewHolder implements InventoryHolder {
    private final Clan clan;
    private Inventory inventory;

    public ClanViewHolder(Clan clan) {
        this.clan = clan;
    }

    /** Le clan dont on affiche les infos */
    public Clan getClan() {
        return clan;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /** À appeler juste après createInventory(...) */
    public void setInventory(Inventory inv) {
        this.inventory = inv;
    }
}
