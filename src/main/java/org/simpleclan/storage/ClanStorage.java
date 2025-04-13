// 📁 ClanStorage.java
package org.simpleclan.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.simpleclan.clan.Clan;
import org.simpleclan.SimpleClan;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ClanStorage {

    private static final File folder = new File(SimpleClan.getInstance().getDataFolder(), "clans");

    public static void saveClan(Clan clan) {
        if (!folder.exists()) folder.mkdirs();
        File file = new File(folder, clan.getName().toLowerCase() + ".yml");

        FileConfiguration config = new YamlConfiguration();
        config.set("name", clan.getName());
        config.set("prefix", clan.getPrefix());
        config.set("owner", clan.getOwner().toString());
        config.set("members", clan.getMembers().stream().map(UUID::toString).collect(Collectors.toList()));
        config.set("bank", clan.getBank());

        if (clan.getBase() != null) {
            Location base = clan.getBase();
            config.set("base.world", base.getWorld().getName());
            config.set("base.x", base.getX());
            config.set("base.y", base.getY());
            config.set("base.z", base.getZ());
            config.set("base.pitch", base.getPitch());
            config.set("base.yaw", base.getYaw());
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
        double bank = config.getDouble("bank", 0);

        Location base = null;
        if (config.contains("base.world")) {
            String world = config.getString("base.world");
            double x = config.getDouble("base.x");
            double y = config.getDouble("base.y");
            double z = config.getDouble("base.z");
            float pitch = (float) config.getDouble("base.pitch");
            float yaw = (float) config.getDouble("base.yaw");
            base = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }

        Clan clan = new Clan(name, prefix, owner);
        members.forEach(clan::addMember);
        clan.setBase(base);
        clan.setBank(bank);
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
}
