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

| Command | Description | Permission |
| --- | --- | --- |
| `/advancements` | Displays a chronological list of all earned advancements. | *(none)* |
| `/agui` | Opens the graphical user interface for all advancements. | *(none)* |
| `/achallenge start` | Starts a reset or ended challenge. | `challenge.admin.start` |
| `/achallenge pause` | Pauses the running challenge. | `challenge.admin.pause` |
| `/achallenge resume`| Resumes a paused challenge. | `challenge.admin.pause` |
| `/achallenge stop confirm` | Ends the challenge, keeping data for review. | `challenge.admin.stop` |
| `/achallenge reset confirm`| Resets all player and plugin data. | `challenge.admin.reset` |
| `/achallenge lang <de\|en\|pl>` | Changes the plugin's language live. | `challenge.admin.lang` |
| `/achallenge autosave <seconds>` | Changes the auto-save interval live. | `challenge.admin.autosave` |
| `/achallenge reload` | Reloads the config and language files. | `challenge.admin.reload` |
| *(Permission Only)* | Allows moving/interacting while paused. **(OPs by default)** | `challenge.admin.bypasspause`|

## 🔄 The Full World Reset

The `/achallenge reset` command resets all player and plugin data. For a **completely fresh world** for the next round, the following 100% safe process is recommended:

1.  **🎮 In-Game Reset**
    * Run the command `/achallenge reset confirm`.
    * This will reset all player stats, clear the history, and prepare the plugin for a new round. The plugin will guide you with further instructions in chat.

2.  **🛑 Stop the Server**
    * Shut down the server completely and safely by typing `stop` into your server console.

3.  **🗑️ Delete World Folders**
    * Open your main server directory.
    * Delete the following folders: `world`, `world_nether`, and `world_the_end`.

4.  **▶️ Restart the Server**
    * Start your server again. Minecraft will automatically generate brand-new, fresh worlds.

5.  **🚀 Start the Challenge**
    * Once everyone is on the server, use `/achallenge start` to begin the new challenge!
