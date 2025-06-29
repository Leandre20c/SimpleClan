package org.classyclan.banner;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BannerUtils {

    public static ItemStack getDefaultBanner() {
        return new ItemStack(Material.RED_BANNER);
    }

    public static boolean isBanner(ItemStack item) {
        return item != null && item.getType().toString().contains("BANNER") && !item.getType().toString().contains("PATTERN");
    }
}
