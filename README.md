[English](./README.md) | [Deutsch](./README_de.md) | [Polski](./README_pl.md)
***

# All Advancements Challenge

![Java Version](https://img.shields.io/badge/Java-17+-blue.svg) ![API Version](https://img.shields.io/badge/API-Spigot%201.21-orange.svg) ![License](https://img.shields.io/badge/License-MIT-green.svg)

A comprehensive Spigot plugin designed to manage and enhance an "All Advancements" challenge between players. It provides a dynamic user interface, full administrative control, and a robust tracking system to make the experience as seamless as possible.

## ✨ Features

* **🏆 Progress Tracking:** A dynamic Boss Bar displays global progress and a running timer.
* **💾 Persistent Storage:** All progress, the timer, and the current challenge state are automatically saved, surviving server restarts and crashes.
* **🌐 Multi-Language Support:** Full support for German, English, and Polish. The language can be changed live in-game or via the configuration file.
* **🖥️ Graphical User Interface (GUI):** An interactive, paginated menu (`/agui`) to view all advancements. Completed achievements are highlighted and show details like the player and timestamps.
* **👑 Full Admin Control:** A complete set of commands to manage the challenge lifecycle:
    * **Start:** Begins the challenge and disables vanilla advancement announcements for a cleaner chat.
    * **Pause/Resume:** Completely freezes the game (timer, players, mobs) and seamlessly resumes it.
    * **Stop:** Gracefully ends the challenge while keeping all data for review.
    * **Reset:** A powerful reset command for all player and plugin data, complete with a confirmation step.
* **⚙️ Fully Configurable:** Customize the Boss Bar color, messages, and technical settings via a simple `config.yml`.
* **🎨 Polished Notifications:** All important events are communicated through beautifully formatted, clear messages in chat.

## 🚀 Installation

1.  Download the latest `.jar` file from the [Releases Page](https://github.com/jayyy-idk/AllAdvancementsChallenge/releases/tag/v.0.1.9).
2.  Place the `.jar` file into your Spigot/Paper server's `plugins` folder.
3.  Restart or reload your server.
4.  The plugin will generate an `AllAchievementsChallenge` folder containing the configuration and language files.

## 🛠️ Configuration

The configuration can be adjusted in the `plugins/AllAchievementsChallenge/config.yml` file.

```yml
# Configuration file for the AllAchievementsChallenge Plugin

# What language should be used?
# Available: de, en, pl (default is en).
language: 'en'

# Settings for the Bossbar
bossbar:
  # What color should the bossbar be?
  # Valid values: BLUE, GREEN, PINK, PURPLE, RED, WHITE, YELLOW
  color: 'YELLOW'

# Settings for messages
messages:
  # What prefix should be in front of the advancement announcement?
  # You can use color codes with '&' (e.g., &6 for gold).
  prefix: '&6[Advancements!]'

# How many seconds should be between automatic saves?
# Values that are too low (below 5) can impact performance.
autosave-interval-seconds: 10
```

## 💻 Commands & Permissions
`/advancements`

Description: Displays a chronological list of all earned advancements.
Permission: `None`
`/agui`

Description: Opens the graphical user interface for all advancements.
Permission: `None`
`/achallenge start`

Description: Starts a reset or ended challenge.
Permission: `challenge.admin.start`
`/achallenge pause`

Description: Pauses the running challenge.
Permission: `challenge.admin.pause`
`/achallenge resume`

Description: Resumes a paused challenge.
Permission: `challenge.admin.pause`
`/achallenge stop confirm`

Description: Ends the challenge, keeping data for review.
Permission: `challenge.admin.stop`
`/achallenge reset confirm`

Description: Resets all player and plugin data.
Permission: `challenge.admin.reset`
`/achallenge lang <de|en|pl>`

Description: Changes the plugin's language live.
Permission: `challenge.admin.lang`
`/achallenge autosave <seconds>`

Description: Changes the auto-save interval live.
Permission: `challenge.admin.autosave`
`/achallenge reload`

Description: Reloads the config and language files.
Permission: `challenge.admin.reload`

Permission: `challenge.admin.bypasspause`
Description: Allows a player to move and interact while the challenge is paused. (Active for OPs by default)

## 🔄 The Full World Reset
The `/achallenge reset` command resets all player and plugin data. For a completely fresh world for the next round, the following 100% safe process is recommended:

1. In-Game: Run `/achallenge reset confirm`. The plugin will reset all data and enter the WAITING_TO_START state. The command will also display the next steps in chat.
2. Stop Server: Shut down the server cleanly using the stop command in the console.
3. Delete Folders: In your main server directory, delete the folders: world, world_nether, and world_the_end.
4. Restart Server: The server will automatically generate brand-new, fresh worlds.
5. Start Challenge: Once the server is online, use `/achallenge start` to begin!
