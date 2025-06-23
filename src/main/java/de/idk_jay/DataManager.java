package de.idk_jay;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DataManager {

    private final Main plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main plugin) {
        this.plugin = plugin;
        org.bukkit.configuration.serialization.ConfigurationSerialization.registerClass(AdvancementEntry.class);
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Konnte die data.yml nicht speichern!");
            e.printStackTrace();
        }
    }

    public void saveData() {
        getConfig().set("challenge-state", plugin.getChallengeState().name());
        getConfig().set("timer.secondsElapsed", plugin.getChallengeTimer().getSecondsElapsed());
        getConfig().set("history", plugin.getAchievementHistory());
        saveConfig();
    }

    public void loadData() {
        if (getConfig().contains("challenge-state")) {
            try {
                Main.ChallengeState state = Main.ChallengeState.valueOf(getConfig().getString("challenge-state"));
                plugin.setChallengeState(state, false);
            } catch (IllegalArgumentException ignored) {}
        }
        if (getConfig().contains("timer.secondsElapsed")) {
            long seconds = getConfig().getLong("timer.secondsElapsed");
            plugin.getChallengeTimer().setSecondsElapsed(seconds);
        }
        if (getConfig().contains("history")) {
            Map<String, Object> rawMap = getConfig().getConfigurationSection("history").getValues(false);
            for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                if (entry.getValue() instanceof AdvancementEntry) {
                    plugin.getAchievementHistory().put(entry.getKey(), (AdvancementEntry) entry.getValue());
                }
            }
        }
    }
}