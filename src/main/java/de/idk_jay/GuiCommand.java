package de.idk_jay;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class GuiCommand implements CommandExecutor {

    public static final String GUI_TITLE_PREFIX = "Advancements Seite ";
    private final Main plugin;
    public static Map<UUID, Integer> playerPages = new HashMap<>();

    public GuiCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können die GUI öffnen.");
            return true;
        }
        openGui((Player) sender, 1);
        return true;
    }

    public void openGui(Player player, int page) {
        playerPages.put(player.getUniqueId(), page);
        Inventory gui = Bukkit.createInventory(player, 54, GUI_TITLE_PREFIX + page);

        Map<String, AdvancementEntry> history = plugin.getAchievementHistory();
        List<Advancement> allServerAdvancements = new ArrayList<>();
        Bukkit.advancementIterator().forEachRemaining(adv -> {
            if (adv.getDisplay() != null && !adv.getDisplay().isHidden()) {
                allServerAdvancements.add(adv);
            }
        });

        List<Advancement> earned = new ArrayList<>();
        List<Advancement> unearned = new ArrayList<>();
        for (Advancement adv : allServerAdvancements) {
            if (history.containsKey(adv.getDisplay().getTitle())) {
                earned.add(adv);
            } else {
                unearned.add(adv);
            }
        }

        earned.sort(Comparator.comparing(adv -> history.get(adv.getDisplay().getTitle()).getRealTimestamp()));

        List<Advancement> sortedDisplayList = new ArrayList<>();
        sortedDisplayList.addAll(earned);
        sortedDisplayList.addAll(unearned);

        int itemsPerPage = 45;
        int startIndex = (page - 1) * itemsPerPage;
        for (int i = 0; i < itemsPerPage; i++) {
            int advancementIndex = startIndex + i;
            if (advancementIndex >= sortedDisplayList.size()) break;

            Advancement adv = sortedDisplayList.get(advancementIndex);
            String title = adv.getDisplay().getTitle();
            String description = adv.getDisplay().getDescription();
            ItemStack item;

            if (history.containsKey(title)) {
                AdvancementEntry entry = history.get(title);
                item = createGuiItem(Material.LIME_DYE, ChatColor.GREEN + "" + ChatColor.BOLD + title,
                        ChatColor.ITALIC + description,
                        "",
                        ChatColor.GRAY + "Erreicht von: " + ChatColor.YELLOW + entry.getPlayerName(),
                        ChatColor.GRAY + "Ingame-Zeit: " + ChatColor.YELLOW + entry.getGameTimestamp(),
                        ChatColor.GRAY + "Echte Zeit: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(entry.getRealTimestamp())
                );
            } else {
                item = createGuiItem(Material.GRAY_DYE, ChatColor.GRAY + "" + ChatColor.BOLD + title,
                        ChatColor.ITALIC + description,
                        "",
                        ChatColor.DARK_GRAY + "Noch nicht erreicht."
                );
            }
            gui.setItem(i, item);
        }

        int maxPages = (int) Math.ceil((double) sortedDisplayList.size() / itemsPerPage);
        if (page > 1) {
            gui.setItem(45, createGuiItem(Material.ARROW, ChatColor.YELLOW + "Vorherige Seite", "Klicke hier, um zu Seite " + (page - 1) + " zu gelangen."));
        }
        if (page < maxPages) {
            gui.setItem(53, createGuiItem(Material.ARROW, ChatColor.YELLOW + "Nächste Seite", "Klicke hier, um zu Seite " + (page + 1) + " zu gelangen."));
        }

        player.openInventory(gui);
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}