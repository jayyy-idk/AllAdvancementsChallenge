package de.idk_jay;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ChallengeCommand implements CommandExecutor {

    private final Main plugin;

    public ChallengeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getLangManager().get("usage"));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "start":
                return handleStart(sender);
            case "pause":
                return handlePause(sender);
            case "resume":
                return handleResume(sender);
            case "stop":
                return handleStop(sender, args);
            case "reset":
                return handleReset(sender, args);
            case "lang":
                return handleLang(sender, args);
            case "autosave":
                return handleAutosave(sender, args);
            case "reload":
                return handleReload(sender);
            default:
                sender.sendMessage(plugin.getLangManager().get("error-unknown-command").replace("%usage%", "/achallenge <...>"));
                return true;
        }
    }

    private boolean handleStart(CommandSender sender) {
        if (!sender.hasPermission("challenge.admin.start")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-start"));
            return true;
        }
        Main.ChallengeState currentState = plugin.getChallengeState();
        if (currentState == Main.ChallengeState.RUNNING || currentState == Main.ChallengeState.PAUSED) {
            sender.sendMessage(plugin.getLangManager().get("start-fail-running"));
            return true;
        }
        if (currentState == Main.ChallengeState.ENDED) {
            plugin.getChallengeTimer().setSecondsElapsed(0);
        }
        if (currentState == Main.ChallengeState.WAITING_TO_START) {
            resetAllOnlinePlayers();
        }
        plugin.setChallengeState(Main.ChallengeState.RUNNING);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("start-title"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("start-by").replace("%player%", sender.getName()));
        Bukkit.broadcastMessage(plugin.getLangManager().get("start-gl"));
        Bukkit.broadcastMessage(" ");
        return true;
    }

    private boolean handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("challenge.admin.reset")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-reset"));
            return true;
        }
        if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
            performReset(sender);
            return true;
        }
        sender.sendMessage(plugin.getLangManager().get("line-separator"));
        sender.sendMessage(plugin.getLangManager().get("reset-confirm-title"));
        sender.sendMessage(plugin.getLangManager().get("reset-confirm-line1"));
        sender.sendMessage(plugin.getLangManager().get("reset-confirm-line2"));
        sender.sendMessage(plugin.getLangManager().get("reset-confirm-line3"));
        sender.sendMessage(plugin.getLangManager().get("line-separator"));
        return true;
    }

    private void performReset(CommandSender sender) {
        Bukkit.broadcastMessage(plugin.getLangManager().get("reset-broadcast"));
        plugin.getAchievementHistory().clear();
        plugin.getChallengeTimer().setSecondsElapsed(0);
        resetAllOnlinePlayers();
        plugin.setChallengeState(Main.ChallengeState.WAITING_TO_START, true);
        plugin.getDataManager().saveData();
        Bukkit.broadcastMessage(plugin.getLangManager().get("reset-success-broadcast"));
        sender.sendMessage(plugin.getLangManager().get("line-separator"));
        sender.sendMessage(plugin.getLangManager().get("reset-admin-info-title"));
        sender.sendMessage(plugin.getLangManager().get("reset-admin-info-start"));
        sender.sendMessage(" ");
        sender.sendMessage(plugin.getLangManager().get("reset-admin-info-world-title"));
        sender.sendMessage(plugin.getLangManager().get("reset-admin-info-world-1"));
        sender.sendMessage(plugin.getLangManager().get("reset-admin-info-world-2"));
        sender.sendMessage(plugin.getLangManager().get("reset-admin-info-world-3"));
        sender.sendMessage(plugin.getLangManager().get("line-separator"));
    }

    private void resetAllOnlinePlayers() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            Bukkit.dispatchCommand(console, "advancement revoke " + playerName + " everything");
            Bukkit.dispatchCommand(console, "clear " + playerName);
            Bukkit.dispatchCommand(console, "experience set " + playerName + " 0 points");
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.teleport(player.getWorld().getSpawnLocation());
            player.sendMessage(plugin.getLangManager().get("reset-player-message"));
        }
        plugin.getLogger().info("Spieler-Daten wurden für alle Online-Spieler zurückgesetzt.");
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("challenge.admin.reload")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-reload"));
            return true;
        }
        plugin.reloadConfig();
        plugin.getLangManager().loadLanguage();
        plugin.restartAutosaveTask();
        sender.sendMessage(plugin.getLangManager().get("config-reloaded"));
        return true;
    }

    private boolean handleAutosave(CommandSender sender, String[] args) {
        if (!sender.hasPermission("challenge.admin.autosave")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-autosave"));
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(plugin.getLangManager().get("autosave-usage"));
            return true;
        }
        try {
            int seconds = Integer.parseInt(args[1]);
            if (seconds < 5) {
                sender.sendMessage(plugin.getLangManager().get("autosave-fail-range"));
                return true;
            }
            plugin.getConfig().set("autosave-interval-seconds", seconds);
            plugin.saveConfig();
            plugin.restartAutosaveTask();
            sender.sendMessage(plugin.getLangManager().get("autosave-success").replace("%seconds%", String.valueOf(seconds)));
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getLangManager().get("autosave-fail-number"));
        }
        return true;
    }

    private boolean handleLang(CommandSender sender, String[] args) {
        if (!sender.hasPermission("challenge.admin.lang")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-lang"));
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(plugin.getLangManager().get("usage"));
            return true;
        }
        String langCode = args[1].toLowerCase();
        if (!langCode.equals("de") && !langCode.equals("en") && !langCode.equals("pl")) {
            sender.sendMessage(plugin.getLangManager().get("lang-fail-not-found").replace("%arg%", langCode));
            return true;
        }
        plugin.getConfig().set("language", langCode);
        plugin.saveConfig();
        plugin.getLangManager().loadLanguage();
        String langName = plugin.getLangManager().get("lang-name");
        sender.sendMessage(" ");
        sender.sendMessage(plugin.getLangManager().get("line-separator"));
        sender.sendMessage(plugin.getLangManager().get("lang-change-title"));
        sender.sendMessage(plugin.getLangManager().get("line-separator"));
        sender.sendMessage(plugin.getLangManager().get("lang-change-info").replace("%lang%", langName));
        sender.sendMessage(" ");
        return true;
    }

    private boolean handlePause(CommandSender sender) {
        if (!sender.hasPermission("challenge.admin.pause")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-pause"));
            return true;
        }
        if (plugin.getChallengeState() != Main.ChallengeState.RUNNING) {
            sender.sendMessage(plugin.getLangManager().get("pause-fail"));
            return true;
        }
        plugin.setChallengeState(Main.ChallengeState.PAUSED);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("pause-title"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("pause-by").replace("%player%", sender.getName()));
        Bukkit.broadcastMessage(plugin.getLangManager().get("pause-info"));
        Bukkit.broadcastMessage(" ");
        return true;
    }

    private boolean handleResume(CommandSender sender) {
        if (!sender.hasPermission("challenge.admin.pause")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-pause"));
            return true;
        }
        if (plugin.getChallengeState() != Main.ChallengeState.PAUSED) {
            sender.sendMessage(plugin.getLangManager().get("resume-fail"));
            return true;
        }
        plugin.setChallengeState(Main.ChallengeState.RUNNING);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("resume-title"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
        Bukkit.broadcastMessage(plugin.getLangManager().get("resume-by").replace("%player%", sender.getName()));
        Bukkit.broadcastMessage(plugin.getLangManager().get("resume-gl"));
        Bukkit.broadcastMessage(" ");
        return true;
    }

    private boolean handleStop(CommandSender sender, String[] args) {
        if (!sender.hasPermission("challenge.admin.stop")) {
            sender.sendMessage(plugin.getLangManager().get("error-no-perm-stop"));
            return true;
        }
        if (plugin.getChallengeState() == Main.ChallengeState.ENDED || plugin.getChallengeState() == Main.ChallengeState.WAITING_TO_START) {
            sender.sendMessage(plugin.getLangManager().get("stop-fail"));
            return true;
        }
        if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
            plugin.setChallengeState(Main.ChallengeState.ENDED);
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
            Bukkit.broadcastMessage(plugin.getLangManager().get("stop-title"));
            Bukkit.broadcastMessage(plugin.getLangManager().get("line-separator"));
            Bukkit.broadcastMessage(plugin.getLangManager().get("stop-by").replace("%player%", sender.getName()));
            Bukkit.broadcastMessage(plugin.getLangManager().get("stop-info"));
            Bukkit.broadcastMessage(" ");
            return true;
        }
        sender.sendMessage(plugin.getLangManager().get("stop-confirm-message"));
        return true;
    }
}