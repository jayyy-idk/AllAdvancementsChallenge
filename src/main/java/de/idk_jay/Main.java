package de.idk_jay;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    public enum ChallengeState {
        RUNNING,
        PAUSED,
        WAITING_TO_START,
        ENDED
    }

    private final Map<String, AdvancementEntry> achievementHistory = new HashMap<>();
    private BossBar challengeBossBar;
    private BukkitTask challengeTimerTask;
    private ChallengeTimer challengeTimer;
    private int totalAdvancements = 0;
    private DataManager dataManager;
    private LangManager langManager;
    private BukkitTask autosaveTask;
    private ChallengeState currentState = ChallengeState.WAITING_TO_START;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.dataManager = new DataManager(this);
        this.langManager = new LangManager(this);
        getLogger().info("Plugin wird aktiviert! Listener und Befehle werden registriert...");
        calculateTotalAdvancements();
        this.challengeTimer = new ChallengeTimer(this);
        this.dataManager.loadData();
        createChallengeBossBar();
        this.challengeTimerTask = this.challengeTimer.runTaskTimer(this, 0L, 20L);

        restartAutosaveTask();

        getServer().getPluginManager().registerEvents(new AdvancementListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        getServer().getPluginManager().registerEvents(new PauseListener(this), this);

        this.getCommand("advancements").setExecutor(new HistoryCommand(this));
        this.getCommand("agui").setExecutor(new GuiCommand(this));
        this.getCommand("achallenge").setExecutor(new ChallengeCommand(this));

        getLogger().info("Plugin erfolgreich aktiviert. Es gibt " + totalAdvancements + " sichtbare Advancements.");
        setChallengeState(this.currentState, true);
    }

    @Override
    public void onDisable() {
        getLogger().info("Speichere finalen Fortschritt...");
        if (currentState == ChallengeState.RUNNING || currentState == ChallengeState.PAUSED || currentState == ChallengeState.WAITING_TO_START) {
            this.dataManager.saveData();
        }
        getLogger().info("Speicherung abgeschlossen.");
        if (challengeTimerTask != null) {
            challengeTimerTask.cancel();
        }
        if (autosaveTask != null) {
            autosaveTask.cancel();
        }
        if (challengeBossBar != null) {
            challengeBossBar.removeAll();
        }
        getLogger().info("Plugin wurde deaktiviert!");
    }

    public void restartAutosaveTask() {
        if (autosaveTask != null) {
            autosaveTask.cancel();
        }
        long intervalTicks = getConfig().getLong("autosave-interval-seconds", 10) * 20L;
        this.autosaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (currentState == ChallengeState.RUNNING || currentState == ChallengeState.PAUSED) {
                this.dataManager.saveData();
            }
        }, intervalTicks, intervalTicks);
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    private void calculateTotalAdvancements() {
        this.totalAdvancements = 0;
        Bukkit.advancementIterator().forEachRemaining(advancement -> {
            if (advancement.getDisplay() != null && !advancement.getDisplay().isHidden()) {
                this.totalAdvancements++;
            }
        });
    }

    private BarColor getConfigBarColor() {
        String colorString = getConfig().getString("bossbar.color", "YELLOW").toUpperCase();
        try {
            return BarColor.valueOf(colorString);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Ungültige Farbe '" + colorString + "' in der config.yml! Benutze stattdessen YELLOW.");
            return BarColor.YELLOW;
        }
    }

    private void createChallengeBossBar() {
        this.challengeBossBar = Bukkit.createBossBar("Challenge Timer", getConfigBarColor(), BarStyle.SOLID);
    }

    public void updateBossBar() {
        if (challengeBossBar == null || currentState != ChallengeState.RUNNING) {
            return;
        }
        challengeBossBar.setVisible(true);
        int earned = achievementHistory.size();
        double progress = (totalAdvancements > 0) ? (double) earned / totalAdvancements : 0.0;
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;
        challengeBossBar.setProgress(progress);
        String timeString = challengeTimer != null ? challengeTimer.getFormattedTime() : "00:00:00";
        String title = getLangManager().get("bossbar-title")
                .replace("%earned%", String.valueOf(earned))
                .replace("%total%", String.valueOf(totalAdvancements))
                .replace("%time%", timeString);
        challengeBossBar.setTitle(title);
    }

    public Map<String, AdvancementEntry> getAchievementHistory() {
        return achievementHistory;
    }

    public BossBar getChallengeBossBar() {
        return challengeBossBar;
    }

    public ChallengeTimer getChallengeTimer() {
        return challengeTimer;
    }

    public ChallengeState getChallengeState() {
        return currentState;
    }

    public void setChallengeState(ChallengeState newState) {
        setChallengeState(newState, true);
    }

    public void setChallengeState(ChallengeState newState, boolean performSideEffects) {
        this.currentState = newState;

        if (performSideEffects) {
            ConsoleCommandSender console = Bukkit.getConsoleSender();
            if (newState == ChallengeState.RUNNING) {
                Bukkit.dispatchCommand(console, "gamerule announceAdvancements false");
            } else {
                Bukkit.dispatchCommand(console, "gamerule announceAdvancements true");
            }
            boolean isFrozen = (newState == ChallengeState.PAUSED || newState == ChallengeState.WAITING_TO_START);
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                        ((LivingEntity) entity).setAI(!isFrozen);
                    }
                }
            }
        }

        switch (newState) {
            case RUNNING:
                challengeBossBar.setColor(getConfigBarColor());
                updateBossBar();
                break;
            case PAUSED:
                challengeBossBar.setVisible(true);
                challengeBossBar.setColor(BarColor.BLUE);
                challengeBossBar.setTitle(getLangManager().get("bossbar-paused"));
                challengeBossBar.setProgress(1.0);
                break;
            case WAITING_TO_START:
                challengeBossBar.setVisible(true);
                challengeBossBar.setColor(BarColor.RED);
                challengeBossBar.setTitle(getLangManager().get("bossbar-waiting"));
                challengeBossBar.setProgress(1.0);
                break;
            case ENDED:
                if (challengeBossBar != null) {
                    challengeBossBar.setVisible(false);
                }
                break;
        }
    }
}