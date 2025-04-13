package org.simpleclan.command.sub;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.simpleclan.SimpleClan;
import org.simpleclan.clan.Clan;
import org.simpleclan.clan.ClanManager;

import java.util.Map;

public class BankSubCommand implements SubCommand {

    private final Economy econ = SimpleClan.getEconomy();

    @Override
    public String getName() {
        return "bank";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!ClanManager.get().isInClan(player.getUniqueId())) {
            player.sendMessage(SimpleClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = ClanManager.get().getClan(player.getUniqueId());

        if (args.length == 1) {
            player.sendMessage(SimpleClan.getMessages().get("bank-balance", Map.of(
                    "amount", String.format("%.2f", clan.getBank())
            )));
            return;
        }

        if (args.length == 3) {
            String sub = args[1].toLowerCase();
            double amount;

            try {
                amount = Double.parseDouble(args[2]);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                player.sendMessage(SimpleClan.getMessages().get("invalid-amount"));
                return;
            }

            switch (sub) {
                case "deposit" -> {
                    if (econ.getBalance(player) < amount) {
                        player.sendMessage(SimpleClan.getMessages().get("not-enough-money"));
                        return;
                    }
                    econ.withdrawPlayer(player, amount);
                    clan.deposit(amount);
                    player.sendMessage(SimpleClan.getMessages().get("bank-deposit", Map.of("amount", String.format("%.2f", amount))));
                }
                case "withdraw" -> {
                    if (!clan.getOwner().equals(player.getUniqueId())) {
                        player.sendMessage(SimpleClan.getMessages().get("action-requires-owner"));
                        return;
                    }
                    if (!clan.withdraw(amount)) {
                        player.sendMessage(SimpleClan.getMessages().get("not-enough-in-bank"));
                        return;
                    }
                    econ.depositPlayer(player, amount);
                    player.sendMessage(SimpleClan.getMessages().get("bank-withdraw", Map.of("amount", String.format("%.2f", amount))));
                }
                default -> player.sendMessage(SimpleClan.getMessages().get("bank-usage"));
            }
        } else {
            player.sendMessage(SimpleClan.getMessages().get("bank-usage"));
        }
    }
}