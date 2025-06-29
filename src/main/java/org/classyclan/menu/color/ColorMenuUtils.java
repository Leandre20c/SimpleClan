package org.classyclan.menu.color;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Map;

public class ColorMenuUtils {

    public static final Map<ChatColor, Material> COLOR_ITEMS = Map.ofEntries(
            Map.entry(ChatColor.BLACK, Material.BLACK_DYE),
            Map.entry(ChatColor.DARK_BLUE, Material.BLUE_DYE),
            Map.entry(ChatColor.DARK_GREEN, Material.GREEN_DYE),
            Map.entry(ChatColor.DARK_AQUA, Material.CYAN_DYE),
            Map.entry(ChatColor.DARK_RED, Material.RED_DYE),
            Map.entry(ChatColor.DARK_PURPLE, Material.PURPLE_DYE),
            Map.entry(ChatColor.GOLD, Material.ORANGE_DYE),
            Map.entry(ChatColor.GRAY, Material.GRAY_DYE),
            Map.entry(ChatColor.DARK_GRAY, Material.LIGHT_GRAY_DYE),
            Map.entry(ChatColor.BLUE, Material.LIGHT_BLUE_DYE),
            Map.entry(ChatColor.GREEN, Material.LIME_DYE),
            Map.entry(ChatColor.AQUA, Material.LIGHT_BLUE_DYE),
            Map.entry(ChatColor.RED, Material.RED_DYE),
            Map.entry(ChatColor.LIGHT_PURPLE, Material.MAGENTA_DYE),
            Map.entry(ChatColor.YELLOW, Material.YELLOW_DYE),
            Map.entry(ChatColor.WHITE, Material.WHITE_DYE)
    );

    public static Material getMaterialForColor(ChatColor color) {
        return COLOR_ITEMS.get(color);
    }
}
