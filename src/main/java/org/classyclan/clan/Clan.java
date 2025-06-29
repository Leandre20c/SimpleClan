// 📁 Clan.java
package org.classyclan.clan;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.classyclan.banner.BannerUtils;
import org.classyclan.ranks.ClanRank;

import java.util.*;

public class Clan {
    private String rawName = "";
    private String rawPrefix= "";
    private final UUID owner;
    private final Set<UUID> members;
    private final Map<Integer, Location> bases = new HashMap<>();
    private double bank;
    private final Map<UUID, ClanRank> ranks = new HashMap<>();
    private int level = 1;
    private int extraMembers = 0;
    private int extraHomes = 0;
    private int extraVaults = 0;
    private String color = "§7";
    private Set<String> unlockedColors = new HashSet<>();
    private final Map<Integer, Inventory> vaults = new HashMap<>();
    private ItemStack banner;
    private boolean isOpen;
    private String description;

    public Clan(String name, String prefix, Player owner) {
        this(name, prefix, owner.getUniqueId());
    }

    public Clan(String name, String prefix, UUID owner) {
        this.rawName = name;
        this.rawPrefix = prefix;
        this.owner = owner;
        this.members = new HashSet<>();
        this.members.add(owner);
        this.bank = 0;
        this.ranks.put(owner, ClanRank.LEADER);
        this.banner = BannerUtils.getDefaultBanner();
        this.description = "";
    }

    public String getRawName() {
        if (Objects.equals(rawName, ""))
            return "No clan";
        return rawName;
    }

    public void setName(String name) {
        this.rawName = name;
    }

    public String getRawPrefix() {
        return rawPrefix;
    }

    public void setPrefix(String prefix) {
        this.rawPrefix = prefix;
    }

    public String getColoredName() {
        if (Objects.equals(rawName, ""))
            return "§7No clan";
        return (color != null ? color : "§7") + rawName + "§r";
    }

    public String getColoredPrefix() {
        return (color != null ? color : "§7") + rawPrefix + "§r";
    }

    public UUID getOwner() {
        return owner;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public void setBase(int index, Location location) {
        bases.put(index, location);
    }

    public Location getBase(int index) {
        return bases.get(index);
    }

    public Map<Integer, Location> getAllBases() {
        return bases;
    }

    public double getBank() {
        return bank;
    }

    public Inventory getVault(int index) {
        return vaults.get(index);
    }

    public void setVault(int index, Inventory inv) {
        vaults.put(index, inv);
    }

    public void setBank(double bank) {
        this.bank = bank;
    }

    public void deposit(double amount) {
        this.bank += amount;
    }

    public boolean withdraw(double amount) {
        if (bank >= amount) {
            this.bank -= amount;
            return true;
        }
        return false;
    }

    public ClanRank getRank(UUID playerId) {
        return ranks.getOrDefault(playerId, ClanRank.MEMBER);
    }

    public void setRank(UUID playerId, ClanRank rank) {
        ranks.put(playerId, rank);
    }

    public Map<UUID, ClanRank> getAllRanks() {
        return ranks;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(50, level));
    }

    public int getExtraMembers() {
        return extraMembers;
    }

    public void setExtraMembers(int amount) {
        this.extraMembers = amount;
    }

    public int getExtraHomes() {
        return extraHomes;
    }

    public void setExtraHomes(int amount) {
        this.extraHomes = amount;
    }

    public int getExtraVaults() {
        return extraVaults;
    }

    public void setExtraVaults(int amount) {
        this.extraVaults = amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<String> getUnlockedColors() {
        return unlockedColors;
    }

    public void setUnlockedColors(Set<String> colors) {
        this.unlockedColors = colors;
    }

    public void addUnlockedColor(String colorCode) {
        this.unlockedColors.add(colorCode);
    }

    public ItemStack getBanner() {
        return banner;
    }

    public void setBanner(ItemStack banner) {
        banner.setAmount(1);
        this.banner = banner;
    }

    public boolean isClanOpen(){
        return isOpen;
    }

    public void setClanOpen(){
        isOpen = true;
    }

    public void setClanStatus(boolean _isOpen){
        isOpen = _isOpen;
    }

    public void setClanClosed(){
        isOpen = false;
    }

    public void setDescription(String arg) {
        description = arg;
    }

    public String getDescription() {
        return description;
    }
}
