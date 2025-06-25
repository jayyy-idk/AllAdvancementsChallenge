package de.idk_jay;

import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (plugin.getAchievementHistory().size() >= plugin.getTotalAdvancements()) {
            triggerChallengeComplete();
        }
    }

    private void triggerChallengeComplete() {
        String finalTime = plugin.getChallengeTimer().getFormattedTime();
        plugin.setChallengeState(Main.ChallengeState.ENDED);
        new BukkitRunnable() {
            @Override
            public void run() {
                String total = String.valueOf(plugin.getTotalAdvancements());
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(ChatColor.GOLD + "==========================================");
                Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "      " + plugin.getLangManager().get("challenge-complete-title"));
                Bukkit.broadcastMessage(ChatColor.GOLD + "==========================================");
                Bukkit.broadcastMessage(plugin.getLangManager().get("challenge-complete-subtitle").replace("%total%", total));
                Bukkit.broadcastMessage(plugin.getLangManager().get("challenge-complete-final-time").replace("%time%", finalTime));
                Bukkit.broadcastMessage(" ");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    spawnFirework(p);
                }
            }
        }.runTaskLater(plugin, 20L);
    }

    private void spawnFirework(Player player) {
        Location loc = player.getLocation();
        Firework fw = player.getWorld().spawn(loc, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(1);
        fwm.addEffect(FireworkEffect.builder()
                .withColor(Color.LIME, Color.YELLOW, Color.AQUA)
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .build());
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }
}