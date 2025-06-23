package de.idk_jay;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ChallengeTimer extends BukkitRunnable {

    private final Main plugin;
    private long secondsElapsed = 0;

    public ChallengeTimer(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.getChallengeState() != Main.ChallengeState.RUNNING) {
            return;
        }
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        secondsElapsed++;
        plugin.updateBossBar();
    }

    public String getFormattedTime() {
        long hours = secondsElapsed / 3600;
        long minutes = (secondsElapsed % 3600) / 60;
        long seconds = secondsElapsed % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public long getSecondsElapsed() {
        return secondsElapsed;
    }

    public void setSecondsElapsed(long seconds) {
        this.secondsElapsed = seconds;
    }
}