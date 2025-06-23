package de.idk_jay;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    private final Main plugin;

    public GuiListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(GuiCommand.GUI_TITLE_PREFIX)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int currentPage = GuiCommand.playerPages.getOrDefault(player.getUniqueId(), 1);

        // Den Befehls-Executor holen, damit wir seine Methoden aufrufen können
        GuiCommand guiCommand = (GuiCommand) plugin.getCommand("agui").getExecutor();
        if (guiCommand == null) return; // Sicherheitsabfrage

        String displayName = clickedItem.getItemMeta().getDisplayName();

        if (displayName.equals(ChatColor.YELLOW + "Nächste Seite")) {
            guiCommand.openGui(player, currentPage + 1);
        } else if (displayName.equals(ChatColor.YELLOW + "Vorherige Seite")) {
            guiCommand.openGui(player, currentPage - 1);
        }
    }
}