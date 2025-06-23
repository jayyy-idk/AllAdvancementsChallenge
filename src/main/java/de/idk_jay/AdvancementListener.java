package de.idk_jay;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.Date;

public class AdvancementListener implements Listener {

    private final Main plugin;

    public AdvancementListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        if (plugin.getChallengeState() != Main.ChallengeState.RUNNING) {
            return;
        }

        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();

        if (advancement.getDisplay() == null || advancement.getDisplay().isHidden()) {
            return;
        }

        String advancementTitle = advancement.getDisplay().getTitle();

        if (plugin.getAchievementHistory().containsKey(advancementTitle)) {
            return;
        }

        String playerName = player.getName();
        String prefix = plugin.getLangManager().get("advancement-prefix");

        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("advancement-title").replace("%prefix%", prefix));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(plugin.getLangManager().get("advancement-player").replace("%player%", playerName));
        Bukkit.broadcastMessage(plugin.getLangManager().get("advancement-reached").replace("%advancement%", advancementTitle));
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(" ");

        String gameTimestamp = plugin.getChallengeTimer().getFormattedTime();
        Date realTimestamp = new Date();

        AdvancementEntry newEntry = new AdvancementEntry(playerName, advancementTitle, gameTimestamp, realTimestamp);
        plugin.getAchievementHistory().put(advancementTitle, newEntry);
        plugin.updateBossBar();

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            plugin.getDataManager().saveData();
        });
    }
}