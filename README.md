# SimpleClan Plugin

SimpleClan is a powerful, flexible, and user-friendly Minecraft plugin for managing clans. It allows players to form clans, manage permissions and ranks, use clan bases and vaults, and progress through a leveling system that unlocks new features.

## 📦 Features

- Create and manage clans (`/clan create <name> <prefix>`)
- Invite and manage members
- Promote and demote players through ranks:
  - Member
  - Soldier
  - Commander
  - Leader
- Clan bank (Vault economy support)
- Clan bases (multiple teleport locations)
- Clan vault (shared inventory with real-time updates)
- Clan leveling system (1 to 50)
  - Unlock: more homes, more vaults, colors, cosmetics, more members
- Menu Interfaces:
  - Main Menu (`/clan menu`)
  - Settings Menu (`/clan settings`)
  - Color Menu (`/clan color`)
  - Base Menu (`/clan base`)
  - Vault Menu (`/clan vault`)
- Fully translatable messages (messages.yml)
- PlaceholderAPI support

## 🎮 Commands

```bash
/clan create <name> <prefix>
/clan invite <player>
/clan join <clan>
/clan leave
/clan disband
/clan promote <player>
/clan demote <player>
/clan kick <player>
/clan setbase <1-5>
/clan base <1-5>
/clan vault
/clan bank deposit|withdraw <amount>
/clan rename <name> <prefix>
/clan level
/clan levelup
/clan color
/clan members [clan]
/clan info [player]
/clan settings
/clan help
```

## 🔐 Rank Permissions

- Member: `/clan bank deposit`, `/clan base`
- Soldier: + `/clan vault`, `/clan bank withdraw`
- Commander: + `/clan sethome`, `/clan promote`, `/clan demote`, `/clan kick`
- Leader: All permissions

## ⚙️ Configuration

```yaml
leveling:
  max-level: 50
  base-cost: 1000.0
  cost-multiplier: 1.25
  rewards:
    5:
      extra-homes: 1
    10:
      clan-color: "§b"
    15:
      extra-member: 1
    20:
      extra-vaults: 1
    25:
      clan-color: "§6"
    30:
      extra-member: 2
    35:
      clan-color: "§d"
      extra-vaults: 3
    40:
      extra-member: 3
    45:
      extra-homes: 3
    50:
      clan-color: "§4"
      extra-vaults: 3
```

## 🧩 Dependencies

- PaperMC 1.21.4+
- Vault (economy support)
- PlaceholderAPI (for placeholders)

## 📁 File Structure

- `clans/` – stores each clan in a separate YAML file
- `vaults/` – stores clan vaults
- `config.yml` – plugin configuration
- `messages.yml` – translatable message file

## 💡 Tips

- Use `/clan settings` to access rename, help and color menu.
- Color menu only shows unlocked colors.
- Vaults and Bases scale with levels.
- Reloading with `/rl` reloads clans properly from disk.
