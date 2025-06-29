package org.classyclan.menu.allClans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.gui.GuiManager;

import java.util.*;
import java.util.stream.Collectors;

public class AllClansMenu {

    private static final int PER_PAGE = 45;

    static final Map<UUID, Integer>    pageMap   = new HashMap<>();
    static final Map<UUID, SortType>  sortMap   = new HashMap<>();
    static final Map<UUID, FilterType> filterMap = new HashMap<>();

    public static void open(Player player) {
        int page   = pageMap.getOrDefault(player.getUniqueId(), 0);
        SortType sort   = sortMap.getOrDefault(player.getUniqueId(), SortType.LEVEL);
        FilterType filter = filterMap.getOrDefault(player.getUniqueId(), FilterType.BOTH);
        open(player, page, sort, filter);
    }

    public static void open(Player player, int page, SortType sort, FilterType filter) {
        GuiManager gui      = ClassyClan.getInstance().getGuiManager();
        ClanManager cm      = ClassyClan.getInstance().getClanManager();
        List<Clan> all      = new ArrayList<>(cm.getAllClans());

        // 1) Filtrer
        List<Clan> filtered = all.stream()
                .filter(c -> switch(filter) {
                    case OPEN   -> c.isClanOpen();
                    case CLOSED -> !c.isClanOpen();
                    default     -> true;
                })
                .collect(Collectors.toList());

        // 2) Trier
        filtered = AllClansMenu.sortClans(filtered, sort);

        // 3) Pagination
        int maxPage = Math.max(0, (filtered.size() - 1) / PER_PAGE);
        page = Math.max(0, Math.min(page, maxPage));

        // 4) Titre et inventaire
        String rawTitle = gui.getString("menus.allClans.title");
        String title = ChatColor.translateAlternateColorCodes('&', rawTitle);
        Inventory inv = Bukkit.createInventory(null, 6 * 9, title);

        // 5) Filler 0–44
        ItemStack filler = gui.getGuiItem("common.filler", Map.of());
        for (int i = 0; i < PER_PAGE; i++) {
            inv.setItem(i, filler);
        }

        // 6) Affichage des clans
        int start = page * PER_PAGE;
        int end   = Math.min(start + PER_PAGE, filtered.size());
        for (int i = start; i < end; i++) {
            Clan clan = filtered.get(i);
            // on clone la bannière
            ItemStack b = clan.getBanner().clone();
            ItemMeta m  = b.getItemMeta();
            m.setDisplayName(
                    ChatColor.COLOR_CHAR + "6" + clan.getRawName()
                            + " " + ChatColor.COLOR_CHAR + "7[" + clan.getColoredPrefix() + "]"
            );
            // lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.COLOR_CHAR + "7Description: ");
            lore.add("§f" + clan.getDescription());
            lore.add("");
            lore.add(ChatColor.COLOR_CHAR + "7Status: "
                    + (clan.isClanOpen() ? ChatColor.COLOR_CHAR + "aOpen"
                    : ChatColor.COLOR_CHAR + "cClosed"));
            lore.add(ChatColor.COLOR_CHAR + "7Level: " + ChatColor.COLOR_CHAR + "f" + clan.getLevel());
            lore.add(ChatColor.COLOR_CHAR + "7Members: " + ChatColor.COLOR_CHAR + "f" + clan.getMembers().size());
            lore.add(ChatColor.COLOR_CHAR + "7Bank: " + ChatColor.COLOR_CHAR + "f"
                    + String.format("%.2f", clan.getBank()) + " PE");
            m.setLore(lore);
            //m.setHideTooltip(true);
            b.setItemMeta(m);

            inv.setItem(i - start, b);
        }

        // 7) Boutons de navigation (slots 45–53)
        inv.setItem(45, gui.getGuiItem(
                "allClans.filterButton",
                Map.of("filter", filter.name())
        ));
        inv.setItem(46, gui.getGuiItem(
                "allClans.sortButton",
                Map.of("sort", sort.name())
        ));
        inv.setItem(48, gui.getGuiItem("allClans.prevPage", Map.of()));
        String manageLabel = cm.isInClan(player.getUniqueId())
                ? ClassyClan.getInstance().getGuiManager().getString("items.allClans.manageClan")
                : ClassyClan.getInstance().getGuiManager().getString("items.allClans.createClan");
        inv.setItem(49, gui.getGuiItem(
                "allClans.manageClan",
                Map.of("manageLabel", manageLabel)
        ));
        inv.setItem(50, gui.getGuiItem("allClans.nextPage", Map.of()));

        // 8) Sauvegarde de l’état
        pageMap.put(player.getUniqueId(), page);
        sortMap.put(player.getUniqueId(), sort);
        filterMap.put(player.getUniqueId(), filter);

        // 9) On ouvre !
        player.openInventory(inv);
    }

    static List<Clan> sortClans(List<Clan> list, SortType sort) {
        return switch (sort) {
            case LEVEL -> list.stream()
                    .sorted(Comparator.comparingInt(Clan::getLevel).reversed()
                            .thenComparing(Clan::getRawName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
            case MEMBERS -> list.stream()
                    .sorted(Comparator.comparingInt((Clan c) -> c.getMembers().size()).reversed()
                            .thenComparing(Clan::getRawName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
            case BANK -> list.stream()
                    .sorted(Comparator.comparingDouble(Clan::getBank).reversed()
                            .thenComparing(Clan::getRawName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        };
    }
}
