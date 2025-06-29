package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.classyclan.ClassyClan;
import org.classyclan.banner.BannerUtils;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.storage.ClanStorage;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class BannerSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "banner";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ClassyClan.getMessages().get("banner-usage"));
            return;
        }

        ClanManager clanManager = ClassyClan.getInstance().getClanManager();
        Clan clan = clanManager.getClan(player.getUniqueId());

        if (clan == null) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "set":
                if (!checkPermission(player, clan, "clan.banner.set")) return;

                ItemStack bannerInHand = player.getInventory().getItemInMainHand();
                if (!BannerUtils.isBanner(bannerInHand)) {
                    player.sendMessage(ClassyClan.getMessages().get("error-not-banner-in-hand"));
                    return;
                }

                clan.setBanner(bannerInHand.clone());
                ClanStorage.saveClan(clan);
                player.sendMessage(ClassyClan.getMessages().get("banner-defined"));
                break;

            case "get":
                if (!checkPermission(player, clan, "clan.banner.get")) return;

                PlayerInventory inventory = player.getInventory();
                ItemStack banner = clan.getBanner();
                ItemMeta meta = banner.getItemMeta();

                String clanName = clan.getRawName();
                meta.setDisplayName(clanName);
                meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

                if (inventory.firstEmpty() == -1) {
                    player.sendMessage(ClassyClan.getMessages().get("inventory-full"));
                    return;
                }

                inventory.addItem(banner.clone());
                player.sendMessage(ClassyClan.getMessages().get("banner-received"));
                break;

            default:
                player.sendMessage(ClassyClan.getMessages().get("usage-banner"));
                break;
        }
    }
}
