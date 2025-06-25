[English](./README.md) | [Deutsch](./README_de.md) | [Polski](./README_pl.md)
***

# All Advancements Challenge

![Java Version](https://img.shields.io/badge/Java-17+-blue.svg) ![API Version](https://img.shields.io/badge/API-Spigot%201.21-orange.svg) ![License](https://img.shields.io/badge/License-MIT-green.svg)

Ein umfassendes Spigot-Plugin, das entwickelt wurde, um eine "All Advancements"-Challenge zwischen Spielern zu verwalten und zu verbessern. Es bietet eine dynamische Benutzeroberfläche, administrative Kontrolle und ein robustes Tracking-System, um das Erlebnis so nahtlos wie möglich zu gestalten.

## ✨ Features

* **🏆 Fortschritts-Tracking:** Eine dynamische Bossbar zeigt den globalen Fortschritt und einen laufenden Timer an.
* **💾 Persistente Speicherung:** Alle Fortschritte, der Timer und der Challenge-Status werden automatisch gespeichert und überleben Server-Neustarts und Crashes.
* **🌐 Mehrsprachigkeit:** Volle Unterstützung für Deutsch, Englisch und Polnisch. Die Sprache kann live im Spiel oder über die Konfigurationsdatei geändert werden.
* **🖥️ Grafische Übersicht (GUI):** Ein interaktives, mehrseitiges Menü (`/agui`), um alle Advancements zu sehen. Erledigte Erfolge werden farblich hervorgehoben und mit Details (Spieler, Zeit) versehen.
* **👑 Admin-Kontrolle:** Ein komplettes Set an Befehlen, um die Challenge zu verwalten:
    * **Start:** Beginnt die Challenge und deaktiviert störende Standard-Nachrichten.
    * **Pause/Resume:** Friert das Spiel komplett ein (Timer, Spieler, Mobs) und setzt es nahtlos fort.
    * **Stop:** Beendet die Challenge, während der Fortschritt zur Ansicht erhalten bleibt.
    * **Reset:** Setzt die gesamte Challenge für einen Neustart zurück, inklusive aller Spielerdaten.
* **⚙️ Vollständig Konfigurierbar:** Passe die Bossbar-Farbe, Nachrichten und technische Einstellungen über eine einfach zu bedienende `config.yml` an.
* **🎨 Professionelle Benachrichtigungen:** Alle wichtigen Ereignisse werden durch schön formatierte, klare Nachrichten im Chat kommuniziert.
* **🎉 Großes Finale:** Eine konfigurierbare "Sieges"-Bossbar, eine serverweite Ankündigung und ein Feuerwerk feiern den Abschluss der Challenge.

## 🚀 Installation

1.  Lade die neueste `.jar`-Datei von der [Releases-Seite](https://github.com/jayyy-idk/AllAdvancementsChallenge/releases) herunter.
2.  Platziere die `.jar`-Datei in den `plugins`-Ordner deines Spigot/Paper-Servers.
3.  Starte oder reloade deinen Server.
4.  Das Plugin wird einen `AllAchievementsChallenge`-Ordner mit den Konfigurations- und Sprachdateien erstellen.

## 🛠️ Konfiguration

Die Konfiguration kann in der Datei `plugins/AllAchievementsChallenge/config.yml` angepasst werden.

```yml
# Konfigurationsdatei für das AllAchievementsChallenge Plugin

# Welche Sprache soll benutzt werden?
# Verfügbar: de, en, pl (Standard ist en).
language: 'de'

# Einstellungen für die Bossbar
bossbar:
  # Welche Farbe soll die Bossbar haben?
  # Gültige Werte: BLUE, GREEN, PINK, PURPLE, RED, WHITE, YELLOW
  color: 'YELLOW'
  # Welche Farbe soll die Bossbar haben, wenn die Challenge abgeschlossen ist?
  completion-color: 'GREEN'

# Einstellungen für Nachrichten
messages:
  # Welcher Präfix soll vor der Nachricht stehen, wenn jemand ein Advancement erreicht?
  # Du kannst Farbcodes mit '&' benutzen (z.B. &6 für Gold).
  prefix: '&6[Advancements!]'

# Wie viele Sekunden sollen zwischen den automatischen Speicherungen liegen?
# Werte, die zu niedrig sind (unter 5), können die Leistung beeinträchtigen.
autosave-interval-seconds: 10
```

## 💻 Befehle & Berechtigungen

| Befehl                               | Beschreibung                                                                  | Permission                        |
| ------------------------------------ | ----------------------------------------------------------------------------- | --------------------------------- |
| `/advancements`                      | Zeigt eine chronologische Liste aller erreichten Advancements.                | *(keine)* |
| `/agui`                              | Öffnet die grafische Übersicht aller Advancements.                            | *(keine)* |
| `/achallenge start`                  | Startet eine zurückgesetzte oder beendete Challenge.                          | `challenge.admin.start`           |
| `/achallenge pause`                  | Pausiert die laufende Challenge.                                              | `challenge.admin.pause`           |
| `/achallenge resume`                 | Setzt eine pausierte Challenge fort.                                          | `challenge.admin.pause`           |
| `/achallenge stop confirm`           | Beendet die Challenge, behält aber die Daten zur Ansicht.                     | `challenge.admin.stop`            |
| `/achallenge reset confirm`          | Setzt alle Spieler- und Plugin-Daten zurück.                                  | `challenge.admin.reset`           |
| `/achallenge lang <de\|en\|pl>`      | Ändert die Sprache des Plugins live.                                          | `challenge.admin.lang`            |
| `/achallenge autosave <sekunden>`    | Ändert das Auto-Save-Intervall live.                                          | `challenge.admin.autosave`        |
| `/achallenge reload`                 | Lädt die Konfigurations- und Sprachdateien neu.                               | `challenge.admin.reload`          |
| *(Nur Permission)* | Erlaubt es, im Pausenmodus Aktionen auszuführen. **(Standardmäßig für OPs aktiv)** | `challenge.admin.bypasspause`     |

## 🔄 Der Komplette Welt-Reset

Der `/achallenge reset`-Befehl setzt alle Spieler und das Plugin zurück. Für eine **komplett frische Welt** für die nächste Runde wird folgender, 100% sicherer Prozess empfohlen:

1.  **🎮 Ingame-Reset**
    * Führe den Befehl `/achallenge reset confirm` aus.
    * Das setzt alle Spieler-Stats zurück, leert die Listen und bereitet das Plugin auf eine neue Runde vor. Das Plugin wird dich im Chat mit weiteren Anweisungen leiten.

2.  **🛑 Server anhalten**
    * Fahre den Server komplett und sicher herunter, indem du `stop` in die Server-Konsole eingibst.

3.  **🗑️ Welt-Ordner löschen**
    * Öffne dein Haupt-Serververzeichnis.
    * Lösche die folgenden Ordner: `world`, `world_nether` und `world_the_end`.

4.  **▶️ Server neustarten**
    * Starte deinen Server wieder. Minecraft wird automatisch brandneue, frische Welten generieren.

5.  **🚀 Challenge starten**
    * Sobald alle auf dem Server sind, benutze `/achallenge start`, um die neue Challenge zu beginnen!
