[English](./README.md) | [Deutsch](./README_de.md) | [Polski](./README_pl.md)
***

# Wyzwanie Wszystkich Osiągnięć

![Java Version](https://img.shields.io/badge/Java-17+-blue.svg) ![API Version](https://img.shields.io/badge/API-Spigot%201.21-orange.svg) ![License](https://img.shields.io/badge/License-MIT-green.svg)

Kompleksowa wtyczka Spigot zaprojektowana do zarządzania i ulepszania wyzwania "Wszystkie Osiągnięcia" między graczami. Zapewnia dynamiczny interfejs użytkownika, pełną kontrolę administracyjną oraz solidny system śledzenia, aby doświadczenie było jak najbardziej płynne.

## ✨ Funkcje

* **🏆 Śledzenie Postępów:** Dynamiczny Boss Bar wyświetla globalny postęp i działający stoper.
* **💾 Trwałe Przechowywanie Danych:** Cały postęp, stoper i status wyzwania są automatycznie zapisywane i przetrwają restarty serwera oraz awarie.
* **🌐 Wsparcie Wielojęzyczne:** Pełne wsparcie dla języka niemieckiego, angielskiego i polskiego. Język można zmieniać na żywo w grze lub poprzez plik konfiguracyjny.
* **🖥️ Graficzny Interfejs Użytkownika (GUI):** Interaktywne, paginowane menu (`/agui`) do przeglądania wszystkich osiągnięć. Ukończone osiągnięcia są podświetlone i zawierają szczegóły, takie jak gracz i znaczniki czasu.
* **👑 Pełna Kontrola Admina:** Kompletny zestaw komend do zarządzania cyklem życia wyzwania:
    * **Start:** Rozpoczyna wyzwanie i wyłącza standardowe komunikaty o osiągnięciach, aby czat był czystszy.
    * **Pause/Resume:** Całkowicie zamraża grę (stoper, graczy, moby) i płynnie ją wznawia.
    * **Stop:** Grzecznie kończy wyzwanie, zachowując wszystkie dane do wglądu.
    * **Reset:** Potężna komenda do resetowania wszystkich danych graczy i wtyczki, z krokiem potwierdzającym.
* **⚙️ W Pełni Konfigurowalna:** Dostosuj kolor Boss Bara, wiadomości i ustawienia techniczne za pomocą prostego pliku `config.yml`.
* **🎨 Dopracowane Powiadomienia:** Wszystkie ważne wydarzenia są komunikowane za pomocą pięknie sformatowanych, jasnych wiadomości na czacie.
* **🎉 Wielki Finał:** Konfigurowalny "Zwycięski" Boss Bar, ogłoszenie na cały serwer i pokaz fajerwerków świętują ukończenie wyzwania.

## 🚀 Instalacja

1.  Pobierz najnowszy plik `.jar` ze [strony Releases](https://github.com/jayyy-idk/AllAdvancementsChallenge/releases).
2.  Umieść plik `.jar` w folderze `plugins` swojego serwera Spigot/Paper.
3.  Zrestartuj lub przeładuj serwer.
4.  Wtyczka utworzy folder `AllAchievementsChallenge` zawierający pliki konfiguracyjne i językowe.

## 🛠️ Konfiguracja

Konfigurację można dostosować w pliku `plugins/AllAchievementsChallenge/config.yml`.

```yml
# Plik konfiguracyjny dla wtyczki AllAchievementsChallenge

# Jaki język powinien być używany?
# Dostępne: de, en, pl (domyślnie en).
language: 'pl'

# Ustawienia dla Boss Bara
bossbar:
  # Jaki kolor powinien mieć Boss Bar?
  # Dopuszczalne wartości: BLUE, GREEN, PINK, PURPLE, RED, WHITE, YELLOW
  color: 'YELLOW'
  # Jaki kolor powinien mieć Boss Bar po ukończeniu wyzwania?
  completion-color: 'GREEN'

# Ustawienia wiadomości
messages:
  # Jaki prefiks powinien znajdować się przed ogłoszeniem o osiągnięciu?
  # Możesz używać kodów kolorów z '&' (np. &6 dla złota).
  prefix: '&6[Osiągnięcia!]'

# Ile sekund powinno upłynąć między automatycznymi zapisami?
# Zbyt niskie wartości (poniżej 5) mogą wpłynąć na wydajność.
autosave-interval-seconds: 10
```

## 💻 Komendy i Uprawnienia

| Komenda                               | Opis                                                                         | Uprawnienie                       |
| ------------------------------------ | ---------------------------------------------------------------------------- | --------------------------------- |
| `/advancements`                      | Wyświetla chronologiczną listę wszystkich zdobytych osiągnięć.               | *(brak)* |
| `/agui`                              | Otwiera graficzny interfejs użytkownika dla wszystkich osiągnięć.             | *(brak)* |
| `/achallenge start`                  | Rozpoczyna zresetowane lub zakończone wyzwanie.                              | `challenge.admin.start`           |
| `/achallenge pause`                  | Wstrzymuje trwające wyzwanie.                                                | `challenge.admin.pause`           |
| `/achallenge resume`                 | Wznawia wstrzymane wyzwanie.                                                 | `challenge.admin.pause`           |
| `/achallenge stop confirm`           | Kończy wyzwanie, zachowując dane do wglądu.                                  | `challenge.admin.stop`            |
| `/achallenge reset confirm`          | Resetuje wszystkie dane graczy i wtyczki.                                    | `challenge.admin.reset`           |
| `/achallenge lang <de\|en\|pl>`      | Zmienia język wtyczki na żywo.                                               | `challenge.admin.lang`            |
| `/achallenge autosave <sekundy>`     | Zmienia interwał autozapisu na żywo.                                         | `challenge.admin.autosave`        |
| `/achallenge reload`                 | Przeładowuje pliki konfiguracyjne i językowe.                                | `challenge.admin.reload`          |
| *(Tylko Uprawnienie)* | Pozwala na poruszanie się i interakcję, gdy wyzwanie jest wstrzymane. **(Domyślnie aktywne dla OP)** | `challenge.admin.bypasspause`     |

## 🔄 Pełny Reset Świata

Komenda `/achallenge reset` resetuje wszystkich graczy i wtyczkę. Dla **całkowicie świeżego świata** na następną rundę zalecany jest następujący, w 100% bezpieczny proces:

1.  **🎮 Reset w Grze**
    * Uruchom komendę `/achallenge reset confirm`.
    * Zresetuje to wszystkie statystyki graczy, wyczyści historię i przygotuje wtyczkę na nową rundę. Wtyczka poprowadzi cię dalej za pomocą instrukcji na czacie.

2.  **🛑 Zatrzymaj Serwer**
    * Wyłącz serwer całkowicie i bezpiecznie, wpisując `stop` w konsoli serwera.

3.  **🗑️ Usuń Foldery Świata**
    * Otwórz główny katalog swojego serwera.
    * Usuń następujące foldery: `world`, `world_nether` i `world_the_end`.

4.  **▶️ Zrestartuj Serwer**
    * Uruchom serwer ponownie. Minecraft automatycznie wygeneruje zupełnie nowe, świeże światy.

5.  **🚀 Rozpocznij Wyzwanie**
    * Gdy wszyscy będą na serwerze, użyj `/achallenge start`, aby rozpocząć nowe wyzwanie!
