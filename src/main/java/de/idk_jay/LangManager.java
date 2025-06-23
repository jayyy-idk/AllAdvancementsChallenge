package de.idk_jay;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LangManager {

    private final Main plugin;
    private FileConfiguration langConfig;
    private FileConfiguration fallbackConfig; // English fallback

    public LangManager(Main plugin) {
        this.plugin = plugin;
        loadLanguage();
    }

    public void loadLanguage() {
        String lang = plugin.getConfig().getString("language", "en");
        File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + lang + ".yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        try {
            InputStream fallbackStream = plugin.getResource("lang/en.yml");
            if (fallbackStream != null) {
                fallbackConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(fallbackStream));
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Could not load fallback language file (en.yml)!");
        }
    }

    public String get(String path) {
        String message = langConfig.getString(path);
        if (message == null) {
            message = fallbackConfig.getString(path, "&cMissing translation: " + path);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}