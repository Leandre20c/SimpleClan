package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;

import java.util.Map;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class BankSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "bank";
    }

    @Override
    public void execute(Player player, String[] args) {
        ClanManager clanManager = ClassyClan.getInstance().getClanManager();

        if (!clanManager.isInClan(player.getUniqueId())) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());
        String clanBalance = String.format("%.2f", clan.getBank());

        // -- Cas 1: /clan bank ou /clan bank balance
        if (args.length < 2
                || args[1].equalsIgnoreCase("balance")
                || args[1].equalsIgnoreCase("bal")
        ) {
            player.sendMessage(ClassyClan.getMessages().get("bank-balance", Map.of(
                    "amount", clanBalance
            )));
            return;
        }

        String sub = args[1].toLowerCase();
        // -- Cas 2: deposit / withdraw sans montant
        if ((sub.equals("deposit") || sub.equals("withdraw")) && args.length < 3) {
            player.sendMessage(ClassyClan.getMessages().get("bank-usage"));
            return;
        }

        double amount;
        boolean isAll = args[2].equalsIgnoreCase("all");

        switch (sub) {
            case "deposit" -> {
                if (!checkPermission(player, clan, "clan.bank.deposit")) return;

                double playerBalance = ClassyClan.getEconomy().getBalance(player);
                amount = isAll ? playerBalance : parseAmount(args[2], player);
                if (amount <= 0 || playerBalance < amount) {
                    player.sendMessage(ClassyClan.getMessages().get("not-enough-money"));
                    return;
                }

                ClassyClan.getEconomy().withdrawPlayer(player, amount);
                clan.setBank(clan.getBank() + amount);
                player.sendMessage(ClassyClan.getMessages().get("bank-deposit", Map.of(
                        "amount", String.format("%.2f", amount)
                )));
            }
            case "withdraw" -> {
                if (!checkPermission(player, clan, "clan.bank.withdraw")) return;

                double clanBal = clan.getBank();
                amount = isAll ? clanBal : parseAmount(args[2], player);
                if (amount <= 0 || clanBal < amount) {
                    player.sendMessage(ClassyClan.getMessages().get("not-enough-clan-money"));
                    return;
                }

                ClassyClan.getEconomy().depositPlayer(player, amount);
                clan.setBank(clanBal - amount);
                player.sendMessage(ClassyClan.getMessages().get("bank-withdraw", Map.of(
                        "amount", String.format("%.2f", amount)
                )));
            }
            default -> {
                // toute autre sous‐commande invalide → usage
                player.sendMessage(ClassyClan.getMessages().get("bank-usage"));
            }
        }
    }

    private double parseAmount(String input, Player player) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            player.sendMessage(ClassyClan.getMessages().get("invalid-amount"));
            return -1;
        }
    }
}
