package org.simpleclan.clan;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Clan {
    private String name;
    private String prefix;
    private final UUID owner;
    private final Set<UUID> members;
    private Location base;
    private double bank;

    public Clan(String name, String prefix, Player owner) {
        this(name, prefix, owner.getUniqueId());
    }

    public Clan(String name, String prefix, UUID owner) {
        this.name = name;
        this.prefix = prefix;
        this.owner = owner;
        this.members = new HashSet<>();
        this.members.add(owner);
        this.bank = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    public Location getBase() {
        return base;
    }

    public void setBase(Location base) {
        this.base = base;
    }

    public double getBank() {
        return bank;
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
}