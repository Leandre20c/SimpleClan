# ClassyClan Plugin

ClassyClan is a powerful, flexible, and user-friendly Minecraft plugin for managing clans. It allows players to form clans, manage permissions and ranks, use clan bases and vaults, and progress through a leveling system that unlocks new features.

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
  - Base Menu (`/clan bases`)
  - Vault Menu (`/clan vaults`)
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
/clan vault <1-5>
/clan bank deposit|withdraw <amount>|all
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
# ──────────────── ⚙️ GENERAL SETTINGS ────────────────

# Default max members allowed per clan (0 = no limit)
max-members-per-clan: 10

# Cooldown in seconds between each /clan base teleport
base-teleport-cooldown: 5


# ──────────────── 🏷️ NAME/PREFIX RULES ────────────────

# Max characters for a clan name (recommended: 16)
max-clan-name-length: 16

# Max characters for a clan prefix (recommended: 2-3)
max-clan-prefix-length: 2

# Allowed characters regex (for name and prefix, leave empty to allow anything)
# Example: ^[A-Za-z0-9_]+$ = letters, numbers, underscores only
allowed-name-characters: "^[A-Za-z0-9_]+$"

# ──────────────── ⚔️ CLAN LEVELING CONFIG ────────────────
leveling:
  max-level: 50
  base-cost: 1000.0
  linear-increment: 500.0
  rewards: #Availables : clan-color, extra-member, extra-homes, extra-vaults
    1:
      clan-color: "§4"
    5:
      extra-homes: 1
      clan-color: "§c"
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
      clan-color: "§a "
      extra-vaults: 3


```

# 📄 PlaceholderAPI Support

SimpleClan fully supports **PlaceholderAPI** to allow you to display clan information anywhere you want (scoreboards, tablists, signs, holograms, etc.).

To use the placeholders, make sure you have installed [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) on your server.

## ✅ Available Placeholders

You can use the following placeholders:

| Placeholder | Description |
|:---|:---|
| `%simpleclan_tag%` | Displays the clan prefix with colors. |
| `%simpleclan_raw_tag%` | Displays the clan prefix without colors. |
| `%simpleclan_name%` | Displays the clan name with colors. |
| `%simpleclan_raw_name%` | Displays the clan name without colors. |
| `%simpleclan_owner%` | Displays the name of the clan owner. |
| `%simpleclan_bank%` | Displays the current amount of money in the clan bank. |
| `%simpleclan_level%` | Displays the clan level. |
| `%simpleclan_extra_members%` | Displays the number of extra members unlocked. |
| `%simpleclan_extra_basess%` | Displays the number of extra homes unlocked. |
| `%simpleclan_extra_vaults%` | Displays the number of extra vaults unlocked. |
| `%simpleclan_color%` | Displays the color code currently set for the clan. |
| `%simpleclan_member_count%` | Displays the number of members in the clan. |
| `%simpleclan_rank%` | Displays the player's rank inside the clan. |
| `%simpleclan_is_leader%` | Displays `true` if the player is the clan owner, otherwise `false`. |
| `%simpleclan_members%` | Displays a comma-separated list of all clan members' names. |

## 🔧 Example Usage

- In your scoreboard plugin: `Clan: %simpleclan_name%`
- In your tablist plugin: `[%simpleclan_tag%] %player_name%`
- On a sign or hologram: `Bank: %simpleclan_bank%$`

## 📢 Important

- Some placeholders require the player to be part of a clan to display information.
- If the player is not in a clan, placeholders may return empty or default values.

Enjoy displaying your clan data anywhere!

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
- Color menu only shows unlockable colors.
- Vaults and Bases scale with levels.
- Reloading with `/rl` reloads clans properly from disk.

## Incoming

- A level indicator menu
- Translations files
- More configurations
- Clan chat
