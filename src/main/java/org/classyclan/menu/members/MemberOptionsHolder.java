// src/main/java/org/simpleclan/menu/holder/MemberOptionsHolder.java
package org.classyclan.menu.members;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.classyclan.clan.Clan;

import java.util.UUID;

/**
 * InventoryHolder pour le menu d’options d’un membre.
 * Il stocke :
 *  - le Clan (pour pouvoir y appliquer les actions)
 *  - l’UUID du membre ciblé
 */
public class MemberOptionsHolder implements InventoryHolder {
    private final Clan clan;
    private final UUID memberId;
    private Inventory inventory;

    public MemberOptionsHolder(Clan clan, UUID memberId) {
        this.clan     = clan;
        this.memberId = memberId;
    }

    /** @return le clan auquel appartient le membre */
    public Clan getClan() {
        return clan;
    }

    /** @return l’UUID du membre sur lequel on agit */
    public UUID getMemberId() {
        return memberId;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /** À appeler juste après la création de l’inventaire */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
