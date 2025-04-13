// 📁 ClanMenu.java
package org.simpleclan.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClanMenu {

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(null, 3 * 9, "§b§lClan Menu");

        // Disband Clan
        ItemStack disband = new ItemStack(Material.TNT);
        ItemMeta disbandMeta = disband.getItemMeta();
        disbandMeta.setDisplayName("§4Disband Clan");
        disband.setItemMeta(disbandMeta);
        menu.setItem(18, disband);

        // Leave Clan
        ItemStack leave = new ItemStack(Material.RED_BED);
        ItemMeta leaveMeta = leave.getItemMeta();
        leaveMeta.setDisplayName("§cLeave Clan");
        leave.setItemMeta(leaveMeta);
        menu.setItem(19, leave);

        // Rename Clan
        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName("§eRename Clan");
        rename.setItemMeta(renameMeta);
        menu.setItem(20, rename);

        // Set base
        ItemStack setbase = new ItemStack(Material.WHITE_BED);
        ItemMeta setbaseMeta = setbase.getItemMeta();
        setbaseMeta.setDisplayName("§eSet Base");
        setbase.setItemMeta(setbaseMeta);
        menu.setItem(12, setbase);

        // Teleport to base
        ItemStack base = new ItemStack(Material.ENDER_PEARL);
        ItemMeta baseMeta = base.getItemMeta();
        baseMeta.setDisplayName("§eTeleport to Base");
        base.setItemMeta(baseMeta);
        menu.setItem(14, base);

        // Open Vault
        ItemStack vault = new ItemStack(Material.TRAPPED_CHEST);
        ItemMeta vaultMeta = vault.getItemMeta();
        vaultMeta.setDisplayName("§eOpen Vault");
        vault.setItemMeta(vaultMeta);
        menu.setItem(16, vault);

        // View Members
        ItemStack members = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta membersMeta = members.getItemMeta();
        membersMeta.setDisplayName("§eView Members");
        members.setItemMeta(membersMeta);
        menu.setItem(10, members);

        player.openInventory(menu);
    }
}
