// 📁 ClanStorage.java
package org.classyclan.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.classyclan.ClassyClan;
import org.classyclan.banner.BannerUtils;
import org.classyclan.clan.Clan;
import org.classyclan.ranks.ClanRank;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ClanStorage {

    private static final File folder = new File(ClassyClan.getInstance().getDataFolder(), "clans");

    public static void saveClan(Clan clan) {
        if (!folder.exists()) folder.mkdirs();
        File file = new File(folder, clan.getRawName().toLowerCase() + ".yml");

        FileConfiguration config = new YamlConfiguration();
        config.set("name", clan.getRawName());
        config.set("prefix", clan.getRawPrefix());
        config.set("owner", clan.getOwner().toString());
        config.set("members", clan.getMembers().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set("bank", clan.getBank());
        config.set("level", clan.getLevel());
        config.set("extraMembers", clan.getExtraMembers());
        config.set("extraHomes", clan.getExtraHomes());
        config.set("extraVaults", clan.getExtraVaults());
        config.set("color", clan.getColor());
        config.set("unlocked-colors", new ArrayList<>(clan.getUnlockedColors()));
        config.set("extraVaults", clan.getExtraVaults());
        config.set("banner", clan.getBanner());
        config.set("description", clan.getDescription());
        config.set("status", clan.isClanOpen());


        Map<String, String> ranks = new HashMap<>();
        for (UUID member : clan.getMembers()) {
            ranks.put(member.toString(), clan.getRank(member).name());
        }
        config.createSection("ranks", ranks);

        ConfigurationSection baseSection = config.createSection("bases");
        for (Map.Entry<Integer, Location> entry : clan.getAllBases().entrySet()) {
            Location base = entry.getValue();
            ConfigurationSection sub = baseSection.createSection(String.valueOf(entry.getKey()));
            sub.set("world", base.getWorld().getName());
            sub.set("x", base.getX());
            sub.set("y", base.getY());
            sub.set("z", base.getZ());
            sub.set("yaw", base.getYaw());
            sub.set("pitch", base.getPitch());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Clan loadClan(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String name = config.getString("name");
        String prefix = config.getString("prefix");
        UUID owner = UUID.fromString(config.getString("owner"));
        List<String> membersStr = config.getStringList("members");
        Set<UUID> members = membersStr.stream().map(UUID::fromString).collect(Collectors.toSet());
        members.add(owner);
        double bank = config.getDouble("bank", 0);
        int level = config.getInt("level", 1);

        Clan clan = new Clan(name, prefix, owner);

        if (config.contains("bases")) {
            ConfigurationSection baseSection = config.getConfigurationSection("bases");
            for (String key : baseSection.getKeys(false)) {
                try {
                    int index = Integer.parseInt(key);
                    ConfigurationSection sub = baseSection.getConfigurationSection(key);
                    String world = sub.getString("world");
                    double x = sub.getDouble("x");
                    double y = sub.getDouble("y");
                    double z = sub.getDouble("z");
                    float yaw = (float) sub.getDouble("yaw");
                    float pitch = (float) sub.getDouble("pitch");
                    Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    clan.setBase(index, loc);
                } catch (Exception ignored) {}
            }
        }
        members.forEach(clan::addMember);
        clan.setBank(bank);
        clan.setLevel(level);
        clan.setExtraMembers(config.getInt("extraMembers", 0));
        clan.setExtraHomes(config.getInt("extraHomes", 0));
        clan.setExtraVaults(config.getInt("extraVaults", 0));
        clan.setColor(config.getString("color"));
        clan.setExtraVaults(config.getInt("extraVaults", 0));
        ItemStack banner = config.getItemStack("banner");
        clan.setBanner(banner != null ? banner : BannerUtils.getDefaultBanner());
        String desc = config.getString("description", "");
        clan.setDescription(desc);
        clan.setClanStatus(Boolean.parseBoolean(config.getString("status")));


        if (config.contains("unlocked-colors")) {
            List<String> unlocked = config.getStringList("unlocked-colors");
            clan.setUnlockedColors(new HashSet<>(unlocked));
        }


        if (clan.getUnlockedColors().isEmpty()) {
            for (String levelStr : ClassyClan.getInstance().getConfig().getConfigurationSection("leveling.rewards").getKeys(false)) {
                int required_level = Integer.parseInt(levelStr);
                if (clan.getLevel() >= required_level) {
                    String color = ClassyClan.getInstance().getConfig().getString("leveling.rewards." + levelStr + ".clan-color");
                    if (color != null) {
                        clan.addUnlockedColor(color);
                    }
                }
            }
        }

        if (config.contains("ranks")) {
            ConfigurationSection section = config.getConfigurationSection("ranks");
            for (String key : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    ClanRank rank = ClanRank.valueOf(section.getString(key));
                    clan.setRank(uuid, rank);
                } catch (Exception ignored) {}
            }
        }

        return clan;
    }

    public static List<Clan> loadAllClans() {
        if (!folder.exists()) return new ArrayList<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return new ArrayList<>();

        List<Clan> clans = new ArrayList<>();
        for (File file : files) {
            Clan clan = loadClan(file);
            if (clan != null) clans.add(clan);
        }
        return clans;
    }

    public static void deleteClan(Clan clan) {
        File file = new File(folder, clan.getRawName().toLowerCase() + ".yml");
        if (file.exists()) file.delete();
    }

}
