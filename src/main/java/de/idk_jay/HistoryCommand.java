package de.idk_jay;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCommand implements CommandExecutor {

    private final Main plugin;

    public HistoryCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(ChatColor.GOLD + "--- Alle erreichten Advancements ---");

        if (plugin.getAchievementHistory().isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "Bisher wurden noch keine Advancements gemacht.");
        } else {
            for (AdvancementEntry entry : plugin.getAchievementHistory().values()) {
                String line = ChatColor.GRAY + "[" + entry.getGameTimestamp() + "] " +
                        ChatColor.YELLOW + entry.getPlayerName() +
                        ChatColor.WHITE + " -> " +
                        ChatColor.AQUA + entry.getAdvancementTitle();
                player.sendMessage(line);
            }
        }
        player.sendMessage(ChatColor.GOLD + "------------------------------------");
        return true;
    }
}