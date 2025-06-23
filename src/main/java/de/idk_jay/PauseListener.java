package de.idk_jay;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PauseListener implements Listener {

    private final Main plugin;
    private final TextComponent pauseMessage = new TextComponent(ChatColor.AQUA + "" + ChatColor.BOLD + "Die Challenge ist pausiert!");

    public PauseListener(Main plugin) {
        this.plugin = plugin;
    }

    private boolean isActionPaused(Player player) {
        if (plugin.getChallengeState() != Main.ChallengeState.RUNNING) {
            return player == null || !player.hasPermission("challenge.admin.bypasspause");
        }
        return false;
    }

    private void sendPauseMessage(Player player) {
        if (player != null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, pauseMessage);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (isActionPaused(event.getPlayer())) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ() || event.getFrom().getY() != event.getTo().getY()) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isActionPaused(event.getPlayer())) {
            event.setCancelled(true);
            sendPauseMessage(event.getPlayer());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isActionPaused(event.getPlayer())) {
            event.setCancelled(true);
            sendPauseMessage(event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (isActionPaused(event.getPlayer())) {
            event.setCancelled(true);
            sendPauseMessage(event.getPlayer());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (plugin.getChallengeState() != Main.ChallengeState.RUNNING) {
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                if (isActionPaused(damager)) {
                    event.setCancelled(true);
                    sendPauseMessage(damager);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (plugin.getChallengeState() != Main.ChallengeState.RUNNING) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setAI(false);
            }
        }
    }
}