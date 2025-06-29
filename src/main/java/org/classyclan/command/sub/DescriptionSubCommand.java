package org.classyclan.command.sub;

import org.bukkit.entity.Player;
import org.classyclan.ClassyClan;
import org.classyclan.clan.Clan;
import org.classyclan.clan.ClanManager;
import org.classyclan.storage.ClanStorage;

import java.util.Arrays;
import java.util.UUID;

import static org.classyclan.ranks.ClanPermissionUtil.checkPermission;

public class DescriptionSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "description";
    }

    @Override
    public void execute(Player player, String[] args) {
        UUID playerId = player.getUniqueId();
        ClanManager manager = ClassyClan.getInstance().getClanManager();

        // 1) Vérifier qu'il est dans un clan
        if (!manager.isInClan(playerId)) {
            player.sendMessage(ClassyClan.getMessages().get("not-in-clan"));
            return;
        }

        // 2) Vérifier la permission
        Clan clan = manager.getClan(playerId);
        if (!checkPermission(player, clan, "clan.settings.setdescription")) {
            return;
        }

        // 3) Vérifier la syntaxe
        if (args.length < 2) {
            player.sendMessage("§cUsage: /clan description <texte de la description>");
            return;
        }

        // 4) Construire la description (plusieurs mots possibles)
        String newDesc = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();

        if (newDesc.length() > ClassyClan.getInstance().getConfig().getInt("max-clan-description-length")) {
            player.sendMessage(ClassyClan.getMessages().get("description-too-long"));
            return;
        }

        // 5) Déterminer si on modifie ou crée
        boolean isModifying = clan.getDescription() != null && !clan.getDescription().isEmpty();

        // 6) Appliquer et sauvegarder
        clan.setDescription(newDesc);
        ClanStorage.saveClan(clan);

        // 7) Retour au joueur
        if (isModifying) {
            player.sendMessage(ClassyClan.getMessages().get("description-changed",
                    java.util.Map.of("description", newDesc)
            ));
        } else {
            player.sendMessage(ClassyClan.getMessages().get("description-set",
                    java.util.Map.of("description", newDesc)
            ));
        }
    }
}
