![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

# SimpleClan

**SimpleClan** is a modular and lightweight clan management plugin for Minecraft servers running Paper 1.21.4.  
It provides essential features for player communities including homes, vaults, banks, ranks, and full persistence.

---

## 🔧 Features

- Clan creation with unique names and custom prefixes
- Member invitation and join system
- Clan homes and teleportation
- Shared clan chest and private vault (real-time sync)
- Vault-based economy integration (clan bank)
- Persistent data storage per clan (YAML)
- Clan prefix display in chat and tablist
- Interactive paginated clan listing
- Internal rank system: Member, Officer, Leader

---

## 📥 Installation

1. Download the latest release.
2. Place the `.jar` file into your server's `/plugins` directory.
3. Restart your server.
4. Configuration and clan data will be auto-generated.

---

## ⚙️ Requirements

- Minecraft: **Paper 1.21.4**
- Java: **17+**
- Economy support: **Vault API**

---

## 💻 Commands Overview

| Command                            | Description                                 |
|------------------------------------|---------------------------------------------|
| `/clan create <name> <prefix>`     | Creates a new clan                          |
| `/clan invite <player>`            | Invites a player to your clan               |
| `/clan join`                       | Joins the clan you were invited to          |
| `/clan list`                       | Lists existing clans (with pagination)      |
| `/clan home`                       | Teleports to the clan home                  |
| `/clan vault`                      | Opens the private vault                     |
| `/clan bank`                       | Views and manages clan bank                 |
| `/clan promote <player>`           | Promotes a member to officer or leader      |
| `/clan demote <player>`            | Demotes a member to a lower rank            |
| `/clan leave`                      | Leaves the current clan                     |

> Permissions support coming soon

---

## 📁 File Structure

```
SimpleClan/
├── config.yml
└── clans/
    ├── <ClanName>.yml
    └── ...
```

Each clan file contains:

- Owner UUID
- Member list
- Ranks and roles
- Home location
- Vault contents
- Bank balance

---

## 🛣️ Planned Features

- Admin tools and moderation commands
- Clan GUI menus (invites, vault, settings)
- War and alliance system
- Message and language customization
- Developer API for integration

---

## 🧑‍💼 About the Project

SimpleClan is maintained by [Leandre20c](https://github.com/Leandre20c).  
The plugin is designed to be extensible and production-ready for any Minecraft survival or RPG server.

---

## 📬 Contributing

Pull requests and suggestions are welcome.  
For bug reports or feature requests, please use the [Issues](https://github.com/Leandre20c/SimpleClan/issues) tab.
